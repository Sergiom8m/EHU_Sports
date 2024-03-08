package com.example.menditrack.model

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.menditrack.data.SportActivity

// Room database where entities are SportActivity objects
@Database(entities = [SportActivity::class], version = 3)
abstract class Database: RoomDatabase() {
   abstract fun activityDao(): ActivityDao
}