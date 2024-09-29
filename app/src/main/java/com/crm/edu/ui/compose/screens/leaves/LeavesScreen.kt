package com.crm.edu.ui.compose.screens.leaves


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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.crm.edu.R
import com.crm.edu.data.leaves.LeaveData
import com.crm.edu.ui.compose.screens.calendar.tryouts.MonthDropdown
import com.crm.edu.ui.compose.screens.calendar.tryouts.YearDropdown
import com.crm.edu.ui.compose.screens.holidayLeaves.toColorOrDefault

@Composable
fun LeavesScreen(
    teamStatus: String,
    viewModel: LeavesViewModel = hiltViewModel(),
    onUpClick: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()
    val dialogState by viewModel.leavesDialogState.collectAsState()
    val isMyTeam = teamStatus.equals("1")
    val toolbarTitle =
        if (isMyTeam) stringResource(id = R.string.team_leaves_title) else stringResource(
            id = R.string.my_leaves_title
        )

    val selectedMonth by viewModel.selectedMonth.collectAsState()
    val selectedYear by viewModel.selectedYear.collectAsState()
    var showInputDialog by remember { mutableStateOf(false) }
    var rejectedId by remember { mutableStateOf("") }
    var rejectedapprovalStatus by remember { mutableStateOf("") }

    LeavesScreenInternal(
        isMyTeam,
        toolbarTitle,
        uiState,
        dialogState,
        selectedMonth,
        selectedYear,
        onUpClick,
        retry = { viewModel.refreshData() },
        onApprove = { id, approvalStatus, message ->
            viewModel.onLeaveApproved(id, approvalStatus, message)
        },
        onReject = { id, approvalStatus, message ->
            rejectedId = id
            rejectedapprovalStatus = approvalStatus
            showInputDialog = true
        },
        onDelete = { id, status ->
            viewModel.onLeaveDeleted(id, status)
        },
        onSelectMonth = { month ->
            viewModel.selectedMonth.value = month
            viewModel.refreshData()
        },
        onSelectYear = { year ->
            viewModel.selectedYear.value = year
            viewModel.refreshData()
        },
        dismissDialog = {
            viewModel.dismissDialog()
        }
    )
    if (showInputDialog) {
        InputDialog(onDismiss = {
            showInputDialog = false
        }, onDone = {
            showInputDialog = false
            viewModel.onLeaveApproved(rejectedId, rejectedapprovalStatus, it)
        })
    }
}

@Composable
private fun LeavesScreenInternal(
    isMyTeam: Boolean,
    toolbarTitle: String,
    state: UIState,
    leavesDialogState: LeavesDialogState?,
    selectedMonth: Int,
    selectedYear: Int,
    onUpClick: () -> Unit,
    retry: () -> Unit,
    onApprove: (id: String, approvalStatus: String, message: String) -> Unit,
    onReject: (id: String, approvalStatus: String, message: String) -> Unit,
    onDelete: (id: String, status: String) -> Unit,
    onSelectMonth: (Int) -> Unit,
    onSelectYear: (Int) -> Unit,
    dismissDialog: () -> Unit,
) {

    Scaffold(
        topBar = {
            TopBar(title = toolbarTitle, onUpClick = onUpClick)
        }
    ) { paddingValues ->
        when (state) {
            is UIState.Loading -> {
                LoadingLayout(paddingValues)
            }

            is UIState.Success -> {
                SuccessLayout(paddingValues = paddingValues,
                    leaveDataList = state.data.leaveDataList,
                    currentMonth = selectedMonth,
                    currentYear = selectedYear,
                    onApprove = onApprove,
                    onReject = onReject,
                    onDelete = onDelete,
                    isMyTeam = isMyTeam,
                    onMonthYearChange = { newMonth, newYear ->
                        onSelectMonth.invoke(newMonth)
                        onSelectYear.invoke(newYear)
                    })
            }

            is UIState.Error -> {
                ErrorScreen(state.message) {
                    retry()
                }
            }
        }

        leavesDialogState?.let {
            LeavesProgressDialog(it) { refresh ->
                // Handle dialog dismiss
                dismissDialog.invoke()
                if (refresh) {
                    retry()
                }
            }
        }
    }
}

@Composable
private fun DialogWindow() {

}


@Composable
private fun SuccessLayout(
    paddingValues: PaddingValues,
    leaveDataList: List<LeaveData>,
    currentMonth: Int,
    currentYear: Int,
    isMyTeam: Boolean,
    onApprove: (id: String, approvalStatus: String, message: String) -> Unit,
    onReject: (id: String, approvalStatus: String, message: String) -> Unit,
    onDelete: (id: String, status: String) -> Unit,
    onMonthYearChange: (Int, Int) -> Unit
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
        // Month and Year Dropdown
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MonthDropdown(currentMonth) { selectedMonth ->
                onMonthYearChange(selectedMonth, currentYear)
            }
            YearDropdown(currentYear) { selectedYear ->
                onMonthYearChange(currentMonth, selectedYear)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        FilterDropdown(selectedFilter) { selectedFilter = it }
        Spacer(modifier = Modifier.height(16.dp))
        LeaveRequestList(filteredLeaveDataList, isMyTeam, onApprove, onReject, onDelete)
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
private fun LeaveRequestList(
    leaveRequests: List<LeaveData>,
    isMyTeam: Boolean,
    onApprove: (id: String, approvalStatus: String, message: String) -> Unit,
    onReject: (id: String, approvalStatus: String, message: String) -> Unit,
    onDelete: (id: String, status: String) -> Unit
) {
    LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
        items(leaveRequests.size) { index ->
            LeaveRequestCard(leaveRequests[index], isMyTeam, onApprove, onReject, onDelete)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun LeaveRequestCard(
    leaveData: LeaveData,
    isMyTeam: Boolean,
    onApprove: (id: String, approvalStatus: String, message: String) -> Unit,
    onReject: (id: String, approvalStatus: String, message: String) -> Unit,
    onDelete: (id: String, status: String) -> Unit
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
            if (isMyTeam && leaveData.approvalStatus == "3") {
                Button(
                    onClick = { onApprove(leaveData.id, "1", "Your Leave has been approved !!") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text(text = "Approve")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        onReject(
                            leaveData.id,
                            "2",
                            "Sorry, Your Leave has been Rejected !!"
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
                ) {
                    Text(text = "Reject")
                }
            } else if (!isMyTeam && leaveData.approvalStatus == "3") {
                // self deletable leave
                if (leaveData.approvalStatusText.equals("Pending", true)) {
                    Button(
                        onClick = { onDelete(leaveData.id, "1") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
                    ) {
                        Text(text = "Delete")
                    }
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLeaveScreen() {
    LeavesScreen(teamStatus = "1")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    title: String,
    onUpClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            Text(title)
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