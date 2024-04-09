package com.example.menditrack.widgets

import androidx.glance.GlanceTheme
import androidx.glance.appwidget.provideContent
import androidx.glance.currentState
import androidx.glance.text.Text
import com.example.menditrack.widgets.EHUSportsWidgetReceiver.Companion.globalActivities
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.ActionParameters
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import com.example.menditrack.R
import com.example.menditrack.data.CompactActivity
import kotlinx.serialization.json.Json


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

        val prefs = currentState<Preferences>()

        val data: String? = prefs[globalActivities]

        val activities: List<CompactActivity> = if (data != null) Json.decodeFromString(data) else emptyList()

        Column(
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
            modifier = GlanceModifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(16.dp)
        ) {

            /*------------------------------------------------
            |                     Header                     |
            ------------------------------------------------*/

            Text(
                text = "HOLA",
                modifier = GlanceModifier.fillMaxWidth().padding(bottom = 16.dp),
                style = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                ),
                maxLines = 1
            )


            /*------------------------------------------------
            |               Body (Visit List)                |
            ------------------------------------------------*/

            when {

                // LogIn pero sin gastos
                activities.isEmpty() -> {
                    Column(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = GlanceModifier.fillMaxSize().defaultWeight()
                    ) {
                        Text(text = "HOLA")
                    }
                }

                // Dena ondo
                else -> {
                    LazyColumn(modifier = GlanceModifier.fillMaxSize().defaultWeight()) {
                        items(activities, itemId = { it.hashCode().toLong() }) { item ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = GlanceModifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {

                                /*------------------------------------------------
                                |                   Visit Data                   |
                                ------------------------------------------------*/

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = GlanceModifier.fillMaxWidth().defaultWeight()
                                ) {

                                    //------------------   Hour   ------------------//



                                    Spacer(GlanceModifier.width(16.dp))


                                    //----------   Client Data and Town   ----------//

                                    Column {
                                        Text(text = item.name, modifier = GlanceModifier.defaultWeight())
                                        Text(text = item.distance.toString(), modifier = GlanceModifier.defaultWeight())
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(GlanceModifier.height(8.dp))

            Image(
                provider = ImageProvider(R.drawable.reload),
                contentDescription = null,
                modifier = GlanceModifier.size(24.dp).clickable{actionRunCallback<WidgetRefresh>()}
            )
        }
    }

    private class WidgetRefresh : ActionCallback {
        override suspend fun onAction(
            context: Context,
            glanceId: GlanceId,
            parameters: ActionParameters
        ) {
            EHUSportsWidget().update(context,glanceId)
        }
    }


}