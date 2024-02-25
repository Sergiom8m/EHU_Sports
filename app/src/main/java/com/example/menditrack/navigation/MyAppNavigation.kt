package com.example.menditrack.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.menditrack.AppViewModel
import com.example.menditrack.screens.MainScreen

@Composable
fun MyAppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    appViewModel: AppViewModel,
    LanguageChange: (String) -> Unit
) {
        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = AppScreens.MainScreen.route
        ){
            composable(route = AppScreens.MainScreen.route){
                MainScreen(navController, appViewModel, modifier, LanguageChange)
            }
        }
}