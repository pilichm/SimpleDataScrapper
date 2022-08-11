package pl.pilichm.getcomposerimage.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import org.jsoup.Connection
import org.jsoup.Jsoup
import pl.pilichm.getcomposerimage.R
import pl.pilichm.getcomposerimage.databinding.ActivityMainBinding
import pl.pilichm.getcomposerimage.network.GetComposerImageViewModel
import pl.pilichm.getcomposerimage.network.GetComposerRepository

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.buttonSearchComposer.setOnClickListener {
            val composerName = binding.editTextComposerName.text
            if (!composerName.isNullOrEmpty()) {

                GetComposerImageViewModel(GetComposerRepository())
                    .getComposerImageUrl("John_Williams", binding.imageViewComposer)

            } else {
                Toast.makeText(this, "Please enter search name!", Toast.LENGTH_LONG).show()
            }
        }
    }
}