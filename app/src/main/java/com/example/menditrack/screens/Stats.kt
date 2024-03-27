package com.example.menditrack.screens

import BarGraph
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.menditrack.viewModel.AppViewModel
import com.example.menditrack.R
import com.example.menditrack.charts.PieChart
import com.example.menditrack.data.BarType
import com.example.menditrack.utils.sumActivityDistances

@Composable
fun Stats(
    appViewModel: AppViewModel,
    modifier: Modifier = Modifier.verticalScroll(rememberScrollState())
){
    val user = appViewModel.actualUser.value.username

    // Get as states the lists of activities of each type
    val walkingActivities by appViewModel.getActivitiesByType("Walking", user).collectAsState(initial = emptyList())
    val runningActivities by appViewModel.getActivitiesByType("Running", user).collectAsState(initial = emptyList())
    val cyclingActivities by appViewModel.getActivitiesByType("Cycling", user).collectAsState(initial = emptyList())

    // Calculate the sum of distance of each activity type
    val totalDistanceWalking = sumActivityDistances(walkingActivities)
    val totalDistanceRunning = sumActivityDistances(runningActivities)
    val totalDistanceCycling = sumActivityDistances(cyclingActivities)

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){

        StatsHeading()

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {

            // Section to visualize the total number of activities for each activity type
            Title(stringResource(id = R.string.num_activity))

            // Create icons for each activity type
            val walkingIcon = painterResource(id = R.drawable.walk)
            val runningIcon = painterResource(id = R.drawable.run)
            val cyclingIcon = painterResource(id = R.drawable.bicycle)

            // Check if there is any activity
            if ( cyclingActivities.isNotEmpty() || runningActivities.isNotEmpty() || walkingActivities.isNotEmpty()) {

                // If activities exist on the DB create a pie chart showing the count of activities of each type
                PieChart(
                    data = mapOf(
                        Pair(cyclingIcon, cyclingActivities.size),
                        Pair(runningIcon, runningActivities.size),
                        Pair(walkingIcon, walkingActivities.size)
                    )
                )
            }
            else{
                // If no activities on the DB show a NO DATA message
                NoData()
            }

            // Section to visualize the total accumulated distance for each activity type
            Title(stringResource(id = R.string.dist_activity))

            // Modify the data to make it compatible with bar chart
            val dataList = listOf(totalDistanceCycling, totalDistanceRunning, totalDistanceWalking)
            val maxValue = dataList.maxOrNull() ?: 1 // Search for the max value
            val floatValue = dataList.map { it.toFloat() / maxValue } // Rescale the values according to the mean

            if (maxValue != 0) {

                // If activities exist on the DB create a bar chart showing the total distance of each activity type
                BarGraph(
                    graphBarData = floatValue,
                    xAxisScaleData = listOf(
                        stringResource(id = R.string.cycling),
                        stringResource(id = R.string.running),
                        stringResource(id = R.string.walking)
                    ),
                    barData_ = dataList,
                    height = 200.dp,
                    roundType = BarType.TOP_CURVED,
                    barWidth = 20.dp,
                    barColor = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondaryContainer,
                        MaterialTheme.colorScheme.tertiaryContainer
                    ),
                    barArrangement = Arrangement.SpaceEvenly
                )
            }
            else{
                // If no activities on the DB show a NO DATA message
                NoData()
            }
        }
    }
}

@Composable
fun StatsHeading(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Text(
            text = stringResource(id = R.string.your_stats),
            Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary,
            letterSpacing = 0.1.em,
            lineHeight = 24.sp
        )
    }
}

@Composable
fun NoData(){
    Column (
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = stringResource(id = R.string.noData),
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .align(Alignment.CenterHorizontally),
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
fun Title(boldText: String) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)),
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.primaryContainer

    ) {
        Row(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = boldText,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 8.dp),
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

