package com.example.capstoneutilitrack.ui.bills

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.capstoneutilitrack.R
import com.example.capstoneutilitrack.model.Status
import com.example.capstoneutilitrack.ui.navigation.BottomNavBar
import com.example.capstoneutilitrack.ui.payment.PaymentViewModel

@Composable
fun BillsScreen(navController: NavController,
                paymentViewModel: PaymentViewModel){
    Column(modifier = Modifier.background(colorResource(R.color.creamex))) {
        Box(modifier = Modifier.weight(1f)) {
            Content(navController = navController,
                paymentViewModel= paymentViewModel)
        }
        BottomNavBar(navController = navController)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content(
    navController: NavController,
    viewModel: BillsViewModel = hiltViewModel(),
    paymentViewModel: PaymentViewModel
) {
    LaunchedEffect(Unit) {
        viewModel.loadBills()
    }
    val state = viewModel.billsState

    val details = viewModel.selectedBillDetails

    details?.let {
        BillDetailsBottomSheet(
            bill = it,
            onDismiss = { viewModel.clearDetails() }
        )
    }

    Spacer(Modifier.height(10.dp))
    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            state?.let { data ->

                BillsGlanceSection(glance = data.glanceCard, navController,
                    bills = state.utilities,
                    paymentViewModel = paymentViewModel)

                Spacer(Modifier.height(25.dp))

                OverdueBillsSection(
                    bills = data.utilities.filter { it.status == Status.Overdue },
                    navController = navController,
                    onBillClick = { id ->
                        viewModel.loadBillDetails(id)
                    },
                    paymentViewModel = paymentViewModel
                )

                Spacer(Modifier.height(25.dp))

                MonthlyBillsSection(
                    bills = data.utilities.filter { it.status == Status.Pending },
                    navController = navController,
                    onBillClick = { id ->
                        viewModel.loadBillDetails(id)
                    },
                    paymentViewModel = paymentViewModel
                )

                Spacer(Modifier.height(25.dp))

                BillHistorySection(
                    bills = data.utilities,
                    navController = navController,
                    onBillClick = { id ->
                        viewModel.loadBillDetails(id)
                    }
                )
            }
        }
    }
}