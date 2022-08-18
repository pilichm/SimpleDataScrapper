package pl.pilichm.getcomposerimage.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import pl.pilichm.getcomposerimage.R
import pl.pilichm.getcomposerimage.databinding.ActivitySearchForAlbumReleaseYearBinding
import pl.pilichm.getcomposerimage.network.NetworkUtil
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
        val query = NetworkUtil.convertTextToQuery("$albumName movie wikipedia")
        val wikipediaUrl = NetworkUtil.getWikipediaUrlByQuery(query)
        return if (wikipediaUrl!= NetworkUtil.SEARCH_VALUE_NOT_FOUND) {
            NetworkUtil.getMovieReleaseYearFromWikipedia(wikipediaUrl)
        } else {
            NetworkUtil.SEARCH_VALUE_NOT_FOUND
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}