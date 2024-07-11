package com.crm.edu.ui.compose.screens.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.crm.edu.R
import com.crm.edu.ui.compose.screens.attendance.AttendanceStatus
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun CalendarScreen(
    navController: NavHostController,
    viewModel: CalendarViewModel = hiltViewModel(),
    onUpClick: () -> Unit = {},
) {
    val optionItems = getAttendanceData()

    CalendarScreenInternal(
        attendanceData = optionItems,
        onUpClick = onUpClick
    )
}


@Composable
private fun CalendarScreenInternal(
    attendanceData: List<AttendanceDay>,
    onUpClick: () -> Unit = {},
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val currentMonth = remember { mutableStateOf(Calendar.getInstance()) }

    Scaffold(
        topBar = {
            AttendanceTopBar(onUpClick = onUpClick)
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                CalendarHeader(
                    currentMonth = currentMonth.value,
                    onPreviousMonth = {
                        coroutineScope.launch {
                            currentMonth.value.add(Calendar.MONTH, -1)
                        }
                    },
                    onNextMonth = {
                        coroutineScope.launch {
                            currentMonth.value.add(Calendar.MONTH, 1)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                CalendarView(
                    month = currentMonth.value,
                    attendanceData = attendanceData
                )
                Spacer(modifier = Modifier.height(16.dp))
                Legend(getDateStatusItems())
                Spacer(modifier = Modifier.height(16.dp))
                AttendanceSummaryTable(getSummaryTableData())
            }
        }
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
        Text(text = label)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = value, fontWeight = FontWeight.Bold)
    }
}


@Composable
private fun CalendarHeader(
    currentMonth: Calendar,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    val monthFormat = remember { SimpleDateFormat("MMMM yyyy", Locale.getDefault()) }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPreviousMonth) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Previous Month")
        }
        Text(
            text = monthFormat.format(currentMonth.time),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        IconButton(onClick = onNextMonth) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Next Month")
        }
    }
}

@Composable
private fun CalendarView(
    month: Calendar,
    attendanceData: List<AttendanceDay>
) {
    val daysInMonth = month.getActualMaximum(Calendar.DAY_OF_MONTH)
    val firstDayOfWeek = month.get(Calendar.DAY_OF_WEEK)
    val days = (1..daysInMonth).map { day ->
        val date = month.clone() as Calendar
        date.set(Calendar.DAY_OF_MONTH, day)
        date.time
    }
    val attendanceMap = attendanceData.associateBy {
        SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.getDefault()
        ).format(Date())
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(firstDayOfWeek) {
            Box(modifier = Modifier.size(40.dp))
        }
        items(daysInMonth) { day ->
            val date = month.clone() as Calendar
            date.set(Calendar.DAY_OF_MONTH, day)
            val dateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date.time)
            val attendanceStatus = attendanceMap[dateString]?.status
            DayCell(day = day, attendanceStatus = attendanceStatus)
        }
    }
}

@Composable
private fun DayCell(day: Int, attendanceStatus: AttendanceStatus?) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(colorForStatus(attendanceStatus))
            .pointerInput(Unit) {
                detectTapGestures {
                    // Handle day click
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Text(text = day.toString(), color = Color.Black, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun Legend(items: List<DayStatus>) {
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

private fun getDateStatusItems(): List<DayStatus> {
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

private data class DayStatus(val text: String, val color: Color)

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
        Text(text = text)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AttendanceTopBar(
    onUpClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            Text(stringResource(id = R.string.my_attendance_title))
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

private fun getAttendanceData(): List<AttendanceDay> {
    val optionItems = mutableListOf(
        AttendanceDay(
            date = "2024-07-11 15:30:00",
            status = AttendanceStatus.PRESENT
        ),
        AttendanceDay(
            date = "2024-07-10 15:30:00",
            status = AttendanceStatus.PRESENT
        ),
        AttendanceDay(
            date = "2024-07-09 15:30:00",
            status = AttendanceStatus.PRESENT
        ),
    )
    return optionItems
}

@Preview
@Composable
private fun CalendarScreenPreview(
    @PreviewParameter(CalendarScreenInternalPreviewParamProvider::class) optionItems: List<AttendanceDay>
) {
    CalendarScreenInternal(attendanceData = optionItems)
}

private class CalendarScreenInternalPreviewParamProvider :
    PreviewParameterProvider<List<AttendanceDay>> {

    override val values: Sequence<List<AttendanceDay>> =
        sequenceOf(
            getAttendanceData()
        )
}
