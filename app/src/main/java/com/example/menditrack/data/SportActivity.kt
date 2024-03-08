package com.example.menditrack.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Data class to store different sport activities
// Is a DB entity that corresponds to "activities" table with the ID field as primary key
@Entity(tableName = "activities")
data class SportActivity (
    @PrimaryKey val id: Long,
    val name: String,
    val distance: Double,
    val initPoint: String,
    val grade: Double,
    val difficulty: String,
    val type: String
)