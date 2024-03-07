package com.example.menditrack.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.menditrack.AppViewModel
import androidx.compose.material3.IconButton
import androidx.compose.ui.res.stringResource
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.menditrack.R
import com.example.menditrack.model.SportActivity
import com.example.menditrack.utils.exportActivityToTxt
import com.example.menditrack.utils.mapToUserLanguageDifficulty
import com.example.menditrack.utils.openGoogleMaps
import com.example.menditrack.utils.openShare

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

    // Create a scaffold to capsule activity's data and a download button
    Scaffold (
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            if (activityToShow != null) {
                DownloadButton(activityToShow)
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
                        onClick = { openGoogleMaps(activityToShow.initPoint, context) },
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
            appViewModel.showNavBars = true
            appViewModel.showAddButton = true
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
fun DownloadButton(activity: SportActivity) {
    ExtendedFloatingActionButton(
        onClick = { exportActivityToTxt(activity)},
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





