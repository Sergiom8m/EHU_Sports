package com.example.menditrack.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SportActivity::class], version = 3)
abstract class Database: RoomDatabase() {
   abstract fun activityDao(): ActivityDao
}