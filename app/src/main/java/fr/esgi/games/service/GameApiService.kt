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
    fun fetchGameIds(): Deferred<ArrayList<Int>>

    @GET("/details")
    fun fetchGameDetail(@Query("appId") appId: Int): Deferred<GameDetail>

    @GET("/search")
    fun fetchGameSearch(@Query("text") text: String): Deferred<ArrayList<Int>>
}

object GameApiService {
    private val api = Retrofit.Builder()
        .baseUrl("https://us-central1-kotlinesgigames.cloudfunctions.net")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(GameApiQueries::class.java)

    suspend fun fetchGameIds(): ArrayList<Int>? {
        return try {
            api.fetchGameIds().await()
        } catch (e: Exception){
            null
        }
    }

    suspend fun fetchGameDetail(appId: Int): GameDetail? {
        return try {
            api.fetchGameDetail(appId).await()
        } catch (e: Exception){
            null
        }
    }

    suspend fun fetchGameSearch(text: String): ArrayList<Int>? {
        return try {
            api.fetchGameSearch(text).await()
        } catch (e: Exception){
            null
        }
    }
}