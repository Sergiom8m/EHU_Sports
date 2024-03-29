package com.example.menditrack.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.menditrack.viewModel.AppViewModel
import androidx.compose.material3.IconButton
import androidx.compose.ui.res.stringResource
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.menditrack.R
import com.example.menditrack.data.SportActivity
import com.example.menditrack.navigation.AppScreens
import com.example.menditrack.utils.exportActivityToTxt
import com.example.menditrack.utils.mapToUserLanguageDifficulty
import com.example.menditrack.utils.openGoogleMaps
import com.example.menditrack.utils.openShare
import com.example.menditrack.utils.sendNotification
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
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ViewActivity(
    appViewModel: AppViewModel,
    navController: NavController,
    modifier: Modifier = Modifier.verticalScroll(rememberScrollState())
){
    // Get the variable that indicates which is the activity to show
    val activityToShow = appViewModel.activityToShow.value

    val context = LocalContext.current

    val locationPermissionState = rememberPermissionState(
        permission = Manifest.permission.ACCESS_FINE_LOCATION
    )

    // Create a scaffold to capsule activity's data and a download button
    Scaffold (
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            if (activityToShow != null) {
                DownloadButton(activityToShow, context)
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ){
        Column(
            modifier = modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(7.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = { navController.navigateUp() }
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = activityToShow!!.name,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = { openShare(activityToShow,context) },
                    ) {
                        Icon(
                            Icons.Default.Share,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                    IconButton(
                        onClick = { navController.navigate(AppScreens.Map.route) },
                    ) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }

                }
            }

            Divider()

            InfoRow("${stringResource(id = R.string.distance)}:", "${activityToShow?.distance ?: 0} km")
            InfoRow("${stringResource(id = R.string.start_point)}:",activityToShow?.initPoint ?: "")
            InfoRow("${stringResource(id = R.string.grade)}:", "${activityToShow?.grade ?: 0} m")
            InfoRow("${stringResource(id = R.string.difficulty)}:", mapToUserLanguageDifficulty(activityToShow?.difficulty ?: ""))


        }
    }

    // On entering this screen nav bars and add floating button should be hidden
    DisposableEffect(Unit) {
        appViewModel.showNavBars = false
        appViewModel.showAddButton = false

        // On exiting this screen make them visible again
        onDispose {
            val currentRoute = navController.currentBackStackEntry?.destination?.route
            if (currentRoute != AppScreens.Map.route) {
                appViewModel.showNavBars = true
                appViewModel.showAddButton = true
            }
        }
    }

}

@Composable
fun InfoRow(boldText: String, normalText: String) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = boldText,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = normalText,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

// Button to export activity to downloads file in txt
@Composable
fun DownloadButton(activity: SportActivity, context: Context) {
    ExtendedFloatingActionButton(
        onClick = {
            exportActivityToTxt(activity)
            sendNotification(context, activity.name, "", R.drawable.download)},
        icon = {
            Icon(
                painterResource(id = R.drawable.download),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary
            )
        },
        text = {
            Text(
                text = stringResource(id = R.string.download),
                color = MaterialTheme.colorScheme.secondary
            )
        }
    )
}

fun getLatLngFromAddress(context: Context, mAddress: String): Pair<Double, Double>? {
    val coder = Geocoder(context)
    try {
        val addressList = coder.getFromLocationName(mAddress, 1)
        if (addressList.isNullOrEmpty()) {
            return null
        }
        val location = addressList[0]
        return Pair(location.latitude, location.longitude)
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

