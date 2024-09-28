package com.crm.edu.ui.compose.screens.calendar.tryouts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.crm.edu.R
import com.crm.edu.data.myteam.StaffAttendanceData
import com.crm.edu.ui.compose.screens.attendance.AttendanceStatus
import com.crm.edu.ui.compose.screens.calendar.utils.getCurrentMonthYear
import com.crm.edu.ui.compose.screens.calendar.utils.getDayOfWeek
import com.crm.edu.ui.compose.screens.calendar.utils.getDaysInMonth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun NewCalendarScreen(
    staffId: String? = null,
    staffName: String? = null,
    navController: NavHostController,
    viewModel: NewCalendarViewModel = hiltViewModel(),
    onUpClick: () -> Unit = {},
) {

    val uiStateData by viewModel.uiState.collectAsState()

//    // State to hold the selected month and year
    var selectedMonth by remember { mutableStateOf(getCurrentMonthYear().first) }
    var selectedYear by remember { mutableStateOf(getCurrentMonthYear().second) }

    LaunchedEffect(selectedMonth, selectedYear) {
        viewModel.getStaffAttendanceCalendarData(
            month = (selectedMonth + 1).toString(),
            year = selectedYear.toString(),
            staffId = staffId
        )
    }


    // Sample attendance data: Map<Day of Month, Color>

    // Call the AttendanceCalendarScreen composable


    Scaffold(
        topBar = {
            AttendanceTopBar(staffName, onUpClick = onUpClick)
        },
    ) { padding ->
        when (uiStateData) {
            is CalendarUIState.Loading -> {
                LoadingLayout()
            }

            is CalendarUIState.Success -> {
                val summaryTable =
                    mapToSummaryTableData((uiStateData as CalendarUIState.Success).data)
                val attendanceMapData =
                    getAttendanceMapData((uiStateData as CalendarUIState.Success).data)
                SuccessLayout(
                    padding,
                    selectedMonth,
                    selectedYear,
                    attendanceMapData,
                    summaryTable,
                    staffName,
                    onSelectYear = {
                        selectedYear = it
                    },
                    onSelectMonth = {
                        selectedMonth = it
                    })
            }

            is CalendarUIState.Error -> {
                ErrorScreen(
                    message = (uiStateData as CalendarUIState.Error).message,
                    onRetry = {
                        viewModel.getStaffAttendanceCalendarData(
                            selectedMonth.toString(),
                            selectedYear.toString(),
                        )
                    }
                )
            }
        }
    }

}

@Composable
private fun SuccessLayout(
    padding: PaddingValues,
    selectedMonth: Int,
    selectedYear: Int,
    attendanceData: Map<Int, Color>,
    summaryTableData: List<SummaryTableData>,
    staffName: String?,
    onSelectMonth: (Int) -> Unit,
    onSelectYear: (Int) -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(padding)
    ) {
        AttendanceCalendarScreen(
            staffName = staffName,
            currentMonth = selectedMonth,
            currentYear = selectedYear,
            attendanceData = attendanceData,
            summaryTableData = summaryTableData,
            onMonthYearChange = { month, year ->
                onSelectMonth.invoke(month)
                onSelectYear.invoke(year)
            }
        )
    }
}


@Composable
private fun ErrorScreen(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text(text = "Retry")
        }
    }
}

@Composable
private fun LoadingLayout(paddingValues: PaddingValues = PaddingValues()) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues) // Use padding values from Scaffold to avoid overlap
    ) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

private fun getAttendanceMapData(dataList: List<StaffAttendanceData>): Map<Int, Color> {
    // Define the date format to parse the "start" field
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    // Convert the list to a map
    return dataList.associate { data ->
        val date: Date = dateFormat.parse(data.start) ?: Date()
        val calendar = java.util.Calendar.getInstance()
        calendar.time = date
        val day = calendar.get(java.util.Calendar.DAY_OF_MONTH)
        val color = Color(android.graphics.Color.parseColor(data.colour))
        day to color
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AttendanceTopBar(
    name: String?,
    onUpClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            Text(stringResource(id = R.string.staff_attendance_title))
        },
        modifier = modifier.statusBarsPadding(),
        navigationIcon = {
            IconButton(onClick = onUpClick) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        },
    )
}


@Composable
private fun AttendanceCalendarScreen(
    staffName: String?,
    currentMonth: Int,
    currentYear: Int,
    attendanceData: Map<Int, Color>, // Map of day to color representing status
    summaryTableData: List<SummaryTableData>,
    onMonthYearChange: (Int, Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp)
    ) {

        if (!staffName.isNullOrBlank()) {
            Text(
                text = staffName.toString(),
                modifier = Modifier
                    .background(color = Color.LightGray.copy(alpha = 0.8f)) // 50% alpha
                    .padding(8.dp)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.labelMedium,
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Month and Year Dropdown
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            MonthDropdown(currentMonth) { selectedMonth ->
                onMonthYearChange(selectedMonth, currentYear)
            }
            YearDropdown(currentYear) { selectedYear ->
                onMonthYearChange(currentMonth, selectedYear)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Weekday Headers
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                Text(text = day, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Calendar Days
        val daysInMonth = getDaysInMonth(currentMonth, currentYear)
        val firstDayOfWeek = getDayOfWeek(1, currentMonth, currentYear)

        Column(modifier = Modifier.fillMaxWidth()) {
            var dayCounter = 1
            for (week in 0 until 6) { // 6 weeks max in a month
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (dayOfWeek in 1..7) {
                        if (week == 0 && dayOfWeek < firstDayOfWeek || dayCounter > daysInMonth.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        } else {
                            val color = attendanceData[dayCounter] ?: Color.White
                            Box(modifier = Modifier.weight(1f)) {
                                DayItem(dayCounter, color)
                            }
                            dayCounter++
                        }
                    }
                }
            }
        }

//        Spacer(modifier = Modifier.height(16.dp))
//        Legend(getDateStatusItems())
        Spacer(modifier = Modifier.height(16.dp))
        AttendanceSummaryTable(summaryTableData)
    }
}

@Composable
fun MonthDropdown(currentMonth: Int, onMonthSelected: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val months = listOf(
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"
    )

    Box {
        TextButton(
            onClick = { expanded = !expanded },
            modifier = Modifier.border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
        ) {
            Row {
                Text(text = months[currentMonth])
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null
                )
            }
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            months.forEachIndexed { index, month ->
                DropdownMenuItem(onClick = {
                    onMonthSelected(index)
                    expanded = false
                }, text = { Text(text = month) })
            }
        }
    }
}

@Composable
fun YearDropdown(currentYear: Int, onYearSelected: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val years = (2020..2030).toList()

    Box {
        TextButton(
            onClick = { expanded = !expanded },
            modifier = Modifier.border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
        ) {
            Row {
                Text(text = currentYear.toString())
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null
                )
            }

        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            years.forEach { year ->
                DropdownMenuItem(
                    text = { Text(text = year.toString()) },
                    onClick = {
                        onYearSelected(year)
                        expanded = false
                    })
            }
        }
    }
}

@Composable
fun DayItem(day: Int, color: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
            .background(color)
            .border(1.dp, Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Text(text = day.toString())
    }
}


@Composable
private fun Legend(items: List<com.crm.edu.ui.compose.screens.calendar.tryouts.DayStatus>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(items) { item ->
            LegendItem(item.text, item.color)
        }
    }
}


@Composable
private fun LegendItem(text: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .background(color = color)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, style = MaterialTheme.typography.labelSmall)
    }
}

private data class AttendanceDay(
    val date: String,
    val status: AttendanceStatus
)

private fun colorForStatus(status: AttendanceStatus?): Color {
    return when (status) {
        AttendanceStatus.PRESENT -> Color.Green
        AttendanceStatus.LEAVE -> Color.Magenta
        AttendanceStatus.ABSENT -> Color.Red
        AttendanceStatus.HOLIDAY -> Color.Gray
        AttendanceStatus.COMPULSORY_OFF -> Color.Yellow
        AttendanceStatus.OUTDOOR_DUTY -> Color.Cyan
        AttendanceStatus.HALF_DAY -> Color.LightGray
        AttendanceStatus.CHECK_IN -> Color.Blue
        else -> Color.Transparent
    }
}


private fun getDateStatusItems(): List<com.crm.edu.ui.compose.screens.calendar.tryouts.DayStatus> {
    return mutableListOf(
        DayStatus(text = "Present", Color.Green),
        DayStatus(text = "Leave", Color.Magenta),
        DayStatus(text = "Absent", Color.Red),
        DayStatus(text = "Week Off / Holiday", Color.Gray),
        DayStatus(text = "Compulsory Off", Color.Yellow),
        DayStatus(text = "Outdoor Duty (ODD)", Color.Cyan),
        DayStatus(text = "Half Day", Color.LightGray),
        DayStatus(text = "Check In", Color.Blue),
    )
}


@Composable
private fun AttendanceSummaryTable(summaryTableData: List<SummaryTableData>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        items(summaryTableData) {
            AttendanceSummaryItem(it.text, it.color, it.value)
        }
    }
}

@Composable
fun AttendanceSummaryItem(label: String, color: Color, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .background(color = color)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label, style = MaterialTheme.typography.labelSmall)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun mapToSummaryTableData(dataList: List<StaffAttendanceData>): List<SummaryTableData> {
    // Count occurrences of each title
    val titleCountMap = dataList.groupingBy { it.title }.eachCount()

    // Convert the list to SummaryTableData
    return titleCountMap.map { (title, count) ->
        val colorString = dataList.find { it.title == title }?.colour ?: "#FFFFFF"
        val color = Color(android.graphics.Color.parseColor(colorString))

        SummaryTableData(
            text = title,
            color = color,
            value = count.toString()
        )
    }
}

private data class SummaryTableData(
    val text: String,
    val color: Color,
    val value: String
)

private fun getSummaryTableData(): List<SummaryTableData> {
    return mutableListOf(
        SummaryTableData("Present", Color.Green, "11.00"),
        SummaryTableData("Leave", Color.Magenta, "00.00"),
        SummaryTableData("Absent", Color.Red, "00.00"),
        SummaryTableData("Half Day", Color.LightGray, "00.00")
    )
}

private data class DayStatus(val text: String, val color: Color)