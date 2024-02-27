package com.example.menditrack

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.menditrack.data.Language
import com.example.menditrack.data.SportActivity


class AppViewModel: ViewModel() {

    var actual_language by mutableStateOf(Language.ES)
    var showAddButton by mutableStateOf(true)
    var enableNavigationButtons by mutableStateOf(true)

    var walk_activities: MutableList<SportActivity> = mutableListOf()
    var run_activities: MutableList<SportActivity> = mutableListOf()
    var cyc_activities: MutableList<SportActivity> = mutableListOf()

    init {
        // Actividades de prueba para caminar
        add_activity("Morning Walk", 2.5, "walking")
        add_activity("Evening Stroll", 1.8, "walking")
        add_activity("Morning Walk", 2.5, "walking")
        add_activity("Evening Stroll", 1.8, "walking")

        // Actividades de prueba para correr
        add_activity("Afternoon Run", 5.0, "running")
        add_activity("Weekend Jog", 3.2, "running")
        add_activity("Weekend Jog", 3.2, "running")

        // Actividades de prueba para ciclismo
        add_activity("Bike Ride to Work", 8.7, "cycling")
        add_activity("Mountain Biking Trail", 12.4, "cycling")
        add_activity("Mountain Biking Trail", 12.4, "cycling")
        add_activity("Mountain Biking Trail", 12.4, "cycling")
        add_activity("Mountain Biking Trail", 12.4, "cycling")
        add_activity("Mountain Biking Trail", 12.4, "cycling")

    }

    fun add_activity(name: String, distance: Double, type: String){
        when (type) {
            "walking" -> {
                val activity = SportActivity(name, distance)
                walk_activities.add(activity)
            }
            "running" -> {
                val activity = SportActivity(name, distance)
                run_activities.add(activity)
            }
            "cycling" -> {
                val activity = SportActivity(name, distance)
                cyc_activities.add(activity)
            }
        }
    }
}