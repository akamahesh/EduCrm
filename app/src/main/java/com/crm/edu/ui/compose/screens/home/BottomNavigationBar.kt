package com.crm.edu.ui.compose.screens.home

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.crm.edu.ui.compose.Screen
import com.crm.edu.ui.compose.screens.attendance.AttendanceScreen
import com.crm.edu.ui.compose.screens.calendar.CalendarScreen
import com.crm.edu.ui.compose.screens.calllogs.CallLogsScreen
import com.crm.edu.ui.compose.screens.calendar.tryouts.NewCalendarScreen
import com.crm.edu.ui.compose.screens.dashboard.DashboardScreen
import com.crm.edu.ui.compose.screens.holidayLeaves.HolidayCalendarScreen
import com.crm.edu.ui.compose.screens.leaveRequest.LeaveRequestScreen
import com.crm.edu.ui.compose.screens.leaves.LeavesOptionScreen
import com.crm.edu.ui.compose.screens.leaves.LeavesScreen
import com.crm.edu.ui.compose.screens.markAttendance.MarkAttendanceScreen
import com.crm.edu.ui.compose.screens.myteam.MyTeamScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    bottomNavigationItems: List<BottomNavigationItem>,
    showCallManager: Boolean,
    moveToLogin: () -> Unit
) {
    //initialize the default selected item

    val context: Context = LocalContext.current
    var selectedItemIndex by remember {
        mutableStateOf(0)
    }

    Scaffold(modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (bottomNavigationItems.size > 1) {
                NavigationBar {
                    bottomNavigationItems.forEachIndexed { index, bottomNavigationItem ->
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
            }
        }) { paddingValues ->
        NavHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.navigationBars.asPaddingValues()),
            navController = navController,
            startDestination = if (showCallManager) Screen.CallManager.route else Screen.Dashboard.route
        ) {
            composable(route = Screen.CallManager.route) {
                CallLogsScreen(navController) {
                    moveToLogin()
                }
            }

            composable(route = Screen.Dashboard.route) {
                DashboardScreen(navController, onOptionClick = { route ->
                    navController.navigate(route = route)
                }, onUpClick = {
                    navController.navigateUp()
                })
            }

            composable(route = Screen.Attendance.route) {
                AttendanceScreen(navController, onOptionClick = { route ->
                    navController.navigate(route = route)
                }, onUpClick = {
                    navController.navigateUp()
                })
            }

            composable(route = Screen.LeaveRequest.route) {
                LeaveRequestScreen {
                    navController.navigateUp()
                }
            }

            composable(route = Screen.HolidayCalendar.route) {
                HolidayCalendarScreen(navController, onUpClick = {
                    navController.navigateUp()
                })
            }

            composable(route = Screen.LeavesOptions.route) {
                LeavesOptionScreen(
                    navController,
                    onOptionClick = { route ->
                        navController.navigate(route = route)
                    },
                    onUpClick = { navController.navigateUp() }
                )
            }
            composable(route = Screen.Leaves.route, arguments = listOf(
                navArgument("teamStatus") {
                    defaultValue = ""
                    type = NavType.StringType
                }
            )) { navBackStackEntry ->
                val teamStatus = navBackStackEntry.arguments?.getString("teamStatus").orEmpty()
                LeavesScreen(
                    teamStatus,
                    onUpClick = { navController.navigateUp() },
                )
            }

            composable(
                route = Screen.Calendar.route, arguments = listOf(
                    navArgument("staffId") {
                        defaultValue = ""
                        type = NavType.StringType
                    },
                    navArgument("staffName") {
                        defaultValue = ""
                        type = NavType.StringType
                    },
                )
            ) { navBackStackEntry ->
                /* Extracting the id from the route */
                val staffId = navBackStackEntry.arguments?.getString("staffId")
                val staffName = navBackStackEntry.arguments?.getString("staffName")
                NewCalendarScreen(staffId, staffName, navController) {
                    navController.navigateUp()
                }
            }
            composable(route = Screen.MarkAttendance.route) {
                MarkAttendanceScreen(onUpClick = {
                    navController.navigateUp()
                })
            }
            composable(route = Screen.MyTeam.route) {
                MyTeamScreen(
                    navController = navController,
                    onUpClick = { navController.navigateUp() },
                    onTeamMemberSelected = { staffId, staffName ->
                        val route = Screen.Calendar.route.replace("{staffId}", staffId)
                            .replace("{staffName}", staffName)
                        navController.navigate(route = route)
                    })
            }

            composable(route = Screen.Sample.route) {
                CalendarScreen(navController) {
                    navController.navigateUp()
                }
            }

        }

    }

}