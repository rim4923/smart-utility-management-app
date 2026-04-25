package com.example.capstoneutilitrack.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.capstoneutilitrack.R
import com.example.capstoneutilitrack.components.buttons.AppButton
import com.example.capstoneutilitrack.components.AppTextField
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.ui.focus.onFocusChanged
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit = {}
) {
    var showPassword by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.loginSuccess) {
        if (viewModel.loginSuccess) {
            onLoginSuccess()
        }
    }

    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    val emailBiv = remember { BringIntoViewRequester() }
    val passwordBiv = remember { BringIntoViewRequester() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                colorResource(R.color.cream)
            )
    ) {

        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(Icons.Default.ArrowBackIos, contentDescription = "Back", tint = colorResource(R.color.sand))
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome Back!",
                color = colorResource(R.color.burgundy),
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Your utilities, organized and simplified.",
                fontSize = 16.sp,
                color = colorResource(R.color.steel_blue)
            )
            Spacer(modifier = Modifier.height(1.dp))

            Image(
                painter = painterResource(id = R.drawable.login_illustration),
                contentDescription = null,
                modifier = Modifier
                    .aspectRatio(1.4f)
            )
        }


        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 360.dp)
                .align(Alignment.BottomCenter),
            elevation = CardDefaults.cardElevation(16.dp),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(R.color.cream).copy(alpha = 0.6f))
                    .verticalScroll(scrollState)
                    .padding(20.dp)
            ) {
                Spacer(modifier = Modifier.height(15.dp))

                AppTextField(
                    value = viewModel.email,
                    onValueChange = {
                        viewModel.email = it
                        viewModel.emailError = null
                    },
                    label = "Enter email",
                    keyboardType = KeyboardType.Email,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            tint = colorResource(R.color.steel_blue).copy(alpha = 0.7f)
                        )
                    },
                    isError = viewModel.emailError != null,
                    errorMessage = viewModel.emailError,
                    modifier = Modifier
                        .fillMaxWidth()
                        .bringIntoViewRequester(emailBiv)
                        .onFocusChanged { st ->
                            if (st.isFocused) {
                                scope.launch {
                                    delay(100)
                                    emailBiv.bringIntoView()
                                }
                            }
                        }
                )

                Spacer(modifier = Modifier.height(2.dp))

                val passwordInteraction = remember { MutableInteractionSource() }
                val isPasswordFocused by passwordInteraction.collectIsFocusedAsState()

                AppTextField(
                    value = viewModel.password,
                    onValueChange = {
                        viewModel.password = it
                        viewModel.passwordError = null
                    },
                    label = "Enter password",
                    keyboardType = KeyboardType.Password,
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = colorResource(R.color.steel_blue).copy(alpha = 0.7f)
                        )
                    },
                    trailingIcon = {
                        val icon = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility
                        val iconAlpha = if (isPasswordFocused) 1f else 0.5f
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = LocalContentColor.current.copy(alpha = iconAlpha)
                            )
                        }
                    },
                    isError = viewModel.passwordError != null,
                    errorMessage = viewModel.passwordError,
                    interactionSource = passwordInteraction,
                    modifier = Modifier
                        .fillMaxWidth()
                        .bringIntoViewRequester(passwordBiv)
                        .onFocusChanged { st ->
                            if (st.isFocused) {
                                scope.launch {
                                    delay(100)
                                    passwordBiv.bringIntoView()
                                }
                            }
                        }
                )

                Text(
                    text = "Forgot password?",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = colorResource(R.color.steel_blue),
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(top = 4.dp, bottom = 8.dp)
                        .clickable { navController.navigate("forgot_password?fromProfile=false") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                AppButton(
                    text = "Log In",
                    isLoading = viewModel.isLoading,
                    onClick = { viewModel.onLoginClick() },
                    enabled = !viewModel.isLoading
                )

                Spacer(modifier = Modifier.height(28.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Divider(modifier = Modifier.weight(1f))
                    Text(
                        text = "  Log in with  ",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Divider(modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterHorizontally)
                ) {
                    listOf(
                        R.drawable.ic_apple to "Apple",
                        R.drawable.ic_facebook to "Facebook",
                        R.drawable.ic_google to "Google"
                    ).forEach { (iconRes, contentDesc) ->
                        Box(
                            modifier = Modifier
                                .size(45.dp)
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

                Spacer(modifier = Modifier.height(18.dp))

                val annotatedText = buildAnnotatedString {
                    append("New here? ")
                    pushStringAnnotation(tag = "SIGN_UP", annotation = "signup")
                    withStyle(style = SpanStyle(color = colorResource(R.color.steel_blue))) { append("Sign up >") }
                    pop()
                }

                ClickableText(
                    text = annotatedText,
                    onClick = { offset ->
                        annotatedText.getStringAnnotations(tag = "SIGN_UP", offset, offset).firstOrNull()?.let {
                            navController.navigate("signup_flow")
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = LocalTextStyle.current.copy(fontSize = 14.sp)
                )
                Spacer(modifier = Modifier.height(20.dp))

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
