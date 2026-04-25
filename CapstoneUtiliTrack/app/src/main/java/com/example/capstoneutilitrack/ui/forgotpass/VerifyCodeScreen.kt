package com.example.capstoneutilitrack.ui.forgotpass

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.capstoneutilitrack.components.buttons.AppButton
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.capstoneutilitrack.R
import java.net.URLEncoder

@Composable
fun VerifyCodeScreen(
    navController: NavController,
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
    fromProfile: Boolean
) {
    val state = viewModel.state
    val code = state.enteredCode.padEnd(4, ' ')
    val emailArg = navController.currentBackStackEntry
        ?.arguments?.getString("email") ?: ""


    LaunchedEffect(emailArg) {
        if (state.email.isBlank() && emailArg.isNotBlank()) {
            viewModel.onEmailChange(emailArg)
        }
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
                    text = "Enter 4 Digit Code",
                    fontSize = 27.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.sp,
                    color = colorResource(R.color.burgundy),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "Enter the 4-digit code we sent to your email or phone to verify your identity.",
                    fontSize = 16.sp,
                    color = colorResource(R.color.steel_blue),
                    letterSpacing = 0.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top= 5.dp, bottom = 15.dp)
                )

                Spacer(Modifier.height(16.dp))


                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                ) {
                    val focusRequesters = List(4) { remember { FocusRequester() } }

                    (0..3).forEach { index ->
                        OutlinedTextField(
                            value = code.getOrNull(index)?.takeIf { it != ' ' }?.toString() ?: "",
                            onValueChange = { input ->
                                if (input.length <= 1 && input.all(Char::isDigit)) {
                                    val newCode = StringBuilder(state.enteredCode.padEnd(4, ' '))
                                    newCode.setCharAt(index, input.getOrElse(0) { ' ' })
                                    viewModel.onCodeEntered(newCode.toString().trim())

                                    if (input.isNotEmpty() && index < 3) {
                                        focusRequesters[index + 1].requestFocus()
                                    }

                                    if (input.isEmpty() && index > 0) {
                                        focusRequesters[index - 1].requestFocus()
                                    }
                                }
                            },
                            modifier = Modifier
                                .width(60.dp)
                                .height(60.dp)
                                .focusRequester(focusRequesters[index]),
                            singleLine = true,
                            textStyle = LocalTextStyle.current.copy(
                                fontSize = 22.sp,
                                textAlign = TextAlign.Center
                            ),
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(20),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = if (state.error != null) Color.Red else MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                                unfocusedBorderColor = if (state.error != null) Color.Red else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                cursorColor = if (state.error != null) Color.Red else MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                focusedTextColor = if (state.error != null) Color.Red else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                                unfocusedTextColor = if (state.error != null) Color.Red else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            ),
                            placeholder = null,
                            label = null,
                            isError = state.error != null
                        )
                    }
                }

                state.error?.let {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        it,
                        color = Color.Red,
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .fillMaxWidth()
                    )
                }
                val context = LocalContext.current
                Spacer(Modifier.height(26.dp))

                AppButton(
                    text = "Verify",
                    isLoading = state.isVerifying,
                    onClick = {
                        if (state.enteredCode.length < 4 || state.enteredCode.any { !it.isDigit() }) {
                            viewModel.onCodeEntered(state.enteredCode)
                            viewModel.setError("Please enter the full 4-digit code.")
                            return@AppButton
                        }

                        viewModel.verifyCode(
                            onSuccess = {
                                val e = URLEncoder.encode(state.email, "UTF-8")
                                navController.navigate("reset_password?fromProfile=$fromProfile&email=$e")
                            },
                            onFailure = {},
                            onLockout = {
                                Toast.makeText(
                                    context,
                                    "Too many attempts. Please request a new code.",
                                    Toast.LENGTH_LONG
                                ).show()
                                navController.popBackStack("forgot_password", inclusive = false)
                            }
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
