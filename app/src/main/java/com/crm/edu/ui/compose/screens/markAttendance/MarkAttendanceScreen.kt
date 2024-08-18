@file:JvmName("MarkAttendanceViewModelKt")

package com.crm.edu.ui.compose.screens.markAttendance


import android.location.Location
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.crm.edu.R
import com.crm.edu.core.EResult
import com.crm.edu.data.markAttendance.CheckAttendanceData
import com.crm.edu.data.markAttendance.remote.MarkAttendanceData
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MarkAttendanceScreen(
    navController: NavHostController,
    viewModel: MarkAttendanceViewModel = hiltViewModel(),
    onOptionClick: (String) -> Unit = {},
    onUpClick: () -> Unit = {},
) {
    val checkAttendanceState by viewModel.state.collectAsState()
    val markAttendanceState by viewModel.markAttendanceState.collectAsState()
    val location by viewModel.locationData.collectAsState()

    val locationPermissionState =
        rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)

    if (locationPermissionState.status.isGranted) {
        //request location before enabling buttons
        if (location == null) {
            Text(text = "Fetching your location...")
            viewModel.fetchLocation()
        } else {
            MarkAttendanceScreenInternal(
                checkAttendanceState,
                location,
                markAttendanceState,
                onCheckInCheckout = {
                    Log.d("EduLogs", "checkInOut  $it location: $location")
                    viewModel.checkInOut(
                        it,
                        location?.latitude.toString(),
                        location?.longitude.toString()
                    )
                },
                onUpClick = onUpClick
            ) {
                viewModel.retry()
            }
        }
    } else {
        PermissionLayout(locationPermissionState, onUpClick)
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun PermissionLayout(
    locationPermissionState: PermissionState,
    onUpClick: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            AttendanceTopBar(onUpClick = onUpClick)
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Location permission is required to check in/out.",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 18.sp,
                ),
                modifier = Modifier
                    .padding(start = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { locationPermissionState.launchPermissionRequest() }) {
                Text(
                    text = "Grant Permission"
                )
            }
        }
    }

}


@Composable
private fun MarkAttendanceScreenInternal(
    checkAttendanceState: EResult<CheckAttendanceData>,
    location: Location?,
    markAttendanceState: EResult<MarkAttendanceData>?,
    onCheckInCheckout: (String) -> Unit = {},
    onUpClick: () -> Unit = {},
    onRetry: () -> Unit
) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            AttendanceTopBar(onUpClick = onUpClick)
        },
    ) { innerPadding ->

        when (markAttendanceState) {
            is EResult.Success -> {
                Toast.makeText(
                    context,
                    markAttendanceState.data.message,
                    Toast.LENGTH_SHORT
                ).show()
                if (markAttendanceState.data.status == 1) {
                    onUpClick.invoke()
                }
            }

            is EResult.Loading -> {
                LoadingLayout()
            }

            is EResult.Error -> {
                Toast.makeText(
                    context,
                    "${markAttendanceState.exception}",
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {}
        }

        when (checkAttendanceState) {
            is EResult.Loading -> {
                LoadingLayout()
            }

            is EResult.Success -> {
                SuccessLayout(innerPadding, checkAttendanceState.data) {
                    onCheckInCheckout(it)
                }
            }

            is EResult.Error -> {
                val exception = checkAttendanceState.exception
                ErrorLayout(exception.message ?: "An error occurred", onRetry = { onRetry() })
            }

            else -> {}
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
private fun ErrorLayout(message: String, onRetry: () -> Unit) {
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
private fun SuccessLayout(
    innerPadding: PaddingValues,
    data: CheckAttendanceData,
    onCheckInCheckOut: (selectedId: String) -> Unit
) {
    var selectedOption by remember { mutableStateOf(data.attendanceType[0]) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
    ) {
        // "Check in - Type" Text with Background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFB2DFDB)) // Greenish background color
                .padding(vertical = 12.dp)
        ) {
            Text(
                text = data.buttonText.orEmpty(),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Dropdown Menu
        var expanded by remember { mutableStateOf(false) }


        // Dropdown Menu with Full Width and Down Arrow
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RectangleShape),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = selectedOption.name.orEmpty())
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RectangleShape)
            ) {
                data.attendanceType.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(text = option.name.orEmpty()) },
                        onClick = {
                            selectedOption = option
                            expanded = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        val buttonColor = Color(android.graphics.Color.parseColor(data.buttonColor))

        // Check In Button
        Button(
            onClick = { onCheckInCheckOut.invoke(selectedOption.id.orEmpty()) },
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(data.buttonText.orEmpty())
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AttendanceTopBar(
    onUpClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            Text(stringResource(id = R.string.mark_attendance_title))
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
