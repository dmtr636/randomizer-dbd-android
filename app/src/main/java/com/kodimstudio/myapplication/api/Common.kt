package com.kodimstudio.myapplication.api

import com.kodimstudio.myapplication.retrofit.RetrofitClient

object Common {
    private const val STEAM_BASE_URL = "https://api.steampowered.com/"
    val retrofitServiceSteam: SteamApiServices
        get() = RetrofitClient.getClient(STEAM_BASE_URL).create(SteamApiServices::class.java)

    private const val OPENDOTA_BASE_URL = "https://api.opendota.com/"
    val retrofitServiceOpenDota: SteamApiServices
        get() = RetrofitClient.getClient(OPENDOTA_BASE_URL).create(SteamApiServices::class.java)
}