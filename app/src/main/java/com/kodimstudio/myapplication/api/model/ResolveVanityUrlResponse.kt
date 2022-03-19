package com.kodimstudio.myapplication.api.model

data class ResolveVanityUrlResponse (
    val response: ResponseContent? = null
)

data class ResponseContent (
    val steamid: Long? = null,
    val success: Int? = null
)