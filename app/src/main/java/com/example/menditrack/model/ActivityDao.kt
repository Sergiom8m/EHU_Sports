package com.example.menditrack.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
@Dao
interface ActivityDao {

    @Query("SELECT * FROM activities")
    fun getAllActivities(): Flow<List<SportActivity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addActivity(activity: SportActivity)
}