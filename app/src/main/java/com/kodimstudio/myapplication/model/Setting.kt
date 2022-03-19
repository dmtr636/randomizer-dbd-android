package com.kodimstudio.myapplication.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Setting(
    @PrimaryKey val key: String,
    @ColumnInfo(name = "value") val value: String
)
