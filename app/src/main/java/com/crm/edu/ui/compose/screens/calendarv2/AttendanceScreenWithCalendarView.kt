package com.crm.edu.ui.compose.screens.calendarv2

import android.widget.CalendarView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.crm.edu.R
import java.util.Calendar
import java.util.Date

@Composable
fun AttendanceScreenWithCalendarView(
    staffId: String? = null,
    navController: NavHostController,
    viewModel: AttendanceScreenWithCalendarViewModel = hiltViewModel(),
    onUpClick: () -> Unit = {},
) {
    AttendanceScreenWithCalendarViewInternal(navController, viewModel, onUpClick)
}

@Composable
fun AttendanceScreenWithCalendarViewInternal(
    navController: NavHostController,
    viewModel: AttendanceScreenWithCalendarViewModel,
    onUpClick: () -> Unit = {},
) {
    var selectedDate by remember { mutableStateOf(Calendar.getInstance().timeInMillis) }

    Scaffold(
        topBar = {
            TopBar(onUpClick = onUpClick)
        },
    ) { padding ->

        Column(Modifier.padding(padding)) {
            // Month and Year Header
            Text(
                text = "Selected Date: ${Date(selectedDate)}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Default Android CalendarView
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                factory = { context ->
                    CalendarView(context).apply {
                        setOnDateChangeListener { _, year, month, dayOfMonth ->
                            // Update selected date on change
                            val calendar = Calendar.getInstance()
                            calendar.set(year, month, dayOfMonth)
                            selectedDate = calendar.timeInMillis
                        }
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
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

@Preview(showBackground = true)
@Composable
fun PreviewAttendanceScreenWithCalendarView() {
    val navController = rememberNavController()
    AttendanceScreenWithCalendarView(navController = navController)
}


