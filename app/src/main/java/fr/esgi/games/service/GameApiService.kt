package fr.esgi.games.service

import kotlinx.coroutines.Deferred
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import fr.esgi.games.model.GameDetail
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GameApiQueries {
    @GET("/games")
    fun fetchGameIds(): Deferred<List<Int>>

    @GET("/details")
    fun fetchGameDetail(@Query("appId") appId: Int): Deferred<GameDetail>
}

object GameApiService {
    private val api = Retrofit.Builder()
        .baseUrl("https://us-central1-kotlinesgigames.cloudfunctions.net")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(GameApiQueries::class.java)

    suspend fun fetchGameIds(): List<Int> {
        return api.fetchGameIds().await()
    }

    suspend fun fetchGameDetail(appId: Int): GameDetail {
        return api.fetchGameDetail(appId).await()
    }
}