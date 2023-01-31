package fr.esgi.games.model

import android.net.Uri
import androidx.core.net.toUri

data class GameDetail(
    val appId: Int,
    val title: String,
    val editor: String,
    val price: String,
//    val description: String,
    val bgImage: String,
    ) {

    fun getSmallImage(): Uri? {
        val url =  String.format("https://cdn.origin.steamstatic.com/steam/apps/%s/library_600x900.jpg", appId.toString())
        return url.toUri().buildUpon().scheme("https").build()
    }
}