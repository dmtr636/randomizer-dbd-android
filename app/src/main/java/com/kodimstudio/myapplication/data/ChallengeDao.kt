package com.kodimstudio.myapplication.data

import androidx.room.*
import com.kodimstudio.myapplication.model.Challenge

@Dao
interface ChallengeDao {
    @Query("SELECT * FROM challenge WHERE type = :type")
    fun getAll(type: String): List<Challenge>

    @Query("SELECT * FROM challenge WHERE completed = 0 AND type = :type")
    fun getUncompleted(type: String): List<Challenge>

    @Query("UPDATE challenge SET completed = :completed WHERE id = :id")
    fun setCompleted(id: Int, completed: Boolean)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(challenges: List<Challenge>)

    @Query("UPDATE challenge SET name = :name, text = :text WHERE id = :id")
    fun setName(id: Int, name: String, text: String)
}