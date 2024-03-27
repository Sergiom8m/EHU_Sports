package com.example.menditrack.model

import com.example.menditrack.data.User
import javax.inject.Inject
import javax.inject.Singleton

interface IUserRepository{
    suspend fun addUser(user: User)
    fun deleteUser(user: User)
    suspend fun getUser(username: String): User?
}

@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao
): IUserRepository {
    override suspend fun addUser(user: User) {
        userDao.addUser(user)
    }

    override fun deleteUser(user: User) {
        userDao.deleteUser(user)
    }

    override suspend fun getUser(username: String): User? {
        return userDao.getUser(username)
    }


}