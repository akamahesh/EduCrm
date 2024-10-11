package com.crm.edu.ui.compose.screens.home

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.crm.edu.ui.compose.Screen

@Composable
fun HomeScreen(navController: NavHostController, viewModel: HomeViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val activity = context as? Activity

    val state by viewModel.uiState.collectAsState()

    // Handle back press to close the app
    BackHandler {
        activity?.finish()
    }

    val homeNavController = rememberNavController()
    val homeBottomData = (state as? UIState.Success)?.homeBottomData
    BottomNavigationBar(
        homeNavController,
        bottomNavigationItems(homeBottomData),
        homeBottomData?.showCallManager == true
    ) {
        navController.navigate(Screen.Login.route) {
            popUpTo(Screen.Splash.route) {
                inclusive = true
            }
        }
    }
}


data class BottomNavigationItem(
    val title: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasUpdate: Boolean,
    val badgeCount: Int? = null
)

private fun bottomNavigationItems(homeData: HomeBottomData?): List<BottomNavigationItem> {
    val bottomNavigationList = mutableListOf<BottomNavigationItem>()
    if (homeData?.showCallManager == true) {
        bottomNavigationList.add(
            BottomNavigationItem(
                title = "Call Manager",
                route = Screen.CallManager.route,
                selectedIcon = Icons.Filled.Call,
                unselectedIcon = Icons.Outlined.Call,
                hasUpdate = false,
            )
        )
    }
    bottomNavigationList.add(
        BottomNavigationItem(
            title = "HRMS",
            route = Screen.Dashboard.route,
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            hasUpdate = false
        )
    )
    return bottomNavigationList
}