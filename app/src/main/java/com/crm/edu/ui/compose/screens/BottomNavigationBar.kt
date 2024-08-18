package com.crm.edu.ui.compose.screens

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
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
import com.crm.edu.ui.compose.screens.calendarv2.AttendanceScreenWithCalendarView
import com.crm.edu.ui.compose.screens.dashboard.DashboardScreen
import com.crm.edu.ui.compose.screens.holidayLeaves.HolidayCalendarScreen
import com.crm.edu.ui.compose.screens.leaveRequest.LeaveRequestScreen
import com.crm.edu.ui.compose.screens.leaves.LeavesScreen
import com.crm.edu.ui.compose.screens.markAttendance.MarkAttendanceScreen
import com.crm.edu.ui.compose.screens.myteam.MyTeamScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    //initialize the default selected item

    val context: Context = LocalContext.current
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
        NavHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.navigationBars.asPaddingValues()),
            navController = navController,
            startDestination = Screen.CallManager.route
        ) {
            composable(route = Screen.CallManager.route) {
                CallManagerScreen(navController)
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
                LeaveRequestScreen(navController) {
                    navController.navigateUp()
                }
            }
            composable(route = Screen.HolidayCalendar.route) {
                HolidayCalendarScreen(navController, onUpClick = {
                    navController.navigateUp()
                })
            }
            composable(route = Screen.Leaves.route) {
                LeavesScreen(
                    navController,
                    onUpClick = { navController.navigateUp() },
                    onToast = {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    })
            }

            composable(route = Screen.Calendar.route) {
                CalendarScreen(navController) {
                    navController.navigateUp()
                }
            }
            composable(route = Screen.MarkAttendance.route) {
                MarkAttendanceScreen(navController, onUpClick = {
                    navController.navigateUp()
                })
            }
            composable(route = Screen.MyTeam.route) {
                MyTeamScreen(
                    navController = navController,
                    onUpClick = { navController.navigateUp() },
                    onTeamMemberSelected = {
                        val route = Screen.CalendarV2.route.replace("{staffId}", it)
                        navController.navigate(route = route)
                    })
            }
            composable(route = Screen.CalendarV2.route, arguments = listOf(
                navArgument("staffId") {
                    defaultValue = "default"
                    type = NavType.StringType
                }
            )) { navBackStackEntry ->
                /* Extracting the id from the route */
                val staffId = navBackStackEntry.arguments?.getString("staffId")
                AttendanceScreenWithCalendarView(staffId = staffId, navController = navController) {
                    navController.navigateUp()
                }
            }

        }

    }

}