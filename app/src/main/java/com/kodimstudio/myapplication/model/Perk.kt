package com.kodimstudio.myapplication.model

data class Perk(
    val imageResourceId: Int,
    val name: String,
    var excluded: Boolean = false
)
