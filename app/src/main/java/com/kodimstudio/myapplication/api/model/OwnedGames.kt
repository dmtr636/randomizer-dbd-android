package com.kodimstudio.myapplication.api.model

data class OwnedGames(
    val response: OwnedGamesResponse
) {
    data class OwnedGamesResponse(
        val games: List<OwnedGamesGame>?
    ) {
        data class OwnedGamesGame(
            val appid: Int,
            val playtime_forever: Int
        )
    }
}
