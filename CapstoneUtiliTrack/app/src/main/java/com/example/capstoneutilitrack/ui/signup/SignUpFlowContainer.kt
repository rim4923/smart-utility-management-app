package com.example.capstoneutilitrack.ui.signup


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.capstoneutilitrack.ui.camera.IDUploadChoiceScreen
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.capstoneutilitrack.R

@Composable
fun SignUpFlowContainer(
    navController: NavController,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val step by viewModel.step.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.creamex).copy(0.8f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, start = 16.dp)
            ) {
                IconButton(onClick = {
                    when (step) {
                        1 -> navController.popBackStack()
                        2 -> viewModel.setStep(1)
                        3 -> viewModel.setStep(2)
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIos,
                        contentDescription = "Back",
                        tint = colorResource(R.color.sand)
                    )
                }

            }

            StepProgressIndicator(currentStep = step)
            Box(modifier = Modifier.weight(1f)) {
                when (step) {
                    1 -> SignUpScreenOne(
                        navController = navController,
                        viewModel = viewModel,
                        onSignUpSuccess = { viewModel.setStep(2) }
                    )
                    2 -> SignUpScreenTwo(
                        navController = navController,
                        viewModel = viewModel,
                        onSignUpSuccess = { viewModel.setStep(3) }
                    )
                    3 -> IDUploadChoiceScreen(
                        navController = navController,
                        onSubmitSuccess = {
                            navController.navigate("login") {
                                popUpTo("signup_flow") { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    }
}


