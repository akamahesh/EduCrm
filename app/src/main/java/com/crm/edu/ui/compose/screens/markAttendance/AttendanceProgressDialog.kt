package com.crm.edu.ui.compose.screens.markAttendance

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.crm.edu.R

@Composable
fun AttendanceProgressDialog(
    dialogState: AttendanceDialogState,
    onDismiss: (refresh: Boolean) -> Unit
) {
    Dialog(
        onDismissRequest = { },
        DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        // Content of the dialog based on dialogState
        when (dialogState) {
            is AttendanceDialogState.Loading -> {
                LoadingContent()
            }

            is AttendanceDialogState.Success -> {
                SuccessContent(message = dialogState.message, dialogState.shouldRefresh, onDismiss)
            }

            is AttendanceDialogState.Error -> {
                ErrorContent(
                    message = dialogState.message,
                    refresh = dialogState.shouldRefresh,
                    onDismiss
                )
            }
        }
    }
}

@Composable
fun LoadingContent() {
    Box(
        contentAlignment = Center,
        modifier = Modifier
            .size(100.dp)
            .background(White, shape = RoundedCornerShape(8.dp))
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun SuccessContent(message: String, refresh: Boolean, onDismiss: (Boolean) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White, shape = RoundedCornerShape(8.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            // Success Image
            painter = painterResource(id = R.drawable.baseline_check_circle_24),
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )
        Text(text = message)
        Button(onClick = { onDismiss.invoke(refresh) }) {
            Text("Dismiss")
        }
    }
}

@Composable
fun ErrorContent(message: String, refresh: Boolean, onDismiss: (Boolean) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White, shape = RoundedCornerShape(8.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            // Error Image
            painter = painterResource(id = R.drawable.baseline_error_outline_24),
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )
        Text(text = message)
        Button(onClick = { onDismiss.invoke(refresh) }) {
            Text("Retry")
        }
    }
}