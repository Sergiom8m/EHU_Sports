package com.example.menditrack

import android.app.NotificationManager
import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    fun getAllActivities(): Flow<List<SportActivity>> {
        return activityRepository.getAllActivities()
    }

    suspend fun addActivity(activity: SportActivity) {
        try {
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
                    append("NOMBRE: ${activity.name}\n\n")
                    append("DISTANCIA: ${activity.distance} km\n")
                    append("PUNTO DE INICIO: ${activity.initPoint}\n")
                    append("DESNIVEL: ${activity.grade} m\n")
                    append("DIFICULTAD: ${activity.difficulty}\n")
                }
            }
            Log.d("Download","Download")

        }
    }


}