package com.crm.edu.ui.compose.screens.calendar.utils

import java.util.Calendar
import java.util.Date


fun getFirstDayOfMonth(month: Int, year: Int): Int {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, year)
    calendar.set(Calendar.MONTH, month)
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    return calendar.get(Calendar.DAY_OF_WEEK) - 1 // To start from Sunday (0)
}

fun getMonthNumber(month: String): Int {
    return when (month) {
        "January" -> Calendar.JANUARY
        "February" -> Calendar.FEBRUARY
        "March" -> Calendar.MARCH
        "April" -> Calendar.APRIL
        "May" -> Calendar.MAY
        "June" -> Calendar.JUNE
        "July" -> Calendar.JULY
        "August" -> Calendar.AUGUST
        "September" -> Calendar.SEPTEMBER
        "October" -> Calendar.OCTOBER
        "November" -> Calendar.NOVEMBER
        "December" -> Calendar.DECEMBER
        else -> Calendar.JANUARY
    }
}

fun getDate(year: Int, month: Int, day: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.set(year, month, day)
    return calendar.time
}

fun getCurrentMonthYear(): Pair<Int, Int> {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    return Pair(month, year)
}

fun getDaysInMonth(month: Int, year: Int): List<Int> {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.MONTH, month)
    calendar.set(Calendar.YEAR, year)
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    return (1..daysInMonth).toList()
}

fun getDayOfWeek(day: Int, month: Int, year: Int): Int {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, year)
    calendar.set(Calendar.MONTH, month)
    calendar.set(Calendar.DAY_OF_MONTH, day)
    return calendar.get(Calendar.DAY_OF_WEEK)
}