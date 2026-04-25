package com.example.capstoneutilitrack.ui.forgotpass

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.capstoneutilitrack.components.buttons.AppButton
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.capstoneutilitrack.R
import com.example.capstoneutilitrack.components.AppTextField

@Composable
fun ResetPasswordScreen(
    navController: NavController,
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
    fromProfile: Boolean
) {
    val state = viewModel.state

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    val emailArg = navController.currentBackStackEntry
        ?.arguments?.getString("email") ?: ""

    LaunchedEffect(emailArg) {
        if (state.email.isBlank() && emailArg.isNotBlank()) {
            viewModel.onEmailChange(emailArg)
        }
    }
    fun isStrongPassword(password: String): Boolean {
        val regex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w\\s]).{8,}$")
        return regex.matches(password)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.creamex))
    ) {
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(Icons.Default.ArrowBackIos, contentDescription = "Back", tint =  colorResource(R.color.burgundy))
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            elevation = CardDefaults.cardElevation(16.dp),
            colors = CardDefaults.cardColors(containerColor = colorResource(R.color.card))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Reset Password",
                    fontSize = 23.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.sp,
                    color = colorResource(R.color.burgundy),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(Modifier.height(3.dp))

                Text(
                    text = "Set a new password for your account.",
                    fontSize = 16.sp,
                    color = colorResource(R.color.steel_blue),
                    letterSpacing = 0.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top= 5.dp, bottom = 15.dp)
                )

                Spacer(Modifier.height(16.dp))

                AppTextField(
                    value = state.newPassword,
                    onValueChange = {
                        viewModel.onPasswordChange(it)
                        passwordError = null
                    },
                    label = "New password",
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardType = KeyboardType.Password,
                    isError = passwordError != null,
                    errorMessage = passwordError,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = null,
                                tint = Color.Gray
                            )
                        }
                    }
                )

                Spacer(Modifier.height(8.dp))

                AppTextField(
                    value = state.confirmPassword,
                    onValueChange = {
                        viewModel.onConfirmPasswordChange(it)
                        confirmPasswordError = null
                    },
                    label = "Confirm password",
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardType = KeyboardType.Password,
                    isError = confirmPasswordError != null,
                    errorMessage = confirmPasswordError,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(
                                imageVector = if (confirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = null,
                                tint = Color.Gray
                            )
                        }
                    }
                )

                Spacer(Modifier.height(8.dp))
                state.error?.let { Text(it, color = Color.Red) }

                Spacer(Modifier.height(16.dp))

                AppButton(
                    text = "Confirm",
                    isLoading = false,
                    onClick = {
                        val password = state.newPassword
                        val confirmPassword = state.confirmPassword

                        if (password.isBlank()) {
                            passwordError = "Please enter your password."
                            return@AppButton
                        } else if (!isStrongPassword(password)) {
                            passwordError = "Password must be 8+ characters, include upper/lowercase, digit & symbol."
                            return@AppButton
                        }

                        if (confirmPassword.isBlank()) {
                            confirmPasswordError = "Please confirm your password."
                            return@AppButton
                        } else if (password != confirmPassword) {
                            confirmPasswordError = "Passwords do not match."
                            return@AppButton
                        }

                        viewModel.resetPassword(
                            onSuccess = {
                                viewModel.resetState()
                                navController.navigate("login") {
                                    popUpTo(0)
                                }
                            },
                            onFailure = {}
                        )
                    }
                )

                Spacer(modifier = Modifier.height(26.dp))

                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(40.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color.Black.copy(alpha = 0.6f))
                )
            }
        }
    }
}
