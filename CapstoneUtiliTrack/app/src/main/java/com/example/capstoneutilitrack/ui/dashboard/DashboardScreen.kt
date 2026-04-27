package com.example.capstoneutilitrack.ui.dashboard

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.capstoneutilitrack.R
import com.example.capstoneutilitrack.ui.navigation.BottomNavBar
import com.example.capstoneutilitrack.model.DashboardDto


@Composable
fun DashboardScreen(navController: NavController){
    Column(modifier = Modifier.background(colorResource(R.color.creamex))) {
        Box(modifier = Modifier.weight(1f)) {
            Content()
        }
        BottomNavBar(navController = navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val isLoading = viewModel.isLoading
    val error = viewModel.errorMessage
    val forecastDetails = viewModel.forecastDetails

    if (isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }
    val context = LocalContext.current

    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }
    if (error != null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(error, color = Color.Red)
                Spacer(Modifier.height(12.dp))
                Button(onClick = { viewModel.loadDashboard() }) {
                    Text("Retry")
                }
            }
        }
        return
    }

    val dashboard = viewModel.dashboardState

    val data = dashboard

    if (data == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No data available")
        }
        return
    }

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {

            GlanceCard(data.glanceCard)

            Spacer(Modifier.height(25.dp))

            UtilitiesSection(
                utilities = data.utilities
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min), 
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InsightsCard(
                    insights = data.insights,
                    modifier = Modifier.weight(1f)
                )

                ForecastCard(
                    forecast = data.forecast,
                    modifier = Modifier.weight(1f),
                    forecastDetails = forecastDetails
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            TopExpensesCard(
                expenses = data.topExpenses
            )
        }
    }
}

