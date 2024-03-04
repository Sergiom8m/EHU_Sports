package com.example.menditrack.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
@Dao
interface ActivityDao {

    @Query("SELECT * FROM activities WHERE type = :type ")
    fun getActivitiesByType(type: String): Flow<List<SportActivity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addActivity(activity: SportActivity)

    @Delete
    suspend fun deleteActivity(activity: SportActivity)

    @Update
    suspend fun updateActivity(activity: SportActivity)
}