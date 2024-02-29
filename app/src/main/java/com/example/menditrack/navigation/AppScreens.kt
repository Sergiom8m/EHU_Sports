package com.example.menditrack.navigation

sealed class AppScreens(val route: String) {
    object Walking: AppScreens("walking")
    object Running: AppScreens("running")
    object Cycling: AppScreens("cycling")
    object Stats: AppScreens("stats")
    object Add: AppScreens("add")
    object Edit: AppScreens("edit")
    object ActivityView: AppScreens("activity_view")
}