package com.example.menditrack

import android.annotation.SuppressLint
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
import com.example.menditrack.data.SportActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException


@SuppressLint("MutableCollectionMutableState")
class AppViewModel: ViewModel() {


    private val _change = MutableStateFlow(false)
    val change: StateFlow<Boolean>
        get() = _change

    var actual_language by mutableStateOf(Language.ES)
    var showAddButton by mutableStateOf(true)
    var showNavBars by  mutableStateOf(true)
    var enableNavigationButtons by mutableStateOf(true)

    var walk_activities: MutableList<SportActivity> = mutableListOf()
    var run_activities: MutableList<SportActivity> = mutableListOf()
    var cyc_activities: MutableList<SportActivity> = mutableListOf()

    var activityToShow: MutableState<SportActivity?> = mutableStateOf(null)

    init {
        // Actividades de prueba para caminar
        add_activity("Morning Walk", 2.5, "Bilbao", 3.5, "Easy", "Walking")
        add_activity("Evening Stroll", 1.8, "Madrid", 2.8, "Moderate", "Walking")
        add_activity("Morning Walk", 2.5, "Barcelona", 3.2, "Easy", "Walking")
        add_activity("Evening Stroll", 1.8, "Valencia", 2.5, "Moderate", "Walking")

        // Actividades de prueba para correr
        add_activity("Afternoon Run", 5.0, "Sevilla", 4.0, "Hard", "Running")
        add_activity("Weekend Jog", 3.2, "Granada", 3.7, "Moderate", "Running")
        add_activity("Weekend Jog", 3.2, "Málaga", 3.7, "Moderate", "Running")

        // Actividades de prueba para ciclismo
        add_activity("Bike Ride to Work", 8.7, "Vitoria-Gasteiz", 5.0, "Hard", "Cycling")
        add_activity("Mountain Biking Trail", 12.4, "San Sebastián", 6.5, "Hard", "Cycling")
        add_activity("Mountain Biking Trail", 12.4, "Pamplona", 6.5, "Hard", "Cycling")
        add_activity("Mountain Biking Trail", 12.4, "Bilbao", 6.5, "Hard", "Cycling")
        add_activity("Mountain Biking Trail", 12.4, "Logroño", 6.5, "Hard", "Cycling")
        add_activity("Mountain Biking Trail", 12.4, "Santander", 6.5, "Hard", "Cycling")
    }


    fun add_activity(name: String, distance: Double, initPoint: String, grade: Double, difficulty: String, type: String){
        when (type) {
            "Caminata", "Ibilaldia", "Walking"  -> {
                val activity = SportActivity(name, distance, initPoint, grade, difficulty)
                walk_activities.add(activity)
            }
            "Carrera", "Korrika", "Running" -> {
                val activity = SportActivity(name, distance, initPoint, grade, difficulty)
                run_activities.add(activity)
            }
            "Ciclismo", "Bizikleta", "Cycling" -> {
                val activity = SportActivity(name, distance, initPoint, grade, difficulty)
                cyc_activities.add(activity)
            }
        }
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

    fun deleteActivity(activity: SportActivity, type: String) {
        when (type) {
            "Walking" -> walk_activities.remove(activity)
            "Running" -> run_activities.remove(activity)
            "Cycling" -> cyc_activities.remove(activity)
        }
        this.triggerchange()

    }

    private fun triggerchange() {
        _change.value = true
    }

    // Esta función se llama cuando se ha completado la actualización
    fun changeComplete() {
        _change.value = false
    }

    fun exportRouteToTXT(route: SportActivity) {
        val estadoAlmacenamientoExterno = Environment.getExternalStorageState()
        if (estadoAlmacenamientoExterno == Environment.MEDIA_MOUNTED) {
            val directorioDescargas = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val archivo = File(directorioDescargas, "${route.name}.txt")


            FileWriter(archivo).use { writer ->
                with(writer) {
                    append("NAME: ${route.name}\n\n")
                    append("DISTANCE: ${route.distance} km\n")
                    append("INIT POINT: ${route.initPoint}\n")
                    append("GRADE: ${route.grade} m\n")
                    append("DIFFICULTY: ${route.difficulty}\n")
                }
            }
            Log.d("Download","Download")

        }
    }


}