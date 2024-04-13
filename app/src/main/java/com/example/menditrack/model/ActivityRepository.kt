package com.example.menditrack.model

import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import com.example.menditrack.data.SportActivity
import com.example.menditrack.remote.ApiClient
import com.example.menditrack.utils.postActivityToActivity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// Interface-class file to create a intermediate repository between DAO and ViewModel

interface IActivityRepository{
    fun getActivities(): Flow<List<SportActivity>>
    fun getActivitiesByType(type: String, username: String): Flow<List<SportActivity>>
    suspend fun addActivity(activity: SportActivity)
    suspend fun deleteActivity(activity: SportActivity)
    suspend fun updateActivity(activity: SportActivity)
    suspend fun addActivitiesFromRemote()
    suspend fun uploadActivities()
    suspend fun clearActivitiesOnRemote()

}

// The repository is unique and injects the DAO's constructor
@Singleton
class ActivityRepository @Inject constructor(
    private val activityDao: ActivityDao,
    private val apiClient: ApiClient
): IActivityRepository {

    override fun getActivities():  Flow<List<SportActivity>> {
        return activityDao.getActivities()
    }

    override fun getActivitiesByType(type: String, username: String): Flow<List<SportActivity>> {
        return activityDao.getActivitiesByType(type, username)
    }

    override suspend fun addActivity(activity: SportActivity){
        try {
            apiClient.createActivity(activity)
            activityDao.addActivity(activity)
        }
        catch (_:Exception){

        }
    }

    override suspend fun deleteActivity(activity: SportActivity) {
        try {
            apiClient.deleteActivity(activity)
            activityDao.deleteActivity(activity)
        }
        catch (_:Exception){

        }
    }

    override suspend fun updateActivity(activity: SportActivity) {
        try {
            apiClient.updateActivity(activity)
            activityDao.updateActivity(activity)
        }
        catch (_:Exception){

        }
    }

    override suspend fun addActivitiesFromRemote() {
        activityDao.clearActivities()
        val activityList = apiClient.getActivities()
        activityList.map { activityDao.addActivity(postActivityToActivity(it)) }
    }

    override suspend fun uploadActivities(){
        val activityList = activityDao.getActivities().first()
        activityList.map { apiClient.createActivity(it) }

    }

    override suspend fun clearActivitiesOnRemote(){
        apiClient.clearActivities()
    }

}