package com.kodimstudio.myapplication.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kodimstudio.myapplication.model.Challenge
import com.kodimstudio.myapplication.model.Player

@Database(entities = [Challenge::class, Player::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun challengeDao(): ChallengeDao
    abstract fun playerDAO(): PlayerDAO
}