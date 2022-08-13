package pl.pilichm.getcomposerimage.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Connection
import org.jsoup.Jsoup

class GetComposerRepository {
    companion object {
//        private const val base_url = "https://en.wikipedia.org/wiki/"
        private const val base_url = "http://www.google.com/"
    }

    suspend fun searchForComposerWikipediaPage(composerName: String): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val url = "$base_url$composerName"
                val conn = Jsoup.connect(url).method(Connection.Method.GET)
                val resp = conn.execute()
                println("RESPONSE: ${resp.body()}")
                Result.success("ok")
            } catch (e: Exception) {
                Result.failure(Exception("Cannot get composer image!"))
            }
        }
    }
}