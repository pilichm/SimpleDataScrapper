package pl.pilichm.getcomposerimage.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import pl.pilichm.getcomposerimage.R
import pl.pilichm.getcomposerimage.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        /**
         * Start activity for searching composer image.
         */
        binding.buttonSearchForComposerImage.setOnClickListener {
            val intent = Intent(this, SearchForComposerImageActivity::class.java)
            startActivity(intent)
        }
    }
}