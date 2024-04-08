package com.example.menditrack.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.menditrack.viewModel.AppViewModel
import com.example.menditrack.R
import com.example.menditrack.navigation.AppScreens
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.painterResource
import com.example.menditrack.utils.LoadingImagePlaceholder

@Composable
fun ActivityList(
    appViewModel: AppViewModel,
    navController: NavController,
    type:String,
    modifier: Modifier = Modifier.verticalScroll(rememberScrollState())
){
    val user = appViewModel.actualUser.value.username

    // Get activity list (Flow) as state
    val activities = appViewModel.getActivitiesByType(type, user).collectAsState(initial = emptyList())

    // Define the screens title depending on the selected option by the user
    var title = ""
    when (type){
        "Walking" -> title = stringResource(id = R.string.walk_routes)
        "Running" -> title = stringResource(id = R.string.run_routes)
        "Cycling" -> title = stringResource(id = R.string.cyc_routes)
    }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ){
        // Box with the screens heading
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            Text(
                text = title,
                Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary,
                letterSpacing = 0.1.em,
                lineHeight = 24.sp
            )
        }

        HorizontalDivider()

        // LazyColumn with the list of cards (ACTIVITY LIST)
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            contentPadding = PaddingValues(bottom = 70.dp)
        ) {
            // Iterate all the items in activities flow
            items(activities.value) { activity ->

                // Create a card for each activity
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        // When user clicks the card open ViewActivity screen with clicked activity data
                        .clickable {
                            navController.navigate(AppScreens.ActivityView.route)
                            appViewModel.activityToShow = mutableStateOf(activity)
                        }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .weight(1f),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = activity.name,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                            )
                            Text(
                                text = "${stringResource(id = R.string.route_dist)}: ${activity.distance.toString()} km",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }

                        Spacer(modifier = Modifier.padding(5.dp))

                        Row(
                            horizontalArrangement = Arrangement.End
                        ) {
                            // Button to edit the activity
                            IconButton(
                                onClick = {
                                    navController.navigate(AppScreens.Edit.route)
                                    appViewModel.activityToEdit = mutableStateOf(activity)
                                }
                            ) {
                                Icon(
                                    painterResource(id = R.drawable.edit),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primaryContainer
                                )
                            }

                            Spacer(modifier = Modifier.padding(5.dp))

                            // Button to delete activity
                            IconButton(
                                onClick = {
                                    // On click set visible the deletion confirm dialog
                                    appViewModel.activityToDelete = activity
                                    appViewModel.showDelete = true
                                }
                            ) {
                                Icon(
                                    painterResource(id = R.drawable.delete),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primaryContainer
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(100.dp))
}