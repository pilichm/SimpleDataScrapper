package pl.pilichm.getcomposerimage.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import pl.pilichm.getcomposerimage.R
import androidx.databinding.DataBindingUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import pl.pilichm.getcomposerimage.databinding.ActivitySearchForAlbumComposerBinding
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
        return "Author not found!"
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}