package com.example.menditrack.screens

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.menditrack.R
import com.example.menditrack.navigation.AppScreens
import com.example.menditrack.utils.FlickeringImage
import com.example.menditrack.viewModel.AppViewModel
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// Function to render a login page before starting the app
@Composable
fun LoadingScreen(
    appViewModel: AppViewModel,
    navController: NavHostController
) {
    // Check internet connectivity before downloading data from remote
    if (checkInternetConnectivity()) {
        // Obtain and upload to remote server device unique token (for Firebase Cloud Messaging)
        deviceToken(appViewModel)
        val loadCompleted = appViewModel.correctInit
        // Check if data has been downloaded correctly and redirect user to login page
        if (loadCompleted) {
            navController.navigate(AppScreens.Login.route)
        } else {
            // While data is being downloaded show loading page
            LoadingView()
        }
        appViewModel.downloadData()
    } else {
        // If no internet connection show an alert view
        NoInternetView(navController = navController)
    }
}

@Composable
fun LoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FlickeringImage(R.drawable.downloading, 150.dp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = stringResource(id = R.string.loading))
        }
        Surface(
            color = MaterialTheme.colors.primary.copy(alpha = 0.6f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
                .clip(CircleShape)
        ) {
            Text(
                text = stringResource(id = R.string.timeout),
                color = MaterialTheme.colors.onPrimary,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}


@Composable
fun NoInternetView(navController: NavHostController) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FlickeringImage(R.drawable.no_wifi, 150.dp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.no_internet),
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    navController.popBackStack()
                    navController.navigate(AppScreens.Loading.route)
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(text = stringResource(id = R.string.reload))
            }
        }
    }
}

// Function to validate user's connectivity
@SuppressLint("ServiceCast")
@Composable
fun checkInternetConnectivity(): Boolean {
    val context = LocalContext.current
    val connectivityManager = remember(context) {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}

// Function to upload to remote server device unique token (for Firebase Cloud Messaging)
private fun deviceToken(appViewModel: AppViewModel) {
    CoroutineScope(Dispatchers.IO).launch {
        val token = Firebase.messaging.token.await()
        Log.d("TOKEN", token)
        appViewModel.subscribeDevice(token)
    }
}
