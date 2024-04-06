package com.example.menditrack.model

import com.example.menditrack.data.SportActivity
import com.example.menditrack.remote.ApiClient
import com.example.menditrack.utils.postActivityToActivity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// Interface-class file to create a intermediate repository between DAO and ViewModel

interface IActivityRepository{
    fun getActivitiesByType(type: String, username: String): Flow<List<SportActivity>>
    suspend fun addActivity(activity: SportActivity)
    suspend fun deleteActivity(activity: SportActivity)
    suspend fun updateActivity(activity: SportActivity)
    suspend fun addActivitiesFromRemote()
}

// The repository is unique and injects the DAO's constructor
@Singleton
class ActivityRepository @Inject constructor(
    private val activityDao: ActivityDao,
    private val apiClient: ApiClient
): IActivityRepository {

    override fun getActivitiesByType(type: String, username: String): Flow<List<SportActivity>> {
        return activityDao.getActivitiesByType(type, username)
    }

    override suspend fun addActivity(activity: SportActivity){
        activityDao. addActivity(activity)
        try {
            apiClient.createActivity(activity)
        }
        catch (_:Exception){

        }
    }

    override suspend fun deleteActivity(activity: SportActivity) {
        activityDao.deleteActivity(activity)
        try {
            apiClient.deleteActivity(activity)
        }
        catch (_:Exception){

        }
    }

    override suspend fun updateActivity(activity: SportActivity) {
        activityDao.updateActivity(activity)
        try {
            apiClient.updateActivity(activity)
        }
        catch (_:Exception){

        }
    }

    override suspend fun addActivitiesFromRemote() {
        val activityList = apiClient.getActivities()
        activityList.map { activityDao.addActivity(postActivityToActivity(it)) }
    }


}