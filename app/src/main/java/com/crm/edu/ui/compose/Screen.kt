package com.crm.edu.ui.compose

sealed class Screen(val route: String) {
    data object Splash : Screen("splash_screen")
    data object Login : Screen("login_screen")
    data object Home : Screen("home_screen")
    data object Dashboard : Screen("dashboard_screen")
    data object CallManager : Screen("call_manager_screen")
    data object Attendance : Screen("attendance_screen")
    data object LeaveRequest : Screen("leave_request_screen")
    data object HolidayCalendar : Screen("holiday_calendar_screen")
    data object Leaves : Screen("leaves_screen")
    data object Calendar : Screen("calendar_screen")
}
