package com.crm.edu.ui.compose.screens.leaveRequest

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
fun LeavesRequestProgressDialog(
    dialogState: LeaveRequestDialogState,
    onDismiss: (reset: Boolean) -> Unit
) {
    Dialog(
        onDismissRequest = { },
        DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        // Content of the dialog based on dialogState
        when (dialogState) {
            is LeaveRequestDialogState.Loading -> {
                LoadingContent()
            }

            is LeaveRequestDialogState.Success -> {
                SuccessContent(message = dialogState.message, dialogState.reset, onDismiss)
            }

            is LeaveRequestDialogState.Error -> {
                ErrorContent(
                    message = dialogState.message,
                    refresh = dialogState.reset,
                    onDismiss
                )
            }
        }
    }
}

@Composable
private fun LoadingContent() {
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
private fun SuccessContent(message: String, refresh: Boolean, onDismiss: (Boolean) -> Unit) {
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
private fun ErrorContent(message: String, refresh: Boolean, onDismiss: (Boolean) -> Unit) {
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