package com.example.menditrack.model

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.menditrack.data.SportActivity
import com.example.menditrack.data.User

// Room database where entities are SportActivity objects
@Database(entities = [SportActivity::class, User::class], version = 3)
abstract class Database: RoomDatabase() {
   abstract fun activityDao(): ActivityDao
   abstract fun userDao(): UserDao

}