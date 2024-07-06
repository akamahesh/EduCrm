package com.crm.edu.compose.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.crm.edu.compose.Screen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    //initialize the default selected item

    var selectedItemIndex by remember {
        mutableStateOf(0)
    }

    Scaffold(modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                bottomNavigationItems().forEachIndexed { index, bottomNavigationItem ->
                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick = {
                            selectedItemIndex = index
                            navController.navigate(bottomNavigationItem.route) {
                                navController.graph.startDestinationRoute?.let {
                                    popUpTo(it) {
                                        saveState = true
                                    }
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        label = {
                            Text(text = bottomNavigationItem.title)
                        },
                        icon = {
                            BadgedBox(badge = {
                                if (bottomNavigationItem.badgeCount != null) {
                                    Badge {
                                        Text(text = bottomNavigationItem.badgeCount.toString())
                                    }
                                } else if (bottomNavigationItem.hasUpdate) {
                                    Badge()
                                }
                            }) {
                                Icon(
                                    imageVector = if (index == selectedItemIndex) bottomNavigationItem.selectedIcon else bottomNavigationItem.unselectedIcon,
                                    contentDescription = bottomNavigationItem.title
                                )
                            }
                        })
                }
            }
        }) { paddingValues ->
        NavHost(navController = navController, startDestination = Screen.CallManager.route) {
            composable(route = Screen.CallManager.route) {
                CallManagerScreen(navController)
            }
            composable(route = Screen.Dashboard.route) {
                DashboardScreen(navController)
            }

        }

    }

}