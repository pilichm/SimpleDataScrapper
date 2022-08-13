package pl.pilichm.getcomposerimage.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.*
import pl.pilichm.getcomposerimage.R
import pl.pilichm.getcomposerimage.databinding.ActivityMainBinding
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MainActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var binding: ActivityMainBinding
    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        job = Job()

        binding.buttonSearchComposer.setOnClickListener {
            val composerName = binding.editTextComposerName.text
            if (!composerName.isNullOrEmpty()) {

//                GetComposerImageViewModel(GetComposerRepository())
//                    .getComposerImageUrl("John_Williams", binding.imageViewComposer)
                launch {
                    val result = getComposerWikipediaPage("John Williams")
                    println("RESULT: $result")

                    /**
                     * Extract imageUrl from html document.
                     */
                    CoroutineScope(Dispatchers.IO).launch {
                        runCatching {
                            val document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                                .parse(result)
                            println(1)
                            val source = DOMSource(document)
                            println(2)

                        }.onSuccess {
                            println("OK")
                        }
                    }
                }

            } else {
                Toast.makeText(this, "Please enter search name!", Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * Function for searching duck duck go for passed compoaser name.
     * Returns html content of search page.
     * */
    private suspend fun getComposerWikipediaPage(composerName: String) = suspendCoroutine<String> { cont ->
        val queue = Volley.newRequestQueue(this)
        val query = "john+williams+wikipedia"
        val url = "https://www.google.com/search?q=john+williams"

        val stringRequest = StringRequest(
            Request.Method.GET, "https://duckduckgo.com/html/?q=john+williams+wikipedia",
            { response ->
                cont.resume("Response is: ${response}")
            },
            { cont.resume("Something went wrong!") })

        queue.add(stringRequest)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}