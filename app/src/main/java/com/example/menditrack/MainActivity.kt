package com.example.menditrack

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.menditrack.navigation.AppScreens
import com.example.menditrack.screens.LoadingScreen
import com.example.menditrack.screens.Login
import com.example.menditrack.screens.MainScreen
import com.example.menditrack.screens.Register
import com.example.menditrack.ui.theme.MendiTrackTheme
import com.example.menditrack.viewModel.AppViewModel
import com.example.menditrack.viewModel.PreferencesViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // Instance ViewModels
    private val appViewModel by viewModels<AppViewModel> ()
    private val preferencesViewModel by viewModels<PreferencesViewModel> ()

    // Function to pick multimedia files from the device storage (gallery)
    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){
        if (it!=null){
            val ivImage = ImageView(this)
            ivImage.setImageURI(it)
            val drawable: Drawable = ivImage.drawable

            // Si el drawable es una instancia de BitmapDrawable, obtener el Bitmap directamente
            if (drawable is BitmapDrawable) {
                appViewModel.setProfileImage(appViewModel.actualUser.value.username, drawable.bitmap)
            }
        }
    }

    // Set a CHANNEL_ID
    companion object{
        const val CHANNEL_ID = "EHUSport_Channel"
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        // Create a notification channel
        createNotificationChannel()

        super.onCreate(savedInstanceState)

        setContent {
            preferencesViewModel.restartLang(preferencesViewModel.lang.collectAsState(initial = preferencesViewModel.currentSetLang).value)
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                // Ask for permissions
                AskPermissions()

                // Obtain and upload to remote server device unique token (for Firebase Cloud Messaging)
                deviceToken(appViewModel)

                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = AppScreens.Loading.route) {

                    composable(AppScreens.Loading.route){
                        LoadingScreen(appViewModel, navController)
                    }
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
                                pickMedia = pickMedia,
                                modifier = Modifier
                            )
                        }
                    }
                }
            }
        }
    }

    // Function to upload to remote server device unique token (for Firebase Cloud Messaging)
    private fun deviceToken(appViewModel: AppViewModel) {
        CoroutineScope(Dispatchers.IO).launch {
            val token = Firebase.messaging.token.await()
            Log.d("TOKEN", token)
            appViewModel.subscribeDevice(token)
        }
    }

    // Function to create a local notification channel
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.channel_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = getString(R.string.channel_desc)
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Function to ask all needed permissions fot the correct execution of the app
    @OptIn(ExperimentalPermissionsApi::class)
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Composable
    fun AskPermissions(){
        val permissions = arrayOf(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.POST_NOTIFICATIONS
        )
        val permissionState = rememberMultiplePermissionsState(
            permissions = permissions.toList()
        )
        LaunchedEffect(true){
            permissionState.launchMultiplePermissionRequest()
        }
    }
}