package pl.pilichm.getcomposerimage.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import pl.pilichm.getcomposerimage.Constants.Companion.DDG_SEARCH_URL
import pl.pilichm.getcomposerimage.R
import pl.pilichm.getcomposerimage.databinding.ActivitySearchForAlbumReleaseYearBinding
import java.util.regex.Pattern
import kotlin.coroutines.CoroutineContext

class SearchForAlbumReleaseYearActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var binding: ActivitySearchForAlbumReleaseYearBinding
    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_for_album_release_year)
        job = Job()

        binding.buttonSearchAlbumYear.setOnClickListener {
            val albumName: String = binding.editTextAlbumName.text.toString()

            if (albumName.isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch{
                    runCatching {
                        binding.textViewAlbumYear.text = getAlbumReleaseYear(albumName)
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
     * Searches for info about movie in duck duck go.
     * Checks elements of class result__snippet.
     * Returns first text from matched elements, that consists of four num digits.
     */
    private fun getAlbumReleaseYear(albumName: String): String {
        val searchName = albumName.lowercase().replace(" ", "+")
        val searchUrl = "$DDG_SEARCH_URL$searchName+movie+year"
        val yearPattern = Pattern.compile("[0-9]{4}")

        val doc: Document = Jsoup.connect(searchUrl).get()
        val elements: Elements = doc.getElementsByClass("result__snippet")

        for (element in elements) {
            val elementText = element.text()
            val matcher = yearPattern.matcher(elementText)
            if (matcher.find()){
                return elementText.substring(matcher.start(), matcher.end())
            }
        }

        return "Not found"
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}