package com.example.week11codingchallange

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Words::class],
    version = 1
)
abstract class Database: RoomDatabase(){
    abstract fun funDao():Dao

}