package com.example.week11codingchallange

import android.content.ClipData
import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface Dao {
    @Insert
    fun add(word:Words)

    @Query("SELECT * FROM Words")
    fun getAll(): List<Words>

    @Query("SELECT * FROM Words WHERE word LIKE :search ")
    fun findWord(search:String):MutableList<Words>
}