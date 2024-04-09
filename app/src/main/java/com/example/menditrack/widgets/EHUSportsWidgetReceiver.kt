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
import com.example.menditrack.model.UserRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
@AndroidEntryPoint
class EHUSportsWidgetReceiver() : GlanceAppWidgetReceiver(){

    override val glanceAppWidget: GlanceAppWidget = EHUSportsWidget()

    private val coroutineScope = MainScope()

    @Inject
    lateinit var activityRepository: ActivityRepository

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        Log.d("Widget", "onUpdate Called")
        observeData(context)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        Log.d("Widget", "onReceive Called; Action: ${intent.action}")

        // We filter actions in order to prevent updating twice in onUpdate events.
        if (intent.action == "updateAction" || intent.action.equals("ACTION_TRIGGER_LAMBDA")) {
            observeData(context)
        }
    }
    private fun observeData(context: Context) {
        coroutineScope.launch {
            Log.d("Widget", "Coroutine Called")

            val activities = activityRepository.getActivities().first().map(::CompactActivity)

            Log.d("Widget", "Coroutine - Data-Length: ${activities.size}")

            GlanceAppWidgetManager(context).getGlanceIds(EHUSportsWidget::class.java).forEach { glanceId ->
                updateAppWidgetState(context, PreferencesGlanceStateDefinition, glanceId) { widgetDataStore ->
                    widgetDataStore.toMutablePreferences().apply {

                        this[globalActivities] = Json.encodeToString(activities)

                    }
                }
            }

            // Force widget update
            glanceAppWidget.updateAll(context)
        }
    }
    companion object {
        val globalActivities = stringPreferencesKey("data")
    }



}