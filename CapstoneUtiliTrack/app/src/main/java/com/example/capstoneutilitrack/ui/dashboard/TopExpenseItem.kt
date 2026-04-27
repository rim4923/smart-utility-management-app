package com.example.capstoneutilitrack.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capstoneutilitrack.R
import com.example.capstoneutilitrack.components.backgroundColor
import com.example.capstoneutilitrack.components.iconColor
import com.example.capstoneutilitrack.components.outlinedIcon
import com.example.capstoneutilitrack.model.ExpenseDto
import com.example.capstoneutilitrack.model.MeterType


@Preview(showBackground = true)
@Composable
fun MiPreview() {
    val insights = listOf(
        ExpenseDto(type = MeterType.WATER, amount = 120.2, currency = "$"),
        ExpenseDto(type = MeterType.GAS, amount = 85.6, currency = "$"),
        ExpenseDto(type = MeterType.INTERNET, amount = 85.6, currency = "$")
    )
    TopExpensesCard(insights)
}

@Composable
fun TopExpensesCard(
    expenses: List<ExpenseDto>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxHeight(),
        colors = CardDefaults.cardColors(Color(0xFFF8F5F0)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(12.dp)
        ) {

            // Row 1
            Text("Top Expenses",
                fontWeight = FontWeight.SemiBold,
                fontSize = 17.sp,
                color = colorResource(R.color.burgundy)
            )

            if (expenses.isEmpty()) {
                Text(
                    "No expenses data yet",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                return@Column
            }

            Spacer(modifier = Modifier.height(20.dp))
            // Rows 2–4
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(3) { index ->
                    if (index < expenses.size) {
                        ExpenseItem(expenses[index])
                    } else {
                        Spacer(modifier = Modifier.height(50.dp))
                    }
                }
            }
        }
    }
}
@Composable
fun ExpenseItem(expense: ExpenseDto) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(Color(0xFFECE8E3)),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 12.dp,5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(
                        expense.type.backgroundColor().copy(alpha = 0.2f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = expense.type.outlinedIcon(),
                    contentDescription = null,
                    tint = expense.type.iconColor(),
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Name
            Text(
                text = expense.type.name.lowercase().replaceFirstChar { it.uppercase() },
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.weight(1f))

            // Value
            Text(
                text = "${expense.currency}${expense.amount}",
                color = Color(0xFF5C8DF6),
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                softWrap = false
            )
        }
    }
}