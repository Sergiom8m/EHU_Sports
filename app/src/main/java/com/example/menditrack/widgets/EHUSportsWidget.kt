package com.example.menditrack.widgets

import androidx.glance.appwidget.provideContent
import androidx.glance.currentState
import androidx.glance.text.Text
import com.example.menditrack.widgets.EHUSportsWidgetReceiver.Companion.globalActivities
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.glance.ColorFilter
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
import androidx.glance.unit.ColorProvider
import com.example.menditrack.R
import com.example.menditrack.data.CompactActivity
import kotlinx.serialization.json.Json

// Define EHUSportsWidget as a subclass of GlanceAppWidget
class EHUSportsWidget : GlanceAppWidget() {
    // Override function to provide the glance content
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        // Provide content using the Content composable function
        provideContent {
            Content(context)
        }
    }

    // Composable function to define the UI content of the widget
    @Composable
    fun Content(context: Context) {

        // Define colors for UI elements
        val backgroundColor = Color(0xFFFFFFFF) // Background color for the widget
        val headerColor = Color(0xFFFF5722) // Background color for the header
        val primaryText = Color(0xFFFFFFFF) // Text color
        val secondaryText = Color(0xFF000000) // Text color

        // Retrieve preferences state
        val prefs = currentState<Preferences>()

        // Retrieve stored activity data from preferences
        val data: String? = prefs[globalActivities]

        // Decode stored JSON data into a list of CompactActivity objects
        val activities: List<CompactActivity> = if (data != null) Json.decodeFromString(data) else emptyList()

        // Define the layout of the widget using Jetpack Compose
        Column(
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
            modifier = GlanceModifier
                .fillMaxSize()
                .background(color = backgroundColor)
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = context.getString(R.string.widget_title),
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(headerColor),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    textAlign = TextAlign.Center,
                    color = ColorProvider(primaryText)
                ),
                maxLines = 1
            )

            // Display different content based on whether activities are available or not
            when {
                // Display message for empty widget
                activities.isEmpty() -> {
                    Column(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = GlanceModifier.fillMaxSize().defaultWeight()
                    ) {
                        Image(
                            provider = ImageProvider(R.drawable.empty),
                            contentDescription = null,
                            modifier = GlanceModifier.size(60.dp)
                        )
                        Spacer(GlanceModifier.height(8.dp))
                        Text(
                            text = context.getString(R.string.empty_widget),
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center,
                                color = ColorProvider(secondaryText)
                            ),
                            maxLines = 1
                        )
                    }
                }
                // Display list of activities
                else -> {
                    LazyColumn(
                        modifier = GlanceModifier
                            .fillMaxSize()
                            .defaultWeight()
                            .padding(top = 10.dp)
                    ) {
                        items(activities, itemId = { it.hashCode().toLong() }) { item ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = GlanceModifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Spacer(GlanceModifier.width(16.dp))
                                Column {
                                    Text(
                                        text = item.name,
                                        modifier = GlanceModifier.defaultWeight(),
                                        style = TextStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp,
                                            textAlign = TextAlign.Start,
                                            color = ColorProvider(secondaryText)
                                        )
                                    )
                                    Row (
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalAlignment = Alignment.Start,
                                        modifier = GlanceModifier
                                            .fillMaxWidth()
                                            .defaultWeight()
                                            .padding(top = 4.dp)
                                    ){
                                        Image(
                                            provider = ImageProvider(R.drawable.user),
                                            contentDescription = null,
                                            modifier = GlanceModifier
                                                .size(20.dp)
                                        )
                                        Spacer(GlanceModifier.width(3.dp))
                                        Text(
                                            text = item.userId,
                                            style = TextStyle(
                                                fontWeight = FontWeight.Normal,
                                                fontSize = 15.sp,
                                                textAlign = TextAlign.Start,
                                                color = ColorProvider(secondaryText)
                                            )
                                        )
                                        Spacer(GlanceModifier.width(15.dp))
                                        Image(
                                            provider = ImageProvider(R.drawable.location),
                                            contentDescription = null,
                                            modifier = GlanceModifier.size(14.dp)
                                        )
                                        Spacer(GlanceModifier.width(3.dp))
                                        Text(
                                            text = item.initPoint,
                                            style = TextStyle(
                                                fontWeight = FontWeight.Normal,
                                                fontSize = 15.sp,
                                                textAlign = TextAlign.Start,
                                                color = ColorProvider(secondaryText)
                                            )
                                        )
                                        Spacer(GlanceModifier.width(15.dp))
                                        when(item.type){
                                            "Cycling" -> {
                                                Image(
                                                    provider = ImageProvider(R.drawable.bicycle),
                                                    contentDescription = null,
                                                    modifier = GlanceModifier.size(20.dp),
                                                    colorFilter = ColorFilter.tint(ColorProvider(headerColor))
                                                )
                                            }
                                            "Running" -> {
                                                Image(
                                                    provider = ImageProvider(R.drawable.run),
                                                    contentDescription = null,
                                                    modifier = GlanceModifier.size(20.dp),
                                                    colorFilter = ColorFilter.tint(ColorProvider(headerColor))
                                                )
                                            }
                                            "Walking" -> {
                                                Image(
                                                    provider = ImageProvider(R.drawable.walk),
                                                    contentDescription = null,
                                                    modifier = GlanceModifier.size(20.dp),
                                                    colorFilter = ColorFilter.tint(ColorProvider(headerColor))
                                                )
                                            }
                                        }
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
                modifier = GlanceModifier.size(30.dp).clickable{actionRunCallback<WidgetRefresh>()}
            )
        }
    }

    // Define a class to handle widget refresh action
    private class WidgetRefresh : ActionCallback {
        override suspend fun onAction(
            context: Context,
            glanceId: GlanceId,
            parameters: ActionParameters
        ) {
            // Trigger widget update
            EHUSportsWidget().update(context,glanceId)
        }
    }

    // Function to manually refresh the widget content
    fun refresh(context:Context){
        actionRunCallback<WidgetRefresh>()
        // Send a broadcast to trigger widget update
        val intent = Intent(context, EHUSportsWidgetReceiver::class.java).apply {
            action = "updateAction"
        }
        context.sendBroadcast(intent)
    }
}