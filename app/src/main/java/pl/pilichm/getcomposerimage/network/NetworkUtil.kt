package pl.pilichm.getcomposerimage.network

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import pl.pilichm.getcomposerimage.Constants

class NetworkUtil {
    companion object {
        /**
         * Const value returned when no matching value is found in search.
         */
        const val SEARCH_VALUE_NOT_FOUND = "not_found"

        /**
         * Get search results and extract url to movie wikipedia page.
         * First found url is returned.
         */
        fun getWikipediaUrlForMovie(movieName: String): String {
            val searchName = movieName.lowercase().replace(" ", "+")
            val searchUrl = "${Constants.DDG_SEARCH_URL}$searchName+movie+wikipedia"

            val doc: Document = Jsoup.connect(searchUrl).get()
            val links: Elements = doc.getElementsByAttributeValue("rel", "nofollow")

            return links[0].attr("href") ?: SEARCH_VALUE_NOT_FOUND
        }

        /**
         * Extract movie release year from its wikipedia page.
         * First found year is returned.
         */
        fun getMovieReleaseYearFromWikipedia(wikipediaMovieUrl: String): String {
            val doc: Document = Jsoup.connect(wikipediaMovieUrl).get()
            val elements: Elements = doc.getElementsByClass("bday dtstart published updated")
            return elements[0].text() ?: SEARCH_VALUE_NOT_FOUND
        }

        /**
         * Extract movie music composer from its wikipedia page.
         */
        fun getScoreAuthorFromWikipedia(wikipediaMovieUrl: String): String {
            val doc: Document = Jsoup.connect(wikipediaMovieUrl).get()
            val elements: Elements = doc.select("table tr td")


            println("SIZE ${elements.size}")
            for (element in elements){
                println("ELEMENT: ${element}")
            }

            return SEARCH_VALUE_NOT_FOUND
        }
    }
}