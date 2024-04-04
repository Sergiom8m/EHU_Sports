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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.menditrack.navigation.AppScreens
import com.example.menditrack.screens.Login
import com.example.menditrack.screens.MainScreen
import com.example.menditrack.screens.Register
import com.example.menditrack.ui.theme.MendiTrackTheme
import com.example.menditrack.viewModel.AppViewModel
import com.example.menditrack.viewModel.PreferencesViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // Instance ViewModels
    private val appViewModel by viewModels<AppViewModel> ()
    private val preferencesViewModel by viewModels<PreferencesViewModel> ()

    // Set a CHANNEL_ID
    companion object{
        const val CHANNEL_ID = "EHUSport_Channel"
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        // Create a notification channel
        createNotificationChannel()

        CoroutineScope(Dispatchers.Main).launch {
            appViewModel.addUsersFromRemote()
        }

        super.onCreate(savedInstanceState)

        setContent {
            preferencesViewModel.restartLang(preferencesViewModel.lang.collectAsState(initial = preferencesViewModel.currentSetLang).value)
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                // Ask for notification permission
                NotificationPermission()

                // Ask for storage permission
                StoragePermission()

                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = AppScreens.Login.route) {

                    composable(AppScreens.Login.route) {
                        Login(appViewModel, navController)
                    }
                    composable(AppScreens.Register.route) {
                        Register(appViewModel, navController)
                    }
                    composable(AppScreens.UserScreen.route) {
                        MendiTrackTheme(preferencesViewModel) {
                            MainScreen(
                                appViewModel = appViewModel,
                                prefViewModel = preferencesViewModel,
                                modifier = Modifier
                            )
                        }
                    }
                }
            }
        }
    }


    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun StoragePermission(){
        val permissionState2 = rememberPermissionState(
            permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        LaunchedEffect(true){
            permissionState2.launchPermissionRequest()
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

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun LocationPermission(){
        val locationPermissionState = rememberPermissionState(
            permission = android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        LaunchedEffect(true){
            if (!locationPermissionState.status.isGranted) {
                locationPermissionState.launchPermissionRequest()
            }
        }
    }



}

