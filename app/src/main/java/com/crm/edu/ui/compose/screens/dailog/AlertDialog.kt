package com.crm.edu.ui.compose.screens.dailog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ShowAlertDialog(
    title: String = "Title",
    message: String = "Message",
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    // State to control the dialog visibility
    var showDialog by remember { mutableStateOf(true) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                // hide the dialog
                showDialog = false
            },
            title = {
                Text(title)
            },
            text = {
                Text(message)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        // hide the dialog
                        showDialog = false
                        onConfirm.invoke()
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        // hide the dialog
                        showDialog = false
                        onDismiss.invoke()
                    }
                ) {
                    Text("Dismiss")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDialogExample() {
    ShowAlertDialog()
}