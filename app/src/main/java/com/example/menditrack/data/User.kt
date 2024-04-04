package com.example.menditrack.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.menditrack.remote.PostActivity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = "users")
data class User(
    @PrimaryKey val username: String,
    val password: String
)



