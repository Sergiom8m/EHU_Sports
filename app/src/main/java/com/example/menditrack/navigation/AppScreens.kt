package com.example.menditrack.navigation

sealed class AppScreens(val route: String) {
    object Walking: AppScreens("walking")
    object Running: AppScreens("running")
    object Cycling: AppScreens("cycling")
    object Stats: AppScreens("stats")
    object Add: AppScreens("add")
    object RouteView: AppScreens("route_view")
}