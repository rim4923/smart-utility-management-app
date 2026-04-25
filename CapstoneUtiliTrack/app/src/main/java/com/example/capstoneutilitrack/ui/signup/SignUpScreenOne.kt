package com.example.capstoneutilitrack.ui.signup

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capstoneutilitrack.R
import com.example.capstoneutilitrack.components.buttons.AppButton
import com.example.capstoneutilitrack.components.AppTextField

@Composable
fun SignUpScreenOne(
    navController: NavController,
    viewModel: SignUpViewModel = hiltViewModel(),
    onSignUpSuccess: () -> Unit
) {
    var isReady by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isReady = true
    }

    var rememberMe by remember { mutableStateOf(false) }
    if (isReady) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .imePadding()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .align(Alignment.BottomCenter),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(R.color.cream)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 20.dp)
                ) {
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                        text = "Create your account!",
                        fontSize = 27.sp,
                        color = colorResource(R.color.cocoa),
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Text(
                        text = "One step closer to smarter utility management.",
                        fontSize = 16.sp,
                        color = colorResource(R.color.cocoa).copy(alpha = 0.7f),
                        letterSpacing = 0.sp,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 5.dp, bottom = 15.dp)
                    )


                    AppTextField(
                        value = viewModel.email,
                        onValueChange = {
                            viewModel.email = it
                            viewModel.emailError = null
                        },
                        label = "Email",
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = null,
                                tint = colorResource(R.color.steel_blue).copy(alpha = 0.7f)
                            )
                        },
                        keyboardType = KeyboardType.Email,
                        isError = viewModel.emailError != null,
                        errorMessage = viewModel.emailError
                    )


                    Spacer(modifier = Modifier.height(2.dp))

                    var passwordFocused by remember { mutableStateOf(false) }

                    AppTextField(
                        value = viewModel.password,
                        onValueChange = {
                            viewModel.password = it
                            viewModel.passwordError = null
                        },
                        label = "Password",
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = colorResource(R.color.steel_blue).copy(alpha = 0.7f)
                            )
                        },
                        keyboardType = KeyboardType.Password,
                        visualTransformation = if (viewModel.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        isError = viewModel.passwordError != null,
                        errorMessage = viewModel.passwordError,
                        trailingIcon = {
                            val image =
                                if (viewModel.passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility
                            IconButton(
                                onClick = {
                                    viewModel.passwordVisible = !viewModel.passwordVisible
                                },
                                modifier = Modifier.alpha(if (passwordFocused) 1f else 0.4f)
                            ) {
                                Icon(
                                    imageVector = image,
                                    contentDescription = "Toggle Password Visibility"
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { focusState ->
                                passwordFocused = focusState.isFocused
                            }
                    )


                    Spacer(modifier = Modifier.height(2.dp))

                    var confirmPasswordFocused by remember { mutableStateOf(false) }

                    AppTextField(
                        value = viewModel.confirmPassword,
                        onValueChange = {
                            viewModel.confirmPassword = it
                            viewModel.confirmPasswordError = null
                        },
                        label = "Confirm Password",
                        keyboardType = KeyboardType.Password,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = colorResource(R.color.steel_blue).copy(alpha = 0.7f)
                            )
                        },
                        visualTransformation = if (viewModel.confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        isError = viewModel.confirmPasswordError != null,
                        errorMessage = viewModel.confirmPasswordError,
                        trailingIcon = {
                            val image =
                                if (viewModel.confirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility
                            IconButton(
                                onClick = {
                                    viewModel.confirmPasswordVisible =
                                        !viewModel.confirmPasswordVisible
                                },
                                modifier = Modifier.alpha(if (confirmPasswordFocused) 1f else 0.4f)
                            ) {
                                Icon(
                                    imageVector = image,
                                    contentDescription = "Toggle Confirm Password Visibility"
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { confirmPasswordFocused = it.isFocused }
                    )



                    Spacer(Modifier.height(5.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = rememberMe,
                            onCheckedChange = { rememberMe = it },
                            modifier = Modifier
                                .graphicsLayer(scaleX = 0.7f, scaleY = 0.7f)
                                .offset(
                                    y = (-17).dp,
                                    x = (-15).dp
                                )
                                .align(Alignment.CenterVertically),
                            colors = CheckboxDefaults.colors(
                                checkedColor = colorResource(R.color.deep_blue),
                                uncheckedColor = colorResource(R.color.steel_blue),
                                checkmarkColor = colorResource(R.color.soft_blue)
                            )
                        )

                        Spacer(Modifier.width(2.dp))

                        Text(
                            text = "Remember me",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = colorResource(R.color.deep_blue),
                            modifier = Modifier
                                .offset(
                                    x = (-20).dp
                                )
                                .alignByBaseline()
                        )
                    }


                    Spacer(Modifier.height(5.dp))

                    AppButton(
                        text = "Continue",
                        isLoading = viewModel.isLoading,
                        onClick = {
                            viewModel.validateStepOne (
                                onSuccess = onSignUpSuccess
                            )
                        },
                        enabled = !viewModel.isLoading,
                        modifier = Modifier.fillMaxWidth()
                    )
                    viewModel.errorMessage?.let {
                        Text(
                            text = it,
                            color = Color.Red,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(Modifier.height(24.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Divider(modifier = Modifier.weight(1f))
                        Text(
                            text = "  Sign up with  ",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Divider(modifier = Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(
                            30.dp,
                            Alignment.CenterHorizontally
                        )
                    ) {
                        listOf(
                            R.drawable.ic_apple to "Apple",
                            R.drawable.ic_facebook to "Facebook",
                            R.drawable.ic_google to "Google"
                        ).forEach { (iconRes, contentDesc) ->
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .clickable { },
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = iconRes),
                                    contentDescription = contentDesc,
                                    modifier = Modifier.size(50.dp)
                                )
                            }

                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    val annotatedText = buildAnnotatedString {
                        append("Already have an account? ")
                        pushStringAnnotation(tag = "LOGIN", annotation = "login")
                        withStyle(style = SpanStyle(color = colorResource(R.color.steel_blue))) {
                            append("Log in")
                        }
                        pop()
                    }

                    ClickableText(
                        text = annotatedText,
                        onClick = { offset ->
                            annotatedText.getStringAnnotations(
                                tag = "LOGIN",
                                start = offset,
                                end = offset
                            )
                                .firstOrNull()?.let {
                                    navController.navigate("login")
                                }
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(35.dp))

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
}

