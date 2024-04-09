package com.example.menditrack.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

// Data class to store different sport activities
// Is a DB entity that corresponds to "activities" table with the ID field as primary key
@Entity(tableName = "activities",
    foreignKeys = [
        ForeignKey(entity = User::class, parentColumns = ["username"], childColumns = ["userId"], onDelete = ForeignKey.CASCADE)
    ]
)
data class SportActivity (
    @PrimaryKey val id: String,
    val name: String,
    val distance: Double,
    val initPoint: String,
    val grade: Double,
    val difficulty: String,
    val type: String,
    val userId: String
)

@Serializable
data class CompactActivity(
    val id: String,
    val name: String,
    val distance: Double,
    val initPoint: String,
    val grade: Double,
    val difficulty: String,
    val type: String,
    val userId: String
) {
    constructor(activity: SportActivity): this(
        id = activity.id,
        name = activity.name,
        distance = activity.distance,
        initPoint = activity.initPoint,
        grade = activity.grade,
        difficulty = activity.difficulty,
        type = activity.type,
        userId = activity.userId
    )

}