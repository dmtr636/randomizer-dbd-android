package com.kodimstudio.myapplication.model

data class Character(
    val imageResourceId: Int,
    var selected: Boolean = false,
    val powerAddons: List<Int>,
    val powerImageResId: Int
)
