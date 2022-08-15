package pl.pilichm.getcomposerimage.activities

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import pl.pilichm.getcomposerimage.Constants.Companion.DDG_SEARCH_URL
import pl.pilichm.getcomposerimage.R
import pl.pilichm.getcomposerimage.databinding.ActivitySearchForComposerImageBinding
import java.io.File
import java.io.FileOutputStream
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SearchForComposerImageActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var binding: ActivitySearchForComposerImageBinding
    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_for_composer_image)
        job = Job()

        binding.buttonSearchComposer.setOnClickListener {
            val composerName: String = binding.editTextComposerName.text.toString()
            if (composerName.isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    runCatching {
                        getComposerImageByName(composerName)
                    }.onSuccess {
                        println("OK")
                    }
                }
            } else {
                Toast.makeText(this, "Please enter search name!", Toast.LENGTH_LONG).show()
            }
        }

    }

    /**
     * Search duck duck go for passed composer name.
     * Extract image url from html document.
     * Returns first found matching image.
     */
    private fun getComposerImageByName(composerName: String) {
        val searchName = composerName.lowercase().replace(" ", "+")
        println("SEARCH NAME: $searchName")
        val searchUrl = "$DDG_SEARCH_URL$searchName+wikipedia"
        val doc: Document = Jsoup.connect(searchUrl).get()
        val links: Elements = doc.select("img[src]")
        var imageUrl = ""

        imageLoop@ for (link in links) {
            val src = link.attr("src")
            if (src.lowercase().contains("jpg")||src.lowercase().contains("png")) {
                println("IMAGE: $src")
                imageUrl = src
                break@imageLoop
            }
        }

        /**
         * If image url was found, download it and save to filesystem.
         * */
        if (imageUrl.isNotEmpty()) {
            println("Downloading: $imageUrl")
            val imageBitmap: Bitmap = Picasso.with(applicationContext).load(imageUrl).get()
//            val imagePath = "${getExternalFilesDir(Environment.DIRECTORY_PICTURES)}/composers"

            val imagePath = "${getExternalFilesDir(Environment.DIRECTORY_PICTURES)}/composers"

            val composerDir = File(imagePath)

            if (!composerDir.exists()){
                composerDir.mkdir()
            }

            val imageFileName = composerName.lowercase().replace(" ", "_")
            val imageFile = File(composerDir, "$imageFileName.jpg")

            if (!imageFile.exists()) {
                val fileOutputStream = FileOutputStream(imageFile)
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream)
                fileOutputStream.flush()
                fileOutputStream.close()
                println("Saved new image.")
            } else {
                println("Image for composer $composerName already exists!")
            }

            println("PATH: $imagePath")
            println("IMAGE LOADED!")

            binding.imageViewComposer.setImageBitmap(imageBitmap)
            println("IMAGE URI SET")
        } else {
            println("Image url for: $composerName not found!")
        }
    }

    /**
     * Function for searching duck duck go for passed compoaser name.
     * Returns html content of search page.
     * */
    private suspend fun getComposerWikipediaPage(composerName: String) = suspendCoroutine<String> { cont ->
        val queue = Volley.newRequestQueue(this)

        val stringRequest = StringRequest(
            Request.Method.GET, "https://duckduckgo.com/html/?q=john+williams+wikipedia",
            { response ->
                cont.resume("Response is: $response")
            },
            { cont.resume("Something went wrong!") })

        queue.add(stringRequest)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}