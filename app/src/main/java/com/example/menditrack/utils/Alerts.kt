package com.example.menditrack.utils

import android.app.NotificationManager
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import com.example.menditrack.viewModel.AppViewModel
import com.example.menditrack.MainActivity
import com.example.menditrack.R
import com.example.menditrack.data.Language
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Dialog to select the language (user preference)
@Composable
fun SettingsDialog(showSettings: Boolean, onLanguageChange: (Language)-> Unit, onConfirm: () -> Unit) {
    if (showSettings) {
        AlertDialog(
            onDismissRequest = { onConfirm() },
            confirmButton = { },
            title = { Text(text = stringResource(id = R.string.settings)) },
            text = {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    for (language in Language.entries){
                        Button(
                            onClick = {
                                onConfirm()
                                onLanguageChange(Language.getFromCode(language.code))
                            },
                            Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = language.type,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            }
        )
    }
}

// Dialog to show the information of the app (with a "contact us" button)
@Composable
fun InfoDialog(showInfo: Boolean, context: Context, onConfirm: () -> Unit) {
    if (showInfo) {
        AlertDialog(
            onDismissRequest = { onConfirm() },
            confirmButton = {
                Row {
                    TextButton(onClick = { openEmail(context) }) {
                        Text(text = stringResource(R.string.contact_us))
                    }
                }
            },
            title = { Text(text = stringResource(id = R.string.app_name)) },
            text = {
                Text(text = stringResource(id = R.string.app_desc))
            }
        )
    }
}

// Dialog to select the theme (user preference)
@Composable
fun ShowThemes(showThemes: Boolean, onThemeChange: (Int) -> Unit, onConfirm: () -> Unit) {
    if (showThemes) {
        AlertDialog(
            onDismissRequest = { onConfirm() },
            confirmButton = { },
            title = { Text(text = stringResource(id = R.string.themeAlert)) },
            containerColor = Color.White,
            text = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    val themes = listOf(
                        Pair(R.drawable.primary_palette, "Theme 1"),
                        Pair(R.drawable.secondary_palette, "Theme 2"),
                        Pair(R.drawable.tertiary_palette, "Theme 3")
                    )
                    // Create a button fo each theme
                    themes.forEachIndexed { index, (image, _) ->
                        Image(
                            painter = painterResource(id = image),
                            contentDescription = null,
                            modifier = Modifier
                                .size(55.dp)
                                .clickable(onClick = {
                                    onConfirm()
                                    onThemeChange(index)
                                }))
                    }
                }
            }
        )
    }
}

// Dialog to confirm activity deletion
@Composable
fun ShowDeleteMessage(
    showDelete: Boolean,
    appViewModel: AppViewModel,
    onConfirm: () -> Unit
) {
    if (showDelete) {
        AlertDialog(
            onDismissRequest = { },
            confirmButton = {
                TextButton(onClick = {
                    CoroutineScope(Dispatchers.Main).launch {
                        appViewModel.deleteActivity(appViewModel.activityToDelete!!)
                    }
                    onConfirm()
                }) {
                    Text(text = stringResource(id = R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    onConfirm()
                }) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            },
            title = {
                Text(text = stringResource(id = R.string.delete))
            },
            text = {
                Text(text = stringResource(id = R.string.delete_warn))
            }
        )
    }
}

// Function to throw a notification given the title, the content of the notification and the context
fun sendNotification(context: Context, title: String, content: String, icon: Int) {

    val notificationManager = context.getSystemService(NotificationManager::class.java)

    val notification = NotificationCompat.Builder(context, MainActivity.CHANNEL_ID)
        .setContentTitle(title)
        .setContentText(content)
        .setAutoCancel(true)
        .setSmallIcon(icon)
        .setStyle(
            NotificationCompat.BigTextStyle()
            .bigText(content))
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .build()

    notificationManager.notify(1, notification)
}

