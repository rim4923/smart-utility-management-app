package com.example.capstoneutilitrack.ui.bills

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.capstoneutilitrack.R
import com.example.capstoneutilitrack.components.cards.OverdueUtilityCard
import com.example.capstoneutilitrack.model.UtilityModelDto
import com.example.capstoneutilitrack.ui.payment.PaymentViewModel

@Composable
fun OverdueBillsSection(
    bills: List<UtilityModelDto>,
    navController: NavController,
    paymentViewModel: PaymentViewModel,
    onBillClick: (String) -> Unit
) {
    var showAll by remember { mutableStateOf(false) }

    val visibleBills = if (showAll) bills else bills.take(2)

    if (bills.isEmpty()) return

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Overdue bills",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Medium
                ),
                fontSize = 18.sp,
                color = Color.Black
            )

            if (bills.size > 2) {
                Text(
                    text = if (showAll) "Show less" else "See all",
                    color = colorResource(R.color.deep_blue),
                    fontSize = 14.sp,
                    modifier = Modifier.clickable {
                        showAll = !showAll
                    }
                )
            }
        }

        Divider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = Color.Gray.copy(alpha = 0.2f)
        )

        visibleBills.forEach { bill ->
            OverdueUtilityCard(
                utility = bill,
                navController,
                onClick = onBillClick,
                paymentViewModel = paymentViewModel
            )
        }
    }
}