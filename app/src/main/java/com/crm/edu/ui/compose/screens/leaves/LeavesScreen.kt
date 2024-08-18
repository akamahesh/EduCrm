package com.crm.edu.ui.compose.screens.leaves


import android.content.Context
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.crm.edu.R
import com.crm.edu.core.EResult
import com.crm.edu.data.leaves.LeaveData
import com.crm.edu.ui.compose.screens.holidayLeaves.toColorOrDefault

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeavesScreen(
    navController: NavHostController,
    viewModel: LeavesViewModel = hiltViewModel(),
    onUpClick: () -> Unit = {},
    onToast: (message: String) -> Unit = {}
) {
    val context: Context = LocalContext.current
    val state by viewModel.uiState.collectAsState()
    val leaveApprovalState by viewModel.leaveApprovalState.collectAsState()

    LeavesScreenInternal(
        state,
        onUpClick,
        retry = { viewModel.fetchLeaveRequests() },
        onApprove = { id, approvalStatus, message ->
            viewModel.onLeaveApproved(id, approvalStatus, message)
        },
        onReject = { id, approvalStatus, message ->
            viewModel.onLeaveApproved(id, approvalStatus, message)
        }
    )

    leaveApprovalState?.let {
        when (it) {
            is EResult.Loading -> {
                LoadingLayout()
            }

            is EResult.Success -> {
                LaunchedEffect(Unit) {
                    onToast.invoke(it.data.message)
                    if (it.data.status == 1) {
                        viewModel.fetchLeaveRequests()
                    }
                }

            }

            is EResult.Error -> {
                LaunchedEffect(Unit) {
                    onToast.invoke(it.exception.message.toString())
                }

            }

            else -> {}
        }
    }
}

@Composable
private fun LeavesScreenInternal(
    state: UIState,
    onUpClick: () -> Unit,
    retry: () -> Unit,
    onApprove: (id: String, approvalStatus: String, message: String) -> Unit,
    onReject: (id: String, approvalStatus: String, message: String) -> Unit
) {
    val context: Context = LocalContext.current

    Scaffold(
        topBar = {
            TopBar(onUpClick = onUpClick)
        }
    ) { paddingValues ->
        when (state) {
            is UIState.Loading -> {
                LoadingLayout(paddingValues)
            }

            is UIState.Success -> {
                SuccessLayout(paddingValues, state.data.leaveDataList, onApprove, onReject)
            }

            is UIState.Error -> {
                ErrorScreen(state.message) {
                    retry.invoke()
                }
            }

        }
    }
}


@Composable
private fun SuccessLayout(
    paddingValues: PaddingValues,
    leaveDataList: List<LeaveData>,
    onApprove: (id: String, approvalStatus: String, message: String) -> Unit,
    onReject: (id: String, approvalStatus: String, message: String) -> Unit
) {
    var selectedFilter by remember { mutableStateOf("All") }
    // Apply filtering based on the selected filter
    val filteredLeaveDataList = remember(selectedFilter, leaveDataList) {
        filterLeaveData(leaveDataList, selectedFilter)
    }
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        FilterDropdown(selectedFilter) { selectedFilter = it }
        Spacer(modifier = Modifier.height(16.dp))
        LeaveRequestList(filteredLeaveDataList, onApprove, onReject)
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

fun filterLeaveData(leaveDataList: List<LeaveData>, filter: String): List<LeaveData> {
    return when (filter) {
        "All" -> leaveDataList
        "Approved" -> leaveDataList.filter { it.approvalStatusText == "Approved" }
        "Pending" -> leaveDataList.filter { it.approvalStatusText == "Pending" }
        "Rejected" -> leaveDataList.filter { it.approvalStatusText == "Rejected" }
        "Cancelled" -> leaveDataList.filter { it.approvalStatusText == "Cancelled" }
        else -> leaveDataList
    }
}

@Composable
fun FilterDropdown(selectedFilter: String, onFilterSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val filters = listOf("All", "Pending", "Approved", "Rejected", "Cancelled")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .clickable { expanded = true }
            .padding(12.dp)
    ) {
        Text(text = selectedFilter)
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            filters.forEach { filter ->
                DropdownMenuItem(
                    text = { Text(text = filter) },
                    onClick = {
                        onFilterSelected(filter)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun LeaveRequestList(
    leaveRequests: List<LeaveData>,
    onApprove: (id: String, approvalStatus: String, message: String) -> Unit,
    onReject: (id: String, approvalStatus: String, message: String) -> Unit
) {
    LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
        items(leaveRequests.size) { index ->
            LeaveRequestCard(leaveRequests[index], onApprove, onReject)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun LeaveRequestCard(
    leaveData: LeaveData,
    onApprove: (id: String, approvalStatus: String, message: String) -> Unit,
    onReject: (id: String, approvalStatus: String, message: String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = leaveData.staffName, fontWeight = FontWeight.SemiBold)
            Box(
                modifier = Modifier
                    .background(
                        leaveData.approvalStatusColor.toColorOrDefault(defaultColor = Color.Black),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(text = leaveData.approvalStatusText, color = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "From: ${leaveData.fromDate}")
        Text(text = "To: ${leaveData.toDate}")
        val leaveTypeName =
            leaveData.leaveTypeName + if (leaveData.isHalfDay == "1") ", ${leaveData.halfDayTypeName}" else ""

        Text(text = "Leave Type: $leaveTypeName")
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Reason:", fontWeight = FontWeight.SemiBold)
        Text(text = leaveData.reason)
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            Button(
                onClick = { onApprove(leaveData.id, "1", "Your Leave has been approved !!") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text(text = "Approve")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { onReject(leaveData.id, "2", "Sorry, Your Leave has been Rejected !!") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
            ) {
                Text(text = "Reject")
            }
        }
    }
}

private fun getLeaveType(type: String): String {
    return when (type) {
        "1" -> "Casual Leave"
        "2" -> "Sick Leave"
        "3" -> "Planned Leave"
        else -> "Other"
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLeaveScreen() {
    val navController = rememberNavController()
    LeavesScreen(navController)
}

private fun getLeaveList(): List<LeaveData> {
    return emptyList()
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    onUpClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            Text(stringResource(id = R.string.leaves_title))
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

fun String.toColorOrNull(): Color? {
    return try {
        Color(android.graphics.Color.parseColor(this))
    } catch (e: IllegalArgumentException) {
        null
    }
}

enum class LeaveStatus(val color: Color, val label: String) {
    Pending(Color(0xFFFFA500), "Pending"),
    Approved(Color(0xFF4CAF50), "Approved"),
    Rejected(Color(0xFFF44336), "Rejected"),
    Cancelled(Color(0xFF8B4513), "Cancelled")
}