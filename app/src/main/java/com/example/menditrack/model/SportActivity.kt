package com.example.menditrack.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activities")
data class SportActivity (
    @PrimaryKey val name: String,
    val distance: Double,
    val initPoint: String,
    val grade: Double,
    val difficulty: String,
    val type: String
)