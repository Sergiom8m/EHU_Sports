package com.example.menditrack.screens

import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.menditrack.R
import com.example.menditrack.utils.LoadingImagePlaceholder
import com.example.menditrack.utils.getLatLngFromAddress
import com.example.menditrack.viewModel.AppViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    appViewModel: AppViewModel,
    navController: NavController
){

    // Get the variable that indicates which is the activity to show
    val activityToShow = appViewModel.activityToShow.value

    val context = LocalContext.current

    val locationPermissionState = rememberPermissionState(
        permission = Manifest.permission.ACCESS_FINE_LOCATION
    )
    LaunchedEffect(true){
        if (!locationPermissionState.status.isGranted) {
            locationPermissionState.launchPermissionRequest()
        }
    }

    if (locationPermissionState.status.isGranted) {
        activityToShow?.initPoint?.let { initPoint ->
            getLatLngFromAddress(context, initPoint)?.let { (latitude, longitude) ->
                val cameraPositionState = rememberCameraPositionState {
                    position =
                        CameraPosition.fromLatLngZoom(LatLng(latitude, longitude), 10f)
                }
                GoogleMap(
                    modifier = Modifier
                        .fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(isMyLocationEnabled = true)
                ) {
                    Marker(
                        state = MarkerState(position = LatLng(latitude, longitude)),
                        title = "Location",
                        snippet = "Marker at provided address"
                    )
                }
            } ?: run {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LoadingImagePlaceholder(R.drawable.no_location, 150.dp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = stringResource(id = R.string.no_location))
                    }
                }
            }
        }
    }

    FloatingActionButton(
        onClick = { navController.navigateUp() },
        containerColor = MaterialTheme.colorScheme.primary,
        shape = RoundedCornerShape(20),
        modifier = Modifier.padding(10.dp)
    ) {
        Icon(
            Icons.Filled.ArrowBack,
            stringResource(id = R.string.add),
            tint = MaterialTheme.colorScheme.secondary
        )
    }

}

