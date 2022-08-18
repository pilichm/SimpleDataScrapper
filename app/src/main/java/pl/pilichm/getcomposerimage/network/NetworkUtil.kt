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
        const val WIKIPEDIA_IMAGE_URL_BEG = "https://upload.wikimedia.org/wikipedia/commons"

        /**
         * Converts search query to query string format for duck duck go.
         */
        fun convertTextToQuery(searchText: String): String =
            searchText.lowercase().replace(" ", "+")

        /**
         * Get search results and extract url to movie wikipedia page.
         * First found url is returned.
         */
        fun getWikipediaUrlByQuery(query: String): String {
            val searchUrl = "${Constants.DDG_SEARCH_URL}$query"
            val doc: Document = Jsoup.connect(searchUrl).get()
            val links: Elements = doc.body().getElementsByAttributeValue("rel", "nofollow")
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

            /**
             * Music author is in ninth index.
             */
            return elements[8].text() ?: SEARCH_VALUE_NOT_FOUND
        }

        /**
         * Method for extracting composer image from wikipedia page.
         * Returns url to found image.
         */
        fun getAuthorImageFromWikipedia(wikipediaUrl: String): String {
            val doc: Document = Jsoup.connect(wikipediaUrl).get()
            val elements = doc.body().select("img[src]")

            val fullPath = elements[1].attr("src")
            println("PATH: $fullPath")
            val split1 = fullPath.split("thumb")[1]

            val split2 = split1.split("/")

            var final = SEARCH_VALUE_NOT_FOUND

            for (index in 0..split2.size){
                if (index<split2.size-1){
                    final += "/${split2[index]}"
                }
            }

            return "$WIKIPEDIA_IMAGE_URL_BEG$final"
        }
    }
}