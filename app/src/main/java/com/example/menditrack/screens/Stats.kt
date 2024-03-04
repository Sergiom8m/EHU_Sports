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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.menditrack.AppViewModel
import com.example.menditrack.R
import com.example.menditrack.charts.PieChart
import com.example.menditrack.data.BarType
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@Composable
fun Stats(
    appViewModel: AppViewModel,
    navController: NavController,
    modifier: Modifier = Modifier.verticalScroll(rememberScrollState())
){
    val totalWalkingActivities by appViewModel.getActivitiesByType("Walking").collectAsState(initial = emptyList())
    val totalRunningActivities by appViewModel.getActivitiesByType("Running").collectAsState(initial = emptyList())
    val totalCyclingActivities by appViewModel.getActivitiesByType("Cycling").collectAsState(initial = emptyList())

    val totalDistanceWalking = totalWalkingActivities.sumByDouble { it.distance }.toInt()
    val totalDistanceRunning = totalRunningActivities.sumByDouble { it.distance }.toInt()
    val totalDistanceCycling = totalCyclingActivities.sumByDouble { it.distance }.toInt()

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
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
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {

            // Total de actividades por tipo
            Title(stringResource(id = R.string.num_activity))

            if ( totalCyclingActivities.size > 0 || totalRunningActivities.size > 0 || totalWalkingActivities.size > 0) {

                PieChart(
                    data = mapOf(
                        Pair( stringResource(id = R.string.cycling), totalCyclingActivities.size),
                        Pair( stringResource(id = R.string.running), totalRunningActivities.size),
                        Pair( stringResource(id = R.string.walking), totalWalkingActivities.size)
                    )
                )
            }
            else{
                NoData()
            }

            // Distancia total por deporte
            Title(stringResource(id = R.string.dist_activity))

            // Gráfico de barras
            val dataList = listOf(totalDistanceCycling, totalDistanceRunning, totalDistanceWalking)
            val maxValue = dataList.maxOrNull() ?: 1
            val floatValue = dataList.map { it.toFloat() / maxValue }

            if (maxValue != 0) {
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
                NoData()
            }

        }

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

