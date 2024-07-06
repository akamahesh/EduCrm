package com.crm.edu.compose

sealed class Screen(val route: String) {
    object Splash : Screen("splash_screen")
    object Login : Screen("login_screen")
    object Home : Screen("home_screen")
    object Dashboard : Screen("dashboard_screen")
    object CallManager : Screen("call_manager_screen")
}
