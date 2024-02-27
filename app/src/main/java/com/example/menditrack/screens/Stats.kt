package com.example.menditrack.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.menditrack.AppViewModel
import com.example.menditrack.navigation.AppScreens

@Composable
fun Stats(
    innerPadding: PaddingValues,
    appViewModel: AppViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
){
    Text(text = "STATS")
}