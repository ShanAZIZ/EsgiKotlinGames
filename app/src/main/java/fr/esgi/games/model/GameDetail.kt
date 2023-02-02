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
        val url =  String.format("http://cdn.origin.steamstatic.com/steam/apps/%s/library_600x900.jpg?t=1673331963", appId.toString())
        return url.toUri().buildUpon().scheme("http").build()
    }

    fun getBgrImage(): Uri? {
        val url = String.format("https://cdn.akamai.steamstatic.com/steam/apps/%s/page_bg_generated.jpg?t=1668125812", appId.toString())
        return url.toUri().buildUpon().scheme("https").build()
    }

    // TODO ONCLICK SUR DETAILS



}