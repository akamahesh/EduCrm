package com.crm.edu.ui.compose.screens

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.crm.edu.ui.compose.Screen

@Composable
fun HomeScreen(navController: NavHostController) {
    val context = LocalContext.current
    val activity = context as? Activity

    // Handle back press to close the app
    BackHandler {
        activity?.finish()
    }

    val homeNavController = rememberNavController()
    BottomNavigationBar(homeNavController)
}


data class BottomNavigationItem(
    val title: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasUpdate: Boolean,
    val badgeCount: Int? = null
)

fun bottomNavigationItems(): List<BottomNavigationItem> {
    return listOf(
        BottomNavigationItem(
            title = "Call Manager",
            route = Screen.CallManager.route,
            selectedIcon = Icons.Filled.Call,
            unselectedIcon = Icons.Outlined.Call,
            hasUpdate = false,
            badgeCount = 4
        ),
        BottomNavigationItem(
            title = "HRMS",
            route = Screen.Dashboard.route,
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            hasUpdate = false
        )
    )
}