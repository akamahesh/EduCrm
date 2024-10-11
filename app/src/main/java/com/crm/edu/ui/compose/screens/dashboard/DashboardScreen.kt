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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
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
        onUpClick = onUpClick,
        logout = { viewModel.logout() }
    )
}

@Composable
private fun DashboardScreenInternal(
    dashboardItems: List<DashboardItem>,
    onOptionClick: (String) -> Unit = {},
    onUpClick: () -> Unit = {},
    logout: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            DashBoardTopBar(onUpClick = onUpClick, logOut = logout)
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
    logOut: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showMenu by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(stringResource(id = R.string.dashboard_title))
        },
        actions = {
            // Menu Icon
            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_more_vert_24), // Your menu icon
                    contentDescription = "Menu"
                )
            }
        },
        modifier = modifier.statusBarsPadding(),
    )

    if (showMenu) {
        LogoutAlert(showDialog = showMenu,
            logOut = logOut,
            onDismiss = { showMenu = false })
    }
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
            name = "Holiday Calendar",
            route = Screen.HolidayCalendar.route,
            drawableRes = R.drawable.ic_holiday_leave,
            color = Color.Cyan
        ),
        DashboardItem(
            name = "Leave",
            route = Screen.LeavesOptions.route,
            drawableRes = R.drawable.ic_leave_request,
            color = Color.DarkGray
        ),
//        DashboardItem(
//            name = "Sample",
//            route = Screen.Sample.route,
//            drawableRes = R.drawable.ic_my_team_color,
//            color = Color.DarkGray
//        ),
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

@Composable
fun LogoutAlert(
    showDialog: Boolean,
    logOut: () -> Unit,
    onDismiss: () -> Unit,
) {
    if (showDialog) {
        AlertDialog(
            title = {
                Text(text = "Logout", color = Color.Black)
            },
            text = {
                Text(text = "Are you sure you want to Logout?", color = Color.Black)
            },
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = logOut) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        )
    }
}