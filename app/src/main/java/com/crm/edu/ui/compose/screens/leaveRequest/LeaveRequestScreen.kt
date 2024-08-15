package com.crm.edu.ui.compose.screens.leaveRequest


import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.crm.edu.R
import java.util.Calendar

@Composable
fun LeaveRequestScreen(
    navController: NavHostController,
    viewModel: LeaveRequestViewModel = hiltViewModel(),
    onUpClick: () -> Unit = {},
) {

    LeaveRequestScreenInterval(navController, viewModel, onUpClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LeaveRequestScreenInterval(
    navController: NavHostController,
    viewModel: LeaveRequestViewModel,
    onUpClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

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
                SuccessLayout(context, padding, (state as UIState.Success).data, viewModel)
            }

            is UIState.Error -> {
                ErrorScreen((state as UIState.Error).message) {
                    viewModel.retry()
                }
            }

            is UIState.Exit -> {
                val message = (state as UIState.Exit).message
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                onUpClick()
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
    context: Context,
    padding: PaddingValues,
    leaveRequestDetail: LeaveRequestState,
    viewModel: LeaveRequestViewModel
) {

    Log.d("EduLogs", "Leave Request Detail: $leaveRequestDetail")
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val selectedDate = "$dayOfMonth/${month + 1}/$year"
            val formattedMonth = String.format("%02d", month + 1)
            val formattedDay = String.format("%02d", dayOfMonth)

            val formattedDate = "$year-${formattedMonth}-$formattedDay"
            // Determine if setting "From" or "To" date based on UI interaction
            if (viewModel.isSelectingFromDate) {
                viewModel.onFromDateChange(formattedDate)
            } else {
                viewModel.onToDateChange(formattedDate)
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
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // From Date Picker (Placeholder for simplicity)
        OutlinedTextField(
            value = leaveRequestDetail.fromDate,
            onValueChange = { },
            label = { Text("From") },
            trailingIcon = {
                IconButton(onClick = {
                    viewModel.isSelectingFromDate = true
                    datePickerDialog.show()
                }) {
                Icon(Icons.Default.DateRange, contentDescription = null)
                }
            },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
        )

        // To Date Picker (Placeholder for simplicity)
        OutlinedTextField(
            value = leaveRequestDetail.toDate,
            onValueChange = {},
            label = { Text("To") },
            trailingIcon = {
                IconButton(onClick = {
                    viewModel.isSelectingFromDate = false
                    datePickerDialog.show()
                }) {
                Icon(Icons.Default.DateRange, contentDescription = null)
                }
            },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()

        )

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
                modifier = Modifier.fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = leaveTypeExpanded,
                onDismissRequest = { leaveTypeExpanded = false }
            ) {
                leaveRequestDetail.leaveTypes.forEach { leaveType ->
                    DropdownMenuItem(
                        text = { Text(leaveType.name.toString()) },
                        onClick = {
                            viewModel.onLeaveTypeChange(leaveType = leaveType)
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
                    onCheckedChange = viewModel::onHalfDayChange
                )
                Text("Half Day")
            }
            if (leaveRequestDetail.halfDay) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RadioButton(
                        selected = leaveRequestDetail.halfDayPeriod == "First Half",
                        onClick = { viewModel.onHalfDayPeriodChange("First Half") }
                    )
                    Text("First Half")

                    RadioButton(
                        selected = leaveRequestDetail.halfDayPeriod == "Second Half",
                        onClick = { viewModel.onHalfDayPeriodChange("Second Half") }
                    )
                    Text("Second Half")
                }
            }
        }

        // Reason Text Field
        OutlinedTextField(
            value = leaveRequestDetail.reason,
            onValueChange = viewModel::onReasonChange,
            label = { Text("Reason") },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        )

        // Apply Leave Button
        Button(
            onClick = { viewModel.applyLeave() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
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