package com.crm.edu.ui.compose.screens.leaveRequest


import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import android.widget.DatePicker
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.crm.edu.R
import com.crm.edu.data.leaverequest.remote.LeaveType
import java.util.Calendar

@Composable
fun LeaveRequestScreen(
    viewModel: LeaveRequestViewModel = hiltViewModel(),
    onUpClick: () -> Unit = {},
) {

    val uiState by viewModel.uiState.collectAsState()
    val dialogState by viewModel.dialogState.collectAsState()

    LeaveRequestScreenInterval(
        uiState,
        dialogState,
        onUpClick = onUpClick,
        retry = viewModel::retry,
        dismissDialog = viewModel::dismissDialog,
        onFromDateChange = viewModel::onFromDateChange,
        onToDateChange = viewModel::onToDateChange,
        onLeaveTypeChange = viewModel::onLeaveTypeChange,
        onHalfDayChange = viewModel::onHalfDayChange,
        onHalfDayPeriodChange = viewModel::onHalfDayPeriodChange,
        onReasonChange = viewModel::onReasonChange,
        applyLeave = viewModel::applyLeave
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LeaveRequestScreenInterval(
    uiState: UIState,
    dialogState: LeaveRequestDialogState?,
    onUpClick: () -> Unit,
    retry: () -> Unit,
    dismissDialog: () -> Unit,
    onFromDateChange: (String) -> Unit,
    onToDateChange: (String) -> Unit,
    onLeaveTypeChange: (LeaveType) -> Unit,
    onHalfDayChange: (Boolean) -> Unit,
    onHalfDayPeriodChange: (Int) -> Unit,
    onReasonChange: (String) -> Unit,
    applyLeave: () -> Unit,
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopBar(onUpClick = onUpClick)
        },
    ) { padding ->

        when (uiState) {
            is UIState.Loading -> {
                LoadingLayout(padding)
            }

            is UIState.Success -> {
                SuccessLayout(
                    padding,
                    uiState.data,
                    onFromDateChange,
                    onToDateChange,
                    onLeaveTypeChange,
                    onHalfDayChange,
                    onHalfDayPeriodChange,
                    onReasonChange,
                    applyLeave
                )
            }

            is UIState.Error -> {
                ErrorScreen(uiState.message) {
                    retry()
                }
            }
        }

        dialogState?.let {
            LeavesRequestProgressDialog(it) { reset ->
                dismissDialog.invoke()
                if (reset) {
                    // reset form
                    retry()
                }
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SuccessLayout(
    padding: PaddingValues,
    leaveRequestDetail: LeaveRequestState,
    onFromDateChange: (String) -> Unit,
    onToDateChange: (String) -> Unit,
    onLeaveTypeChange: (LeaveType) -> Unit,
    onHalfDayChange: (Boolean) -> Unit,
    onHalfDayPeriodChange: (Int) -> Unit,
    onReasonChange: (String) -> Unit,
    applyLeave: () -> Unit,
) {

    var isFromDate by remember { mutableStateOf(true) }
    val context: Context = LocalContext.current
    var isValidFromDate by remember { mutableStateOf(false) }
    var isValidToDate by remember { mutableStateOf(false) }
    var isValidReason by remember { mutableStateOf(false) }

    Log.d("EduLogs", "Leave Request Detail: $leaveRequestDetail")
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val formattedMonth = String.format("%02d", month + 1)
            val formattedDay = String.format("%02d", dayOfMonth)

            val formattedDate = "$year-${formattedMonth}-$formattedDay"
            // Determine if setting "From" or "To" date based on UI interaction
            if (isFromDate) {
                onFromDateChange(formattedDate)
                isValidFromDate = formattedDate.isNotEmpty()
            } else {
                onToDateChange(formattedDate)
                isValidToDate = formattedDate.isNotEmpty()
            }
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        // From Date Picker (Placeholder for simplicity)
        OutlinedTextField(
            value = leaveRequestDetail.fromDate,
            onValueChange = {},
            isError = !isValidFromDate,
            label = { Text("From") },
            trailingIcon = {
                IconButton(onClick = {
                    isFromDate = true
                    datePickerDialog.show()
                }) {
                Icon(Icons.Default.DateRange, contentDescription = null)
                }
            },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
        )
        if (!isValidFromDate) {
            Text(
                text = "Please enter valid date", color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
            )
        }

        // To Date Picker (Placeholder for simplicity)
        OutlinedTextField(
            value = leaveRequestDetail.toDate,
            onValueChange = {},
            isError = !isValidToDate,
            label = { Text("To") },
            trailingIcon = {
                IconButton(onClick = {
                    isFromDate = false
                    datePickerDialog.show()
                }) {
                Icon(Icons.Default.DateRange, contentDescription = null)
                }
            },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()

        )
        if (!isValidToDate) {
            Text(
                text = "Please enter valid date", color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
            )
        }

        // Leave Type Dropdown
        var leaveTypeExpanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = leaveTypeExpanded,
            onExpandedChange = { leaveTypeExpanded = !leaveTypeExpanded }
        ) {
            OutlinedTextField(
                value = leaveRequestDetail.selectedLeaveType?.name.toString(),
                onValueChange = {},
                label = { Text("Leave Type") },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = leaveTypeExpanded)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = leaveTypeExpanded,
                onDismissRequest = { leaveTypeExpanded = false }
            ) {
                leaveRequestDetail.leaveTypes.forEach { leaveType ->
                    DropdownMenuItem(
                        text = { Text(leaveType.name.toString()) },
                        onClick = {
                            onLeaveTypeChange(leaveType)
                            leaveTypeExpanded = false
                        }
                    )
                }
            }
        }

        // Half Day Switch and Radio Buttons
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Switch(
                    checked = leaveRequestDetail.halfDay,
                    onCheckedChange = { onHalfDayChange(it) }
                )
                Text("Half Day")
            }
            if (leaveRequestDetail.halfDay) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RadioButton(
                        selected = leaveRequestDetail.halfDayPeriod == 1,
                        onClick = { onHalfDayPeriodChange(1) }
                    )
                    Text("First Half")

                    RadioButton(
                        selected = leaveRequestDetail.halfDayPeriod == 2,
                        onClick = { onHalfDayPeriodChange(2) }
                    )
                    Text("Second Half")
                }
            }
        }

        // Reason Text Field
        OutlinedTextField(
            value = leaveRequestDetail.reason,
            onValueChange = {
                onReasonChange(it)
                isValidReason = it.isNotEmpty()
            },

            isError = !isValidReason,
            label = { Text("Reason") },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        )
        if (!isValidReason) {
            Text(
                text = "Please enter a valid reason", color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
            )
        }

        // Apply Leave Button
        Button(
            onClick = { applyLeave() },
            modifier = Modifier.align(Alignment.CenterHorizontally),
            enabled = isValidReason && isValidFromDate && isValidToDate
        ) {
            Text("Apply Leave")
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
            Text(stringResource(id = R.string.leave_request_title))
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