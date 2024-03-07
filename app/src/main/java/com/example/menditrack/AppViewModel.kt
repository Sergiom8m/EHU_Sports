package com.example.menditrack

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModel
import com.example.menditrack.model.IActivityRepository
import com.example.menditrack.model.SportActivity
import com.example.menditrack.utils.generateRandomId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.example.menditrack.utils.*

@HiltViewModel
class AppViewModel @Inject constructor(
    private val activityRepository: IActivityRepository
) : ViewModel() {

    // Variables to show nav bars and floating button
    var showAddButton by mutableStateOf(true)
    var showNavBars by  mutableStateOf(true)

    // Variables to store activities to show and edit
    var activityToShow: MutableState<SportActivity?> = mutableStateOf(null)
    var activityToEdit: MutableState<SportActivity?> = mutableStateOf(null)




    /* ############################################################################################# */
    /* ######################### INTERACTION WITH THE ACTIVITY REPOSITORY ########################## */
    /* ############################################################################################# */
    fun getActivitiesByType(type: String): Flow<List<SportActivity>> {
        return activityRepository.getActivitiesByType(type)
    }

    suspend fun addActivity(
        routeName: String,
        routeDistance: Double,
        startingPoint: String,
        grade: Double,
        selectedDifficulty: String,
        selectedSport: String
    ) {
        try {
            val englishDifficulty = mapToEnglishDifficulty(selectedDifficulty)
            val englishSport = mapToEnglishSport(selectedSport)

            val activity = SportActivity(
                generateRandomId(routeName),
                routeName,
                routeDistance,
                startingPoint,
                grade,
                englishDifficulty,
                englishSport
            )
            activityRepository.addActivity(activity)
        }
        catch (e: Exception){
            Log.d("BASE DE DATOS", e.toString())
        }
    }

    suspend fun deleteActivity(activity: SportActivity){
        activityRepository.deleteActivity(activity)
    }

    suspend fun updateActivity(
        id: Long,
        routeName: String,
        routeDistance: Double,
        startingPoint: String,
        grade: Double,
        selectedDifficulty: String,
        selectedSport: String
    ){
        val englishDifficulty = mapToEnglishDifficulty(selectedDifficulty)
        val englishSport = mapToEnglishSport(selectedSport)

        val updatedActivity = SportActivity(id, routeName, routeDistance, startingPoint, grade, englishDifficulty, englishSport)
        activityRepository.updateActivity(updatedActivity)
    }

    fun sendAddNotification(
        context: Context,
        title: String,
        content: String
    ) {

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        val notificationTitle = title
        val notificationContent = content

        val notification = NotificationCompat.Builder(context, MainActivity.CHANNEL_ID)
            .setContentTitle(notificationTitle)
            .setContentText(notificationContent)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.correct)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(notificationContent))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(1, notification)
    }



}