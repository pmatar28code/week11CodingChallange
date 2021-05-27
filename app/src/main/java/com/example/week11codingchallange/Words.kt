package com.example.week11codingchallange

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Words")
data class Words(
    @PrimaryKey(autoGenerate = true)val key:Int=0,
    @ColumnInfo(name="word")val word: String
)