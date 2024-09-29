package com.crm.edu.ui.compose.screens.leaves

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun InputDialog(onDismiss: () -> Unit, onDone: (String) -> Unit) {
    var userInput by remember { mutableStateOf("") }  // Holds the text input from the dialog

    AlertDialog(
        onDismissRequest = { },  // Dismiss when clicking outside the dialog
        confirmButton = {
            TextButton(onClick = {
                onDone(userInput)  // Dismiss the dialog
            }) {
                Text("Done")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onDismiss()  // Dismiss the dialog
            }) {
                Text("Cancel")
            }
        },
        text = {
            Column {
                Text(text = "Enter your reason:")
                Spacer(modifier = Modifier.height(8.dp))

                // Multi-line TextField
                TextField(
                    value = userInput,
                    onValueChange = { userInput = it },  // Update input as the user types
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),  // Make the TextField multiline by increasing the height
                    singleLine = false,  // Allow multi-line input
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done  // Show "Done" on the keyboard
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            onDone(userInput)  // Set text and close dialog when "Done" is pressed on the keyboard
                        }
                    )
                )
            }
        },
        containerColor = Color.White
    )
}
