package com.example.capstoneutilitrack.ui.bills

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.capstoneutilitrack.R
import com.example.capstoneutilitrack.components.cards.HistoryItem
import com.example.capstoneutilitrack.model.Status
import com.example.capstoneutilitrack.model.UtilityModelDto

@Composable
fun BillHistorySection(
    bills: List<UtilityModelDto>,
    navController: NavController,
    onBillClick: (String) -> Unit
) {
    val paidBills = bills.filter { it.status == Status.Paid }.take(3)

    if (paidBills.isEmpty()) return

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recent payments",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            Text(

                text = "See all",
                color = colorResource(R.color.deep_blue),
                fontSize = 14.sp,
                modifier = Modifier.clickable {
                    navController.navigate("bill_history")
                }
            )
        }

        Divider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = Color.Gray.copy(alpha = 0.2f)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(colorResource(R.color.card)),
            elevation = CardDefaults.cardElevation(3.dp)
        ) {
            Column(
                modifier = Modifier.padding(vertical = 8.dp)
            ) {

                paidBills.forEachIndexed { index, bill ->

                    HistoryItem(
                        utility = bill,
                        onClick = onBillClick
                    )

                    if (index != paidBills.lastIndex) {
                        Divider(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            color = Color.Gray.copy(alpha = 0.2f),
                            thickness = 1.dp
                        )
                    }
                }
            }
        }
    }
}