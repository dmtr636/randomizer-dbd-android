package com.kodimstudio.myapplication.api.model

data class PlayerSummariesResponse(
    val response: ResponseContent
) {
    data class ResponseContent(
        val players: List<ResponsePlayer>
    )
    data class ResponsePlayer(
        val steamid: String,
        val personaname: String,
        val avatarfull: String
    )
}
