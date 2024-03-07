package com.example.menditrack

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.menditrack.screens.MainScreen
import com.example.menditrack.ui.theme.MendiTrackTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // Instance ViewModels
    private val appViewModel by viewModels<AppViewModel> ()
    val preferencesViewModel by viewModels<PreferencesViewModel> ()

    // Set a CHANNEL_ID
    companion object{
        const val CHANNEL_ID = "Menditrack_Channel"
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {

        // Create a notification channel
        createNotificationChannel()

        super.onCreate(savedInstanceState)

        setContent {
            MendiTrackTheme(preferencesViewModel) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Ask for notification permission
                    NotificationPermission()

                    // Redirect to MainScreen
                    MainScreen(
                        appViewModel = appViewModel,
                        prefViewModel = preferencesViewModel,
                        modifier = Modifier
                    )
                }
            }
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.channel_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = getString(R.string.channel_desc)
            }

            // Register the channel with the system.
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Composable
    fun NotificationPermission(){
        val permissionState = rememberPermissionState(
            permission = android.Manifest.permission.POST_NOTIFICATIONS
        )
        LaunchedEffect(true){
            permissionState.launchPermissionRequest()
        }
    }


}

