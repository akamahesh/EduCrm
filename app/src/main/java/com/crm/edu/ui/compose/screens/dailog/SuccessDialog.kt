package com.crm.edu.ui.compose.screens.dailog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.crm.edu.R

@Composable
fun SuccessDialog(show: Boolean, message: String, onDismiss: () -> Unit) {
    if (show) {
        Dialog(
            onDismissRequest = { },
            DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(White, shape = RoundedCornerShape(8.dp)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Success Image
                Image(
                    painter = painterResource(id = R.drawable.baseline_check_circle_24),  // Replace with your success image
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)  // Set image size
                        .padding(bottom = 16.dp)  // Padding between image and message
                )

                // Success Message
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Dismiss Button
                Button(
                    onClick = {
                        onDismiss.invoke()
                    },
                    modifier = Modifier.fillMaxWidth(0.5f)  // Make button half the width of the dialog
                ) {
                    Text(text = "Dismiss")
                }
            }
        }
    }

}