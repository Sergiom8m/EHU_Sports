package com.example.menditrack.model

import android.util.Log
import com.example.menditrack.data.User
import com.example.menditrack.remote.ApiClient
import com.example.menditrack.utils.postUserToUser
import javax.inject.Inject
import javax.inject.Singleton

interface IUserRepository{
    suspend fun addUser(user: User)
    suspend fun addUsersFromRemote()
    fun deleteUser(user: User)
    suspend fun getUser(username: String): User?
}

@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val apiClient: ApiClient
): IUserRepository {
    override suspend fun addUser(user: User) {
        try {
            apiClient.createUser(user)
        }
        catch (e: Exception){
            Log.d("RemoteDB","User insert failed")
        }
        userDao.addUser(user)
    }

    override suspend fun addUsersFromRemote() {
        val userList = apiClient.getUsers()
        userList.map {
            try {
                userDao.addUser(postUserToUser(it))
            }
            catch (e: Exception){
                Log.d("RemoteDB","User insert failed")
            }
        }
    }

    override fun deleteUser(user: User) {
        userDao.deleteUser(user)
    }

    override suspend fun getUser(username: String): User? {
        return userDao.getUser(username)
    }



}