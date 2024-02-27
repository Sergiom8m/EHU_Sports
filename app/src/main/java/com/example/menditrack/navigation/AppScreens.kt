package com.example.menditrack.navigation

sealed class AppScreens(val route: String) {
    object Home: AppScreens("home")
    object Profile: AppScreens("profile")
    object Feed: AppScreens("feed")
}