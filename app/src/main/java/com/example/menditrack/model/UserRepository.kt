package com.example.menditrack.model

import android.graphics.Bitmap
import android.util.Log
import com.example.menditrack.data.User
import com.example.menditrack.remote.ApiClient
import com.example.menditrack.utils.postUserToUser
import com.example.menditrack.utils.userToPostUser
import io.ktor.client.plugins.ResponseException
import javax.inject.Inject
import javax.inject.Singleton

interface IUserRepository{
    suspend fun addUser(user: User)
    suspend fun addUsersFromRemote()
    fun deleteUser(user: User)
    suspend fun getUser(username: String): User?
    suspend fun setUserProfile(username: String, image: Bitmap)
    suspend fun getUserProfile(username: String): Bitmap?
    suspend fun uploadUsers()
    suspend fun clearUsersOnRemote()
}

@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val apiClient: ApiClient
): IUserRepository {

    override suspend fun addUser(user: User) {
        userDao.addUser(user)
        try {
            apiClient.createUser(user)
        }
        catch (_: Exception){}
    }

    override suspend fun addUsersFromRemote() {
        userDao.clearUsers()
        val userList = apiClient.getUsers()
        userList.map { userDao.addUser(postUserToUser(it)) }
    }

    override fun deleteUser(user: User) {
        userDao.deleteUser(user)
    }

    override suspend fun getUser(username: String): User? {
        return userDao.getUser(username)
    }

    override suspend fun setUserProfile(username: String, image: Bitmap) {
        try {
          apiClient.setUserImage(username, image)
        } catch (_: ResponseException) {

        }
    }

    override suspend fun getUserProfile(username: String): Bitmap? {
        var image: Bitmap? = null
        try {
            image = apiClient.getUserImage(username)
        } catch (_: ResponseException) {

        }
        return image
    }

    override suspend fun uploadUsers(){
        val userList = userDao.getUsers()
        userList.map { apiClient.createUser(it) }
    }

    override suspend fun clearUsersOnRemote(){
        apiClient.clearUsers()
    }


}