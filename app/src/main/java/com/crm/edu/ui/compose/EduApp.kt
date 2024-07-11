package com.crm.edu.ui.compose

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.crm.edu.ui.compose.screens.HomeScreen
import com.crm.edu.ui.compose.screens.LoginScreen
import com.crm.edu.ui.compose.screens.SplashScreen

@Composable
fun EduApp() {
    val navController = rememberNavController()
    EduAppNavHost(
        navController = navController
    )
}

@Composable
fun EduAppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(route = Screen.Splash.route) {
            SplashScreen(navController)
        }
        composable(route = Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(route = Screen.Home.route) {
            HomeScreen(navController)
        }

    }
}
