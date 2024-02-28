package com.example.menditrack

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModel
import com.example.menditrack.data.Language
import com.example.menditrack.data.SportActivity


class AppViewModel: ViewModel() {

    var actual_language by mutableStateOf(Language.ES)
    var showAddButton by mutableStateOf(true)
    var showSettingButton by  mutableStateOf(true)
    var enableNavigationButtons by mutableStateOf(true)

    var walk_activities: MutableList<SportActivity> = mutableListOf()
    var run_activities: MutableList<SportActivity> = mutableListOf()
    var cyc_activities: MutableList<SportActivity> = mutableListOf()

    init {
        // Actividades de prueba para caminar
        add_activity("Morning Walk", 2.5, "Walking")
        add_activity("Evening Stroll", 1.8, "Walking")
        add_activity("Morning Walk", 2.5, "Walking")
        add_activity("Evening Stroll", 1.8, "Walking")

        // Actividades de prueba para correr
        add_activity("Afternoon Run", 5.0, "Running")
        add_activity("Weekend Jog", 3.2, "Running")
        add_activity("Weekend Jog", 3.2, "Running")

        // Actividades de prueba para ciclismo
        add_activity("Bike Ride to Work", 8.7, "Cycling")
        add_activity("Mountain Biking Trail", 12.4, "Cycling")
        add_activity("Mountain Biking Trail", 12.4, "Cycling")
        add_activity("Mountain Biking Trail", 12.4, "Cycling")
        add_activity("Mountain Biking Trail", 12.4, "Cycling")
        add_activity("Mountain Biking Trail", 12.4, "Cycling")

    }

    fun add_activity(name: String, distance: Double, type: String){
        when (type) {
            "Caminata", "Ibilaldia", "Walking"  -> {
                val activity = SportActivity(name, distance)
                walk_activities.add(activity)
            }
            "Carrera", "Korrika", "Running" -> {
                val activity = SportActivity(name, distance)
                run_activities.add(activity)
            }
            "Ciclismo", "Bizikleta", "Cycling" -> {
                val activity = SportActivity(name, distance)
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
}