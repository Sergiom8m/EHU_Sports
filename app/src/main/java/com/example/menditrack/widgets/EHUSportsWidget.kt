package com.example.menditrack.widgets

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.glance.GlanceId
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.text.Text
import com.example.menditrack.screens.Stats
import dagger.hilt.android.AndroidEntryPoint


class EHUSportsWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme {
                Content()
            }
        }
    }

    @Composable
    fun Content() {

    }

}

@AndroidEntryPoint
class EHUSportsWidgetReceiver() : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = EHUSportsWidget()
}