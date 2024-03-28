package com.example.menditrack.navigation

// Class to store the different objects for each screen of the app (each object distinguished by a route)
sealed class AppScreens(val route: String) {
    data object Walking: AppScreens("walking")
    data object Running: AppScreens("running")
    data object Cycling: AppScreens("cycling")
    data object Stats: AppScreens("stats")
    data object Add: AppScreens("add")
    data object Edit: AppScreens("edit")
    data object ActivityView: AppScreens("activity_view")
    data object Login: AppScreens("login")
    data object Register: AppScreens("register")
    data object UserScreen: AppScreens("user_screen")
    data object Map: AppScreens("map")
}