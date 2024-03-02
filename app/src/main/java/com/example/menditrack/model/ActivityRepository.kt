package com.example.menditrack.model

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface IActivityRepository{
    fun getAllActivities(): Flow<List<SportActivity>>
    suspend fun addActivity(activity: SportActivity)
}

@Singleton
class ActivityRepository @Inject constructor(
    private val activityDao: ActivityDao
) : IActivityRepository {

    override fun getAllActivities(): Flow<List<SportActivity>> {
        return activityDao.getAllActivities()
    }

    override suspend fun addActivity(activity: SportActivity){
        activityDao. addActivity(activity)
    }







}