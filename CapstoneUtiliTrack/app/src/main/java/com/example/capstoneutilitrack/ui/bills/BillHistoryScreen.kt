package com.example.capstoneutilitrack.ui.bills

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.capstoneutilitrack.R
import com.example.capstoneutilitrack.ui.navigation.BottomNavBar
import com.example.capstoneutilitrack.components.*
import com.example.capstoneutilitrack.model.FilterState
import com.example.capstoneutilitrack.model.MeterType
import com.example.capstoneutilitrack.model.Status
import com.example.capstoneutilitrack.model.UtilityModelDto
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BillHistoryScreen(
    navController: NavController,
    viewModel: BillsViewModel = hiltViewModel()
){
    val state = viewModel.billsState

    LaunchedEffect(Unit) {
        viewModel.loadBills()
    }

    val bills = state?.utilities ?: emptyList()

    Column(modifier = Modifier.background(colorResource(R.color.creamex))) {
        Box(modifier = Modifier.weight(1f)) {
            ContentScreen(bills, navController)
        }
        BottomNavBar(navController = navController)
    }
}

@Composable
fun ContentScreen(
    bills: List<UtilityModelDto>,
    navController: NavController,
    viewModel: BillsViewModel = hiltViewModel()
) {
    var showFilter by remember { mutableStateOf(false) }
    var filterState by remember { mutableStateOf(FilterState()) }
    val noDateFilter = filterState.startDate.isEmpty() && filterState.endDate.isEmpty()
    val state = viewModel.billsState

    LaunchedEffect(Unit) {
        viewModel.loadBills()
    }

    val details = viewModel.selectedBillDetails

    details?.let {
        BillDetailsBottomSheet(
            bill = it,
            onDismiss = { viewModel.clearDetails() }
        )
    }

    val filteredBills = bills
        .filter { it.status == Status.Paid }
        .filter { bill ->
            val typeMatch = when (filterState.type) {
                "All" -> true
                "Electricity" -> bill.type == MeterType.ELECTRICITY
                "Water" -> bill.type == MeterType.WATER
                "Gas" -> bill.type == MeterType.GAS
                "Internet" -> bill.type == MeterType.INTERNET
                "Phone" -> bill.type == MeterType.PHONE
                else -> true
            }

            if (noDateFilter) {
                typeMatch
            } else {
                val billDate = bill.nextBillDate

                val start = parseDate(filterState.startDate)
                val end = parseDate(filterState.endDate)

                val startMatch = start?.let { !billDate.before(it) } ?: true
                val endMatch = end?.let { !billDate.after(it) } ?: true

                typeMatch && startMatch && endMatch
            }
        }

    val sortedBills = filteredBills
        .sortedByDescending { it.nextBillDate }

    val groupedBillsSorted = sortedBills
        .groupBy {
            SimpleDateFormat("MMMM yyyy", Locale.US).format(it.nextBillDate)
        }
        .toList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .padding(10.dp)
            ) {
                Icon(
                    Icons.Default.ArrowBackIos,
                    contentDescription = "Back",
                    tint = colorResource(R.color.sand)
                )
            }

            Text("Bills History", fontSize = 20.sp, fontWeight = FontWeight.Medium)

            Box(
                modifier = Modifier
                    .background(Color(0xFFEDE7E3), RoundedCornerShape(20.dp))
                    .clickable { showFilter = true }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = if (filterState.type == "All") "All Time" else filterState.type,
                    fontSize = 13.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (groupedBillsSorted.isEmpty()) {
            EmptyState()
        } else {
            groupedBillsSorted.forEach { (month, list) ->
                Text(
                    text = month,
                    fontSize = 15.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Card(
                    elevation = CardDefaults.cardElevation(3.dp),
                    modifier = Modifier
                        .background(colorResource(R.color.creamex)),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .background(colorResource(R.color.card))
                            .padding(vertical = 6.dp)
                    ) {
                        list.forEachIndexed { index, bill ->
                            HistoryFullRow(
                                utility = bill,
                                modifier = Modifier.padding(horizontal = 12.dp),
                                onBillClick = { id ->
                                    viewModel.loadBillDetails(id)
                                }
                            )

                            if (index != list.lastIndex) {
                                Divider(
                                    modifier = Modifier.padding(horizontal = 12.dp),
                                    color = Color.Gray.copy(alpha = 0.2f)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }

    if (showFilter) {
        _root_ide_package_.com.example.capstoneutilitrack.components.filter.FilterBottomSheet(
            currentFilter = filterState,
            onDismiss = { showFilter = false },
            onApply = { type, start, end ->
                filterState = FilterState(type, start, end)
            }
        )
    }
}

fun parseDate(raw: String?): Date {
    if (raw.isNullOrBlank()) return Date()

    return try {
        // Handles: 2026-05-02T23:07:26.0600000
        val localDateTime = java.time.LocalDateTime.parse(raw)
        Date.from(localDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant())
    } catch (e: Exception) {
        try {
            val offsetDateTime = java.time.OffsetDateTime.parse(raw)
            Date.from(offsetDateTime.toInstant())
        } catch (e: Exception) {
            Date() // fallback
        }
    }
}

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "No bills found",
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}