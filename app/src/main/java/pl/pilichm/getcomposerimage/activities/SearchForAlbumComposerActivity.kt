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
import pl.pilichm.getcomposerimage.databinding.ActivitySearchForAlbumComposerBinding
import pl.pilichm.getcomposerimage.network.NetworkUtil
import pl.pilichm.getcomposerimage.network.NetworkUtil.Companion.SEARCH_VALUE_NOT_FOUND
import kotlin.coroutines.CoroutineContext

class SearchForAlbumComposerActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var binding: ActivitySearchForAlbumComposerBinding
    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_for_album_composer)
        job = Job()

        binding.buttonSearchAlbumAuthor.setOnClickListener {
            val albumName: String = binding.editTextAlbumAuthor.text.toString()

            if (albumName.isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    runCatching {
                        binding.textViewAlbumAuthor.text = getAlbumAuthor(albumName)
                    }.onSuccess {
                        println("OK")
                    }
                }
            } else {
                Toast.makeText(this, "Please enter search name!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getAlbumAuthor(albumName: String): String {
        val query = NetworkUtil.convertTextToQuery("$albumName movie wikipedia")
        val wikipediaUrl = NetworkUtil.getWikipediaUrlByQuery(query)
        return if (wikipediaUrl!=SEARCH_VALUE_NOT_FOUND) {
            NetworkUtil.getScoreAuthorFromWikipedia(wikipediaUrl)
        } else {
            SEARCH_VALUE_NOT_FOUND
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}