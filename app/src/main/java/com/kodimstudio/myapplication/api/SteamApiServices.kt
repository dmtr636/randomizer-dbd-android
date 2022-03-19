package com.kodimstudio.myapplication.api

import com.kodimstudio.myapplication.BuildConfig
import com.kodimstudio.myapplication.api.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

const val API_KEY = BuildConfig.API_KEY

interface SteamApiServices {
    @GET("ISteamUser/ResolveVanityURL/v1/?key=${API_KEY}")
    fun resolveVanityUrl(@Query("vanityurl") vanityUrl: String): Call<ResolveVanityUrlResponse>

    @GET("ISteamUser/GetPlayerSummaries/v2/?key=${API_KEY}")
    fun getPlayerSummaries(@Query("steamids") steamids: Long): Call<PlayerSummariesResponse>

    @GET("api/search")
    fun searchPlayer(@Query("q") q: String): Call<List<SearchPlayerResponse>>
}