package com.crm.edu.ui.compose.screens.login


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.sp
import androidx.core.util.PatternsCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.crm.edu.ui.compose.Screen
import com.skydoves.landscapist.glide.GlideImage


@Composable
fun LoginScreen(navController: NavHostController) {
    val loginViewModel = hiltViewModel<LoginViewModel>()
    val loginState by loginViewModel.loginState.collectAsState()
    val logoImageUrl by loginViewModel.updateLogoImage.collectAsState()

    var isLoadingButton by remember { mutableStateOf(false) }

    var email by remember { mutableStateOf("kunal.kumar@educationvibes.in") }
    var password by remember { mutableStateOf("123456") }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    val showDialogEvent by loginViewModel.showDialogEvent.collectAsState()
    var dialogMessage :String? by remember { mutableStateOf("-") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {

        Log.d("ffffffff","logoImageUrl $logoImageUrl")


        Box(
            modifier = Modifier
                .height(250.dp)
                .fillMaxWidth()
                .padding(top = 24.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            logoImageUrl?.let {
                GlideImage(
                    imageModel = { it },
                    requestBuilder = {
                        Glide.with(LocalContext.current).asBitmap()
                            .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                            .thumbnail(0.6f).transition(withCrossFade())
                    },
                    requestOptions = {RequestOptions().centerInside()},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp, 10.dp, 8.dp, 0.dp),
                    loading = {
                        Box(modifier = Modifier.matchParentSize()) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }
                    },
                    failure = { Text(text = "image request failed.") })
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                //emailError = !isValidEmail(it)
            },
            label = { Text("Email") },
            isError = emailError,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = Color(0xFF007273)) ,
        )
        if (emailError) {
            Text(
                text = "Invalid email address",
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                //passwordError = it.isEmpty()
            },
            label = { Text("Password") },
            isError = passwordError,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = Color(0xFF007273)) ,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = if (passwordVisible) "Hide password" else "Show password", tint = Color(0xFF007273))
                }
            },
        )
        if (passwordError) {
            Text(
                text = "Password cannot be empty",
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        Spacer(modifier = Modifier.height(30.dp))
        Button(
            onClick = {
                emailError = !isValidEmail(email)
                passwordError = password.isEmpty()
                if (!emailError && !passwordError) {
                    isLoadingButton = true
                    loginViewModel.login(email, password)
                   // isLoadingButton = false
                }
            },
            enabled = !isLoadingButton,
            modifier = Modifier.fillMaxWidth().padding(start = 100.dp, end = 100.dp),
            colors = ButtonDefaults.buttonColors(containerColor =Color(0xFF007273), contentColor = Color.White)
        ) {
            if (isLoadingButton) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(16.dp) // Adjust size as needed
                )
            } else {
                Text("Login",fontSize = 18.sp)
            }

        }
    }

    when (loginState) {
        is LoginUiState.Loading -> {
            isLoadingButton = true
        }

        is LoginUiState.Success -> {
            isLoadingButton = false
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Login.route) {
                    inclusive = true
                }
            }
        }

        is LoginUiState.Error -> {
            dialogMessage = (loginState as LoginUiState.Error).message
            isLoadingButton = false
        }

        else -> {
            // Show idle state
        }
    }

    SimpleMessageDialog(showDialog = showDialogEvent, message = dialogMessage?: "Something went wrong") { loginViewModel.onDialogDismissed() }
}

fun isValidEmail(email: String): Boolean {
    return email.isNotEmpty() && PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
}

@Composable
fun SimpleMessageDialog(
    showDialog: Boolean,
    message: String,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            text = { Text(message) },
            confirmButton = {
                Button(onClick = onDismiss) {
                    Text("OK")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    // LoginScreen()
}