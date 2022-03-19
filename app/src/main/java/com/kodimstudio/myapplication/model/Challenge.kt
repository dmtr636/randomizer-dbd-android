package com.kodimstudio.myapplication.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Challenge(
    @PrimaryKey val id: Int,
    val name: String,
    val text: String,
    val type: String,
    @ColumnInfo(name = "completed") var completed: Boolean
)
