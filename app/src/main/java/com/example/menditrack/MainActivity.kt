package com.example.menditrack

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
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
import java.util.Locale


class MainActivity : ComponentActivity() {

    // Instanciar el ViewModel
    val appViewModel by viewModels<AppViewModel>()

    // Establecer un CHANNEL_ID para el canal de notificaicones
    companion object{
        const val CHANNEL_ID = "Menditrack_Channel"
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        createNotificationChannel()
        super.onCreate(savedInstanceState)

        setContent {
            MendiTrackTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NotificationPermission()
                    MainScreen(
                        appViewModel = appViewModel,
                        modifier = Modifier,
                        languageChange = { code: String -> languageChange(code)}
                    )
                }
            }
        }
    }

    private fun languageChange(code: String){
        resources.configuration.setLocale(Locale(code))
        resources.updateConfiguration(resources.configuration, resources.displayMetrics)

        resources.configuration.locale = Locale(code)
        resources.updateConfiguration(resources.configuration, resources.displayMetrics)
        Log.d("Menditrack", "Language changed")
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

