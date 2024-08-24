package com.crm.edu.ui.compose.screens.myteam


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.crm.edu.R
import com.crm.edu.data.myteam.StaffAttendanceData


@Composable
fun MyTeamScreen(
    navController: NavHostController,
    viewModel: MyTeamScreenViewModel = hiltViewModel(),
    onUpClick: () -> Unit = {},
    onTeamMemberSelected: (staffId: String, staffName: String) -> Unit = { s: String, s1: String -> }
) {
    val state by viewModel.uiState.collectAsState()
    MyTeamScreenInternal(navController, viewModel, state, onUpClick, onTeamMemberSelected)
}

@Composable
private fun MyTeamScreenInternal(
    navController: NavHostController,
    viewModel: MyTeamScreenViewModel,
    state: UIState,
    onUpClick: () -> Unit,
    onTeamMemberSelected: (staffId: String, staffName: String) -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(onUpClick = onUpClick)
        },
    ) { padding ->
        when (state) {
            is UIState.Loading -> {
                LoadingLayout(padding)
            }

            is UIState.Success -> {
                if (state.data.staffList.isEmpty()) {
                    ErrorScreen("No Team Members Found", false) {
                        viewModel.fetchStaffAttendance()
                    }
                } else {
                    TeamList(padding, members = state.data.staffList, onTeamMemberSelected)
                }
            }

            is UIState.Error -> {
                ErrorScreen(state.message) {
                    viewModel.fetchStaffAttendance()
                }
            }
        }
    }
}

@Composable
private fun ErrorScreen(message: String, showRetry: Boolean = true, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message)
        Spacer(modifier = Modifier.height(16.dp))
        if (showRetry) {
            Button(onClick = onRetry) {
                Text(text = "Retry")
            }
        }

    }
}


@Composable
private fun TeamList(
    padding: PaddingValues = PaddingValues(),
    members: List<StaffAttendanceData>,
    onTeamMemberSelected: (staffId: String, staffName: String) -> Unit = { s: String, s1: String -> }
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        items(members.size) { index ->
            TeamMemberItem(
                member = members[index],
                onItemClick = { onTeamMemberSelected.invoke(it.staffId, it.staffName.orEmpty()) })
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
private fun TeamMemberItem(
    member: StaffAttendanceData,
    onItemClick: (StaffAttendanceData) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .clickable {
                // Handle item click
                onItemClick.invoke(member)
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(text = member.staffName.toString(), style = MaterialTheme.typography.bodyLarge)
            Text(text = member.designation, style = MaterialTheme.typography.bodySmall)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .background(
                    Color(android.graphics.Color.parseColor(member.colour)),
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 4.dp, vertical = 2.dp)
        ) {
            val text = member.title
            Text(
                text = text,
                color = Color.Black,
                style = MaterialTheme.typography.labelSmall
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = member.time,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier
                .background(Color(0xFFF2F2F2))
                .padding(horizontal = 4.dp, vertical = 2.dp)
        )
    }
}


private fun getTeamList(): List<StaffAttendanceData> {
    return listOf(
        StaffAttendanceData(
            time = "",
            title = "Absent",
            start = "2024-08-15",
            colour = "#F4DDE5",
            designation = "Assistant",
            staffName = "Sakshi Khurana",
            staffId = "123"
        )
    )
}

@Preview
@Composable
fun PreviewTeamList() {
    Surface(color = Color(0xFFF2F2F2), modifier = Modifier.fillMaxSize()) {
        TeamList(members = getTeamList())
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
            Text(stringResource(id = R.string.my_team_title))
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