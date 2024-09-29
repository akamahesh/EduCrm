package com.crm.edu.ui.compose.screens.holidayLeaves


import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.crm.edu.R
import com.crm.edu.core.EResult
import com.crm.edu.data.holiday.HolidayData

@Composable
fun HolidayCalendarScreen(
    navController: NavHostController,
    viewModel: HolidayCalendarViewModel = hiltViewModel(),
    onUpClick: () -> Unit = {},
) {
    val holidays by viewModel.state.collectAsState()
    HolidayCalendarScreenInternal(holidays, onUpClick) {
        viewModel.retry()
    }
}

@Composable
private fun HolidayCalendarScreenInternal(
    state: EResult<List<HolidayData>>,
    onUpClick: () -> Unit = {},
    onRetry: () -> Unit
) {
    Scaffold(
        topBar = {
            HolidayTopBar(onUpClick = onUpClick)
        },
    ) { padding ->

        when (state) {
            is EResult.Loading -> {
                LoadingLayout()
            }

            is EResult.SuccessAndLoading -> {
                val holidays = state.data
                SuccessHolidayLayout(padding, holidays)
            }

            is EResult.Success -> {
                val holidays = state.data
                SuccessHolidayLayout(padding, holidays)
            }

            is EResult.Error -> {
                val exception = state.exception
                ErrorScreen(exception.message ?: "An error occurred", onRetry = { onRetry() })

            }
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
private fun Legend() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text("CH - Company Holiday", color = Color.Blue)
        Text("FH - Floating Holiday", color = Color.Red)
    }
}

@Composable
private fun HolidayRow(holiday: HolidayData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(holiday.colour.toColorOrDefault(), shape = RoundedCornerShape(8.dp)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val holidayDate = "${holiday.weekDay} \n${holiday.holidayDate}"
        Text(
            text = holidayDate,
            fontSize = 12.sp,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        )
        Text(
            text = holiday.holidayName,
            fontSize = 12.sp,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        )
        Text(
            text = holiday.holidayType,
//            color = holiday.colour.toColorOrDefault(),
            fontSize = 10.sp,
            modifier = Modifier
                .weight(0.5f)
                .align(Alignment.CenterVertically)
        )
    }
}

@Composable
private fun HeaderRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Day",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f),
            fontSize = 14.sp
        )
        Text(
            text = "Occasion",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f),
            fontSize = 14.sp
        )
        Text(
            text = "Leave Type",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(0.5f),
            fontSize = 14.sp
        )
    }
}

@Composable
private fun SuccessHolidayLayout(padding: PaddingValues, holidays: List<HolidayData>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        HeaderRow()
        LazyColumn {
            items(holidays) { holiday ->
                HolidayRow(holiday)
                Divider()
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HolidayTopBar(
    onUpClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            Text(stringResource(id = R.string.holiday_title))
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

fun String.toColorOrDefault(defaultColor: Color = Color.White): Color {
    return try {
        if (this.isNotEmpty()) {
            Color(android.graphics.Color.parseColor(this))
        } else {
            defaultColor
        }
    } catch (e: IllegalArgumentException) {
        // Return the default color if the string is invalid
        defaultColor
    }
}