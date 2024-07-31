package com.crm.edu.ui.compose.screens.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.crm.edu.R
import com.crm.edu.ui.compose.Screen

@Composable
fun DashboardScreen(
    navController: NavHostController,
    viewModel: DashboardViewModel = hiltViewModel(),
    onOptionClick: (String) -> Unit = {},
    onUpClick: () -> Unit = {},
) {
    val dashboardItems = getAllDashboardOptions()

    DashboardScreenInternal(
        dashboardItems = dashboardItems,
        onOptionClick = onOptionClick,
        onUpClick = onUpClick
    )
}

@Composable
private fun DashboardScreenInternal(
    dashboardItems: List<DashboardItem>,
    onOptionClick: (String) -> Unit = {},
    onUpClick: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            DashBoardTopBar(onUpClick = onUpClick)
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(8.dp),
            ) {

                items(dashboardItems) { item ->
                    MainItem(
                        text = item.name,
                        drawableRes = item.drawableRes,
                        color = item.color,
                        onClick = { onOptionClick.invoke(item.route) })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashBoardTopBar(
    onUpClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            Text(stringResource(id = R.string.dashboard_title))
        },
        modifier = modifier.statusBarsPadding(),
    )
}

private fun getAllDashboardOptions(): List<DashboardItem> {
    val dashboardItems = mutableListOf(
        DashboardItem(
            name = "Attendance",
            route = Screen.Attendance.route,
            drawableRes = R.drawable.ic_attendance_green_mutlicolor,
            color = Color.Green
        ),
        DashboardItem(
            name = "Leave Request",
            route = Screen.LeaveRequest.route,
            drawableRes = R.drawable.ic_attendance_multi_color,
            color = Color.Blue
        ),
        DashboardItem(
            name = "Holidays Calendar",
            route = Screen.HolidayCalendar.route,
            drawableRes = R.drawable.ic_holiday_leave,
            color = Color.Cyan
        ),
        DashboardItem(
            name = "Leaves",
            route = Screen.Leaves.route,
            drawableRes = R.drawable.ic_leave_request,
            color = Color.DarkGray
        ),
    )
    return dashboardItems
}

private data class DashboardItem(
    val name: String,
    val route: String,
    val drawableRes: Int,
    val color: Color
)

@Preview
@Composable
private fun DashboardScreenPreview(
    @PreviewParameter(DashboardScreenPreviewParamProvider::class) dashboardItems: List<DashboardItem>
) {
    DashboardScreenInternal(dashboardItems = dashboardItems)
}

private class DashboardScreenPreviewParamProvider :
    PreviewParameterProvider<List<DashboardItem>> {

    override val values: Sequence<List<DashboardItem>> =
        sequenceOf(
            getAllDashboardOptions()
        )
}

@Composable
private fun MainItem(text: String, drawableRes: Int, color: Color, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .clickable { onClick() },
        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
                .size(120.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val painter = painterResource(id = drawableRes)
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .border(1.dp, color, CircleShape)
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = text,
                color = Color.Black,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}