package com.crm.edu.ui.compose.screens.holidayLeaves


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

@Composable
fun HolidayCalendarScreen(
    navController: NavHostController,
    viewModel: HolidayCalendarViewModel = hiltViewModel(),
    onUpClick: () -> Unit = {},
) {
    val optionItems = getHolidayItems()
    HolidayCalendarScreenInternal(optionItems, onUpClick)
}

@Composable
private fun HolidayCalendarScreenInternal(
    optionItems: List<Holiday>,
    onUpClick: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            HolidayTopBar(onUpClick = onUpClick)
        },
    ) { padding ->

        Box(
            modifier = Modifier
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                HeaderRow()
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn {
                    items(optionItems) { holiday ->
                        HolidayRow(holiday)
                        Divider()
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Legend()
            }
        }


    }
}

@Composable
fun Legend() {
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
fun HolidayRow(holiday: Holiday) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = holiday.day,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = holiday.occasion,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = when (holiday.leaveType) {
                LeaveType.COMPANY_HOLIDAY -> "CH"
                LeaveType.FLOATING_HOLIDAY -> "FH"
            },
            color = when (holiday.leaveType) {
                LeaveType.COMPANY_HOLIDAY -> Color.Blue
                LeaveType.FLOATING_HOLIDAY -> Color.Red
            },
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(0.5f)
        )
    }
}

@Composable
fun HeaderRow() {
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
            fontSize = 16.sp
        )
        Text(
            text = "Occasion",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f),
            fontSize = 16.sp
        )
        Text(
            text = "Leave Type",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(0.5f),
            fontSize = 16.sp
        )
    }
}

@Preview
@Composable
private fun HolidayCalendarScreenPreview(
    @PreviewParameter(HolidayCalendarScreenPreviewParamProvider::class) optionItems: List<Holiday>
) {
    HolidayCalendarScreenInternal(optionItems = optionItems)
}

private class HolidayCalendarScreenPreviewParamProvider :
    PreviewParameterProvider<List<Holiday>> {

    override val values: Sequence<List<Holiday>> =
        sequenceOf(
            getHolidayItems()
        )
}


private fun getHolidayItems(): List<Holiday> {
    val optionItems = mutableListOf(
        Holiday(
            day = "Thursday, 11 July 2024",
            occasion = "New Year",
            leaveType = LeaveType.COMPANY_HOLIDAY,
        ),
        Holiday(
            day = "Thursday, 11 July 2024",
            occasion = "Diwali",
            leaveType = LeaveType.COMPANY_HOLIDAY,
        ),
        Holiday(
            day = "Thursday, 11 July 2024",
            occasion = "Holi",
            leaveType = LeaveType.FLOATING_HOLIDAY,
        ),
        Holiday(
            day = "Thursday, 11 July 2024",
            occasion = "Navrata",
            leaveType = LeaveType.COMPANY_HOLIDAY,
        ),
    )
    return optionItems
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


data class Holiday(
    val day: String,
    val occasion: String,
    val leaveType: LeaveType
)

enum class LeaveType {
    COMPANY_HOLIDAY, FLOATING_HOLIDAY
}
