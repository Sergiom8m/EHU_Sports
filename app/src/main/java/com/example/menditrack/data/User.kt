package com.example.menditrack.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Data class to store different users
// Is a DB entity that corresponds to "users" table with the "username" field as primary key
@Entity(tableName = "users")
data class User(
    @PrimaryKey val username: String,
    val password: String,
)