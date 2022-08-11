package pl.pilichm.getcomposerimage.network

import android.widget.ImageView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GetComposerImageViewModel(private val getComposerRepository: GetComposerRepository): ViewModel() {
    fun getComposerImageUrl(composerName: String, imageView: ImageView) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = getComposerRepository.makeRequest(composerName)
            imageView.contentDescription = result.getOrNull()?.substring(100) ?: "Not found!"
            println("RESULT::: ${result.getOrNull()?.substring(100)}")
        }
    }
}