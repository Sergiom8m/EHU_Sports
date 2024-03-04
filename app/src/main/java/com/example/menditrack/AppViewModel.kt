package com.example.menditrack

import android.app.NotificationManager
import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModel
import com.example.menditrack.data.Language
import com.example.menditrack.model.IActivityRepository
import com.example.menditrack.model.SportActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.io.FileWriter
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class AppViewModel @Inject constructor(
    private val activityRepository: IActivityRepository
) : ViewModel() {


    var actual_language by mutableStateOf(Language.ES)
    var showAddButton by mutableStateOf(true)
    var showNavBars by  mutableStateOf(true)
    var enableNavigationButtons by mutableStateOf(true)


    var activityToShow: MutableState<SportActivity?> = mutableStateOf(null)
    var activityToEdit: MutableState<SportActivity?> = mutableStateOf(null)

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
            Log.d("AÑADIDO", "AÑADIDO")
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
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(notificationContent))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(1, notification)
    }


    fun exportRouteToTXT(activity: SportActivity) {
        val estadoAlmacenamientoExterno = Environment.getExternalStorageState()
        if (estadoAlmacenamientoExterno == Environment.MEDIA_MOUNTED) {
            val directorioDescargas = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val archivo = File(directorioDescargas, "${activity.name}.txt")


            FileWriter(archivo).use { writer ->
                with(writer) {
                    append("NAME: ${activity.name}\n\n")
                    append("DISTANCE: ${activity.distance} km\n")
                    append("START POINT: ${activity.initPoint}\n")
                    append("GRADE: ${activity.grade} m\n")
                    append("DIFFICULTY: ${activity.difficulty}\n")
                }
            }
            Log.d("Download","Download")
        }
    }

    fun generateRandomId(name: String): Long {
        val hash = name.hashCode()

        val random = Random(hash.toLong())

        return random.nextLong()
    }

    private fun mapToEnglishDifficulty(selectedDifficulty: String): String {
        return when (selectedDifficulty.toLowerCase()) {
            "fácil", "erraza", "easy" -> "easy"
            "moderado", "ertaina", "moderate" -> "moderate"
            "difícil", "zaila", "hard" -> "hard"
            else -> selectedDifficulty
        }
    }

    private fun mapToEnglishSport(selectedSport: String): String {
        return when (selectedSport.toLowerCase()) {
            "ciclismo", "bizikleta", "cycling" -> "cycling"
            "carrera", "korrika", "running" -> "running"
            "caminata", "ibilaldia", "walking" -> "walking"
            else -> selectedSport
        }
    }

    @Composable
    fun mapToUserLanguageDifficulty(englishDifficulty: String): String {
        return when (englishDifficulty.toLowerCase()) {
            "easy" -> stringResource(id = R.string.easy)
            "moderate" -> stringResource(id = R.string.moderate)
            "hard" -> stringResource(id = R.string.hard)
            else -> englishDifficulty
        }
    }

    @Composable
    fun mapToUserLanguageSport(englishSport: String): String {
        return when (englishSport.toLowerCase()) {
            "running" -> stringResource(id = R.string.running)
            "walking" -> stringResource(id = R.string.walking)
            "cycling" -> stringResource(id = R.string.cycling)
            else -> englishSport
        }
    }




}