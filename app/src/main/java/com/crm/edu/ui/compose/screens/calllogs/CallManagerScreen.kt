package com.crm.edu.ui.compose.screens.calllogs

import android.Manifest
import android.content.Context
import android.provider.CallLog
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.crm.edu.R
import com.crm.edu.domain.calllogs.NameDesignation
import com.crm.edu.ui.compose.Screen
import com.crm.edu.utils.CallLogUtils
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber

@Composable
fun CallLogsScreen(navController: NavHostController) {
    val callLogsViewModel = hiltViewModel<CallLogsViewModel>()
    val userNameDesignationState by callLogsViewModel.userNameDesignationState.collectAsState()
    val uiStateData by callLogsViewModel.callLogsUiState.collectAsState()
    MainContent(userNameDesignationState) { callLogsViewModel.markLogout() }

    when(uiStateData){
        is CallLogsUIState.moveToLogin -> {
            navController.navigate(Screen.Splash.route) {
                popUpTo(Screen.CallManager.route) {
                    inclusive = true
                }
            }
        }

        null -> Timber.tag("CallLogsScreen").d("CallLogsUIState : null")
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun MainContent(userNameDesignationState: NameDesignation, logOut: () -> Unit) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement= Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CallLogPermission(context, userNameDesignationState, logOut)
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CallLogPermission(
    context: Context,
    userNameDesignationState: NameDesignation,
    logOut: () -> Unit
) {
    val readCallLogPermissionState =
        rememberPermissionState(permission = Manifest.permission.READ_CALL_LOG)
    when (readCallLogPermissionState.status) {
        // If the camera permission is granted, then show screen with the feature enabled
        PermissionStatus.Granted -> {
            ////////////////////////////////////////// context.startService(Intent(context, CallLogUploadService::class.java))
            addHeader(context, userNameDesignationState, logOut)
            Spacer(modifier = Modifier.height(4.dp))
            val defaultSelectedValue = 1
            var selectedOption by remember { mutableStateOf("Today") }
            DynamicSelectTextField("Today", listOf("Today", "Yesterday", "Week"), "Select Time Range", { selectedValue -> selectedOption=selectedValue})
            Spacer(modifier = Modifier.height(4.dp))
            displayList(context = context, selectedValue = selectedOption)
            //AddDropdown(context,defaultSelectedValue)
            // displayList(context,defaultSelectedValue)
        }
        is PermissionStatus.Denied -> {
            Column {
                val textToShow = if ((readCallLogPermissionState.status as PermissionStatus.Denied).shouldShowRationale) {
                    // If the user has denied the permission but the rationale can be shown,
                    // then gently explain why the app requires this permission
                    "The Read Call Log is important for this app. Please grant the permission."
                } else {
                    // If it's the first time the user lands on this feature, or the user
                    // doesn't want to be asked again for this permission, explain that the
                    // permission is required
                    "Read Call Log permission required for this feature to be available. " +
                            "Please grant the permission"
                }
                Text(textToShow)
                Button(onClick = { readCallLogPermissionState.launchPermissionRequest() }) {
                    Text("Request permission")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DynamicSelectTextField(
    selectedValue: String,
    options: List<String>,
    label: String,
    onValueChangedEvent: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }


    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            readOnly = true,
            value = selectedValue,
            onValueChange = {},
            label = { Text(text = label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = OutlinedTextFieldDefaults.colors(),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option: String ->
                DropdownMenuItem(
                    text = { Text(text = option) },
                    onClick = {
                        expanded = false
                        onValueChangedEvent(option)
                    }
                )
            }
        }
    }
}

@Composable
fun box(context: Context,selectedValue:String) {
    val (receivedCallsStats, outgoingCallsStats, missedCallsStats) = CallLogUtils.getCallStats(
        context,selectedValue
    )
    Log.d("dheeresh", "receivedCallsStats $receivedCallsStats")
    Log.d("dheeresh", "outgoingCallsStats $outgoingCallsStats")
    Log.d("dheeresh", "missedCallsStats $missedCallsStats")

    val totalNumberOfCalls =
        receivedCallsStats.count + outgoingCallsStats.count + missedCallsStats.count
    val totalCallDuration =
        receivedCallsStats.duration + outgoingCallsStats.duration + missedCallsStats.duration
    val formattedDuration = CallLogUtils.getFormattedDuration(totalCallDuration)
    Column() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 5.dp)
            ) {
                cardLayout(
                    R.drawable.ic_total_calls,
                    "Total Phone Calls",
                    totalNumberOfCalls,
                    formattedDuration
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 5.dp)
            ) {
                cardLayout(
                    R.drawable.in_call,
                    "Incoming Calls",
                    receivedCallsStats.count,
                    receivedCallsStats.formattedDuration
                )
            }
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 5.dp)
            ) {
                cardLayout(
                    R.drawable.out_call,
                    "Outgoing Calls",
                    outgoingCallsStats.count,
                    outgoingCallsStats.formattedDuration
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 5.dp)
            ) {
                cardLayoutOnlyNumber(R.drawable.missed_call, "Missed Calls", missedCallsStats.count)
            }
        }

    }

}


@Composable
fun cardLayout(
    iconId: Int = R.drawable.ic_total_calls,
    title: String = "Total Calls",
    numberOfCalls: Int = 10,
    formattedDuration: String = "",
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White, contentColor = Color.Blue)
    ) {
        Column() {
            Row() {
                Image(
                    painter =
                    painterResource(id = iconId),
                    contentDescription = stringResource(id = R.string.img_content_login_header),
                    modifier = Modifier
                        .size(40.dp)
                        .padding(5.dp, 10.dp, 2.dp, 2.dp)
                )
                Text(
                    text = title,
                    fontSize = 12.sp,
                    color = Color.DarkGray,
                    modifier = Modifier
                        .padding(0.dp, 10.dp, 10.dp, 2.dp)
                )
            }


            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(10.dp))
                Image(
                    painter =
                    painterResource(id = R.drawable.ic_call),
                    contentDescription = stringResource(id = R.string.img_content_login_header),
                    modifier = Modifier
                        .size(28.dp)
                        .padding(5.dp, 2.dp, 2.dp, 0.dp)
                )
                Text(
                    text = numberOfCalls.toString(),
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(4.dp, 6.dp, 14.dp, 0.dp)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(4.dp))
                Image(
                    painter =
                    painterResource(id = R.drawable.ic_timer),
                    contentDescription = stringResource(id = R.string.img_content_login_header),
                    modifier = Modifier
                        .size(40.dp)
                        .padding(2.dp, 0.dp, 0.dp, 0.dp)
                )
                Text(
                    text = formattedDuration,
                    fontSize = 12.sp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(0.dp, 2.dp, 14.dp, 2.dp)
                )
            }
        }

    }
}

@Composable
fun cardLayoutOnlyNumber(
    iconId: Int = R.drawable.ic_total_calls,
    title: String = "Total Calls",
    numberOfCalls: Int = 10,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White, contentColor = Color.Blue)
    ) {
        Column() {
            Row() {
                Image(
                    painter =
                    painterResource(id = iconId),
                    contentDescription = stringResource(id = R.string.img_content_login_header),
                    modifier = Modifier
                        .size(40.dp)
                        .padding(5.dp, 10.dp, 2.dp, 2.dp)
                )
                Text(
                    text = title,
                    fontSize = 12.sp,
                    color = Color.DarkGray,
                    modifier = Modifier
                        .padding(0.dp, 10.dp, 14.dp, 2.dp)
                )
            }


            Spacer(modifier = Modifier.height(30.dp))
            Row() {
                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = numberOfCalls.toString(),
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(4.dp, 6.dp, 10.dp, 10.dp)
                )
            }
        }
    }
}


@Composable
fun addHeader(context: Context, userNameDesignationState: NameDesignation, logOut: () -> Unit) {
    var isPopupVisible by remember { mutableStateOf(false) }
       val showDialog = remember { mutableStateOf(false) }
      if (showDialog.value) {
           Alert(showDialog = showDialog.value,
               logOut = logOut,
               onDismiss = { showDialog.value = false })
       }
    Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier
        .fillMaxWidth()
        .padding(top = 20.dp)) {
        Text(
            text = "Call Logs",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            //  modifier = Modifier.weight(1.0f,true)
        )
        Spacer(modifier = Modifier.width(4.dp))
        IconButton(onClick = { isPopupVisible = true }) {
            Icon(Icons.Default.MoreVert, contentDescription = "More options")
        }

        if (isPopupVisible) {
            Popup(
                alignment = Alignment.TopEnd,
                onDismissRequest = { isPopupVisible = false }
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(56.dp, 16.dp, 26.dp, 16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = userNameDesignationState.name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = userNameDesignationState.designation?:"",
                            fontSize = 16.sp,
                            color = Color.Gray
                        )

                        Button(
                            onClick = {
                                showDialog.value = true
                            },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text(text = "Logout")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Alert(
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


@OptIn(ExperimentalUnitApi::class)
@Composable
fun displayList(context: Context,selectedValue:String) {
    val contactInfoList = CallLogUtils.readCalls(0, context,selectedValue)

    // on below line we are
    // creating a simple column
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn {
            item {
                box(context = context, selectedValue)
                Spacer(modifier = Modifier.height(40.dp))
            }
            items(contactInfoList) { contactInfo ->
                val durationNumber = contactInfo.duration?.toLongOrNull()
                val imageId = when (contactInfo.type) {
                    CallLog.Calls.INCOMING_TYPE -> if (durationNumber != null && durationNumber > 0) R.drawable.in_call else R.drawable.missed_call
                    CallLog.Calls.OUTGOING_TYPE -> if (durationNumber != null && durationNumber > 0) R.drawable.out_call else R.drawable.missed_call
                    CallLog.Calls.MISSED_TYPE -> R.drawable.missed_call
                    CallLog.Calls.VOICEMAIL_TYPE -> R.drawable.ic_call
                    CallLog.Calls.REJECTED_TYPE -> R.drawable.missed_call
                    CallLog.Calls.BLOCKED_TYPE -> R.drawable.missed_call
                    CallLog.Calls.ANSWERED_EXTERNALLY_TYPE -> R.drawable.ic_call
                    else -> R.drawable.ic_call
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(1.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter =
                        painterResource(id = imageId),
                        contentDescription = stringResource(id = R.string.img_content_login_header),
                        modifier = Modifier
                            .size(40.dp)
                            .padding(0.dp, 0.dp, 12.dp, 0.dp)
                    )
                    Text(
                        text = if (contactInfo.name.isNullOrBlank()) "Unknown" else contactInfo.name,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(1.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = contactInfo.number ?: "Unknown",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                    if (!contactInfo.startDateTime.isNullOrBlank()) {
                        Text(
                            text = "    " + contactInfo.startDateTime,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Light
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                HorizontalDivider(
                    color = Color.Gray,
                    thickness = 2.dp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}