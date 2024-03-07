package com.example.menditrack.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// Interface to manage DB data (DAO -> Data Access Object)
@Dao
interface ActivityDao {

    @Query("SELECT * FROM activities WHERE type = :type ")
    fun getActivitiesByType(type: String): Flow<List<SportActivity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addActivity(activity: SportActivity)

    @Delete
    suspend fun deleteActivity(activity: SportActivity)

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateActivity(activity: SportActivity)
}