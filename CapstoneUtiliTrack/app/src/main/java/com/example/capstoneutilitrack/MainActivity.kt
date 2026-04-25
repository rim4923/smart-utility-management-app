package com.example.capstoneutilitrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.capstoneutilitrack.ui.camera.IDCameraScreen
import dagger.hilt.android.AndroidEntryPoint
import android.view.View
import com.example.capstoneutilitrack.ui.forgotpass.ForgotPasswordScreen
import android.util.Log
import android.view.WindowManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import com.example.capstoneutilitrack.ui.bills.BillHistoryScreen
import com.example.capstoneutilitrack.ui.bills.BillsScreen
import com.example.capstoneutilitrack.ui.dashboard.DashboardScreen
import com.example.capstoneutilitrack.ui.auth.Authentication
import com.example.capstoneutilitrack.ui.forgotpass.ResetPasswordScreen
import com.example.capstoneutilitrack.ui.forgotpass.VerifyCodeScreen
import com.example.capstoneutilitrack.ui.payment.ConfirmPaymentScreen
import com.example.capstoneutilitrack.ui.payment.PaymentScreen
import com.example.capstoneutilitrack.ui.profile.ProfileScreen
import com.example.capstoneutilitrack.ui.signup.SignUpFlowContainer
import androidx.compose.ui.window.DialogProperties
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.capstoneutilitrack.model.PaymentMode
import com.example.capstoneutilitrack.ui.login.LoginScreen
import com.example.capstoneutilitrack.ui.payment.AddNewCardScreen
import com.example.capstoneutilitrack.ui.payment.PaymentViewModel
import com.example.capstoneutilitrack.ui.theme.CapstoneUtiliTrackTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_FULLSCREEN

        setContent {
            CapstoneUtiliTrackTheme  {
                AppNavHost()
            }
        }
        Log.d("MainActivity","onCreate Main")
    }
}

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Signup : Screen("signup")
    object ForgotPassword : Screen("forgot_password")

    object Dashboard : Screen("dashboard")
    object Bills : Screen("bills")
    object Profile : Screen("profile")
}

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    val paymentViewModel: PaymentViewModel = hiltViewModel()
    NavHost(
        navController = navController,
        startDestination = "authentication")
    {
        composable("authentication") { Authentication(navController) }
        composable("id_camera1") { IDCameraScreen(navController = navController) }
        composable("signup_flow") { SignUpFlowContainer(navController = navController) }

        composable(Screen.Dashboard.route) { DashboardScreen(navController) }
        composable(Screen.Bills.route) { BillsScreen(navController,
            paymentViewModel= paymentViewModel) }
        composable(Screen.Profile.route) { ProfileScreen(navController) }
        composable(Screen.Login.route) {
            LoginScreen(navController = navController,
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        dialog(
            "forgot_password?fromProfile={fromProfile}",
            arguments = listOf(navArgument("fromProfile") {
                type = NavType.BoolType
                defaultValue = false
            }),
            dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
        ) { backStackEntry ->
            val fromProfile = backStackEntry.arguments?.getBoolean("fromProfile") ?: false
            ForgotPasswordScreen(navController, fromProfile = fromProfile)
        }

        dialog(
            "verify_code?fromProfile={fromProfile}&email={email}",
            arguments = listOf(
                navArgument("fromProfile") {
                    type = NavType.BoolType
                    defaultValue = false
                },
                navArgument("email") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            ),
            dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
        ) { backStackEntry ->
            val fromProfile = backStackEntry.arguments?.getBoolean("fromProfile") ?: false
            VerifyCodeScreen(navController, fromProfile = fromProfile)
        }

        dialog(
            "reset_password?fromProfile={fromProfile}&email={email}",
            arguments = listOf(
                navArgument("fromProfile") {
                    type = NavType.BoolType
                    defaultValue = false
                },
                navArgument("email") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            ),
            dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
        ) { backStackEntry ->
            val fromProfile = backStackEntry.arguments?.getBoolean("fromProfile") ?: false
            ResetPasswordScreen(navController, fromProfile = fromProfile)
        }


        composable("bill_history") {
            BillHistoryScreen(
                navController = navController
            )
        }

        composable("confirm_payment") {
            ConfirmPaymentScreen(navController, paymentViewModel)
        }

        composable("payment") {
            PaymentScreen(navController, paymentViewModel,
                mode = PaymentMode.FULL)
        }
        composable("add_card") { AddNewCardScreen(navController) }
        composable("cards") {
            PaymentScreen(
                navController,
                hiltViewModel(),
                mode = PaymentMode.PROFILE
            )
        }
    }
}
