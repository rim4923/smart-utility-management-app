package com.example.capstoneutilitrack.ui.auth

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.capstoneutilitrack.R
import com.example.capstoneutilitrack.components.buttons.AppButton


@Preview(showBackground = true)
@Composable
fun MyScreenPreview(navController: NavHostController = rememberNavController()) {
    Authentication(navController)
}
@Composable
fun Authentication(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.cream)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 60.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val gradient = remember {
                Brush.horizontalGradient(
                    listOf(
                        Color(0xFF476180),
                        Color(0xFF48596B)
                    )
                )
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Flowiq",
                fontSize = 47.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                style = androidx.compose.ui.text.TextStyle(brush = gradient)
            )

            Spacer(Modifier.height(15.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Track your utilities",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(id = R.color.burgundy)
                )

                Text(
                    text = "Manage bills. Stay in control.",
                    fontSize = 15.sp,
                    color = colorResource(id = R.color.steel_blue)
                )
            }

            Spacer(Modifier.height(40.dp))

            AsyncImage(
                model = R.drawable.login_illustration,
                contentDescription = "Illustration",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            )

            Spacer(Modifier.height(40.dp))


            AppButton(
                    text = "Sign up",
                    isLoading = false,
                    onClick = {navController.navigate("signup_flow") {
                        launchSingleTop = true
                    } }
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Get started in seconds",
                fontSize = 14.sp,
                color = colorResource(id = R.color.steel_blue),
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(Modifier.height(5.dp))

            Box(
                modifier = Modifier
                    .width(350.dp)
                    .clip(RoundedCornerShape(50))
                    .border(
                        width = 1.dp,
                        color = colorResource(id = R.color.burgundy),
                        shape = RoundedCornerShape(50)
                    )
            ) {
                OutlinedButton(
                    onClick = { navController.navigate("login")  },
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, colorResource(id = R.color.burgundy)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                ) {
                    Text(
                        text = "Log in",
                        color = colorResource(id = R.color.deep_blue),
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}
