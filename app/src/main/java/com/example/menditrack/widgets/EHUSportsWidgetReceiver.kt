package com.example.menditrack.widgets

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.example.menditrack.data.CompactActivity
import com.example.menditrack.model.ActivityRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@AndroidEntryPoint
class EHUSportsWidgetReceiver() : GlanceAppWidgetReceiver(){

    // Initialize the EHUSportsWidget and coroutine scope
    override val glanceAppWidget: GlanceAppWidget = EHUSportsWidget()
    private val coroutineScope = MainScope()

    // Inject the activity repository using Dagger
    @Inject lateinit var activityRepository: ActivityRepository

    // Function called when the widget is updated
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        observeData(context)
    }

    // Function called when the widget receives a broadcast
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        // Filter actions in order to prevent updating twice in onUpdate events.
        if (intent.action == "updateAction" || intent.action.equals("ACTION_TRIGGER_LAMBDA")) {
            observeData(context)
        }
    }

    // Function to observe data changes
    private fun observeData(context: Context) {
        // Launch a coroutine
        coroutineScope.launch {
            // Log a debug message indicating coroutine execution
            Log.d("Widget", "Coroutine Called")

            // Retrieve activities from the activity repository
            val activities = activityRepository.getActivities().first().map(::CompactActivity)

            // Log the number of activities retrieved
            Log.d("Widget", "Coroutine - Data-Length: ${activities.size}")

            // Iterate through all Glance widgets of type EHUSportsWidget
            GlanceAppWidgetManager(context).getGlanceIds(EHUSportsWidget::class.java).forEach { glanceId ->
                // Update the widget state
                updateAppWidgetState(context, PreferencesGlanceStateDefinition, glanceId) { widgetDataStore ->
                    // Update widget data with the retrieved activities
                    widgetDataStore.toMutablePreferences().apply {
                        this[globalActivities] = Json.encodeToString(activities)
                    }
                }
            }

            // Force update of all Glance widgets
            glanceAppWidget.updateAll(context)
        }
    }
    companion object {
        // Define a key for accessing global activity data in preferences
        val globalActivities = stringPreferencesKey("data")
    }
}