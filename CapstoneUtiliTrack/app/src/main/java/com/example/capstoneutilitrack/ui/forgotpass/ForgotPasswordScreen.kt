package com.example.capstoneutilitrack.ui.forgotpass

import android.util.Log
import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.capstoneutilitrack.R
import com.example.capstoneutilitrack.components.buttons.AppButton
import com.example.capstoneutilitrack.components.AppTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import java.net.URLEncoder

@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
    fromProfile: Boolean
) {
    val state = viewModel.state
    var emailError by remember { mutableStateOf<String?>(null) }
    Log.d("profile", "fromProfile = $fromProfile")
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
            Icon(Icons.Default.ArrowBackIos, contentDescription = "Back", tint = colorResource(R.color.burgundy))
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
            ){
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Forgot Password?",
                    fontSize = 27.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.sp,
                    color = colorResource(R.color.burgundy),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Enter your email address or phone number, and we’ll send you a verification code to reset your password.",
                    fontSize = 16.sp,
                    color = colorResource(R.color.steel_blue),
                    letterSpacing = 0.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top= 5.dp, bottom = 15.dp)
                )

                Spacer(Modifier.height(24.dp))

                AppTextField(
                    value = state.email,
                    onValueChange = {
                        viewModel.onEmailChange(it)
                        emailError = null
                    },
                    label = "Enter your email",
                    isError = emailError != null,
                    errorMessage = emailError
                )


                Spacer(Modifier.height(26.dp))

                AppButton(
                    text = "Continue",
                    isLoading = false,
                    onClick = {
                        if (state.email.isBlank()) {
                            emailError = "Please enter your email."
                        } else if (!Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) {
                            emailError = "Enter a valid email."
                        } else {
                            emailError = null
                            viewModel.sendCodeToBackend(
                                onSuccess = {
                                    val e = URLEncoder.encode(state.email, "UTF-8")
                                    navController.navigate("verify_code?fromProfile=$fromProfile&email=$e")
                                },
                                onFailure = { emailError = it }
                            )
                        }
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
