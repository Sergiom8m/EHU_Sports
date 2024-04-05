package com.example.menditrack.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.menditrack.data.User

@Dao
interface UserDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    @Transaction
    @Delete
    fun deleteUser(user: User)

    @Transaction
    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUser(username: String): User?

}