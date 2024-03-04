package com.example.menditrack.model

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface IActivityRepository{
    fun getActivitiesByType(type: String): Flow<List<SportActivity>>
    suspend fun addActivity(activity: SportActivity)
    suspend fun deleteActivity(activity: SportActivity)
    suspend fun updateActivity(activity: SportActivity)
}

@Singleton
class ActivityRepository @Inject constructor(
    private val activityDao: ActivityDao
) : IActivityRepository {

    override fun getActivitiesByType(type: String): Flow<List<SportActivity>> {
        return activityDao.getActivitiesByType(type)
    }

    override suspend fun addActivity(activity: SportActivity){
        activityDao. addActivity(activity)
    }

    override suspend fun deleteActivity(activity: SportActivity) {
        activityDao.deleteActivity(activity)
    }

    override suspend fun updateActivity(activity: SportActivity) {
        activityDao.updateActivity(activity)
    }


}