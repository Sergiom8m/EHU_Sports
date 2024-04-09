package com.example.menditrack.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.menditrack.data.SportActivity
import kotlinx.coroutines.flow.Flow

// Interface to manage DB data (DAO -> Data Access Object)
@Dao
interface ActivityDao {

    @Transaction
    @Query("SELECT * FROM activities")
    fun getActivities(): Flow<List<SportActivity>>

    @Transaction
    @Query("SELECT * FROM activities WHERE type = :type AND userId = :username")
    fun getActivitiesByType(type: String, username: String): Flow<List<SportActivity>>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addActivity(activity: SportActivity)

    @Transaction
    @Delete
    suspend fun deleteActivity(activity: SportActivity)

    @Transaction
    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateActivity(activity: SportActivity)
}