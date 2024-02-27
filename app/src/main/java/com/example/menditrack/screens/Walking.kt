package com.example.menditrack.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.menditrack.AppViewModel

@Composable
fun Walking(
    appViewModel: AppViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
){
    Text(text = "CAMINATA")
}