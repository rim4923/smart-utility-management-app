package com.example.capstoneutilitrack.components.billDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capstoneutilitrack.components.backgroundColor
import com.example.capstoneutilitrack.components.cards.buildOverdueText
import com.example.capstoneutilitrack.components.iconColor
import com.example.capstoneutilitrack.components.outlinedIcon
import com.example.capstoneutilitrack.model.BillDetailsDto
import com.example.capstoneutilitrack.model.Status
import com.example.capstoneutilitrack.model.UtilityModelDto

@Composable
fun BillHeaderCard(bill: BillDetailsDto) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .background(
                    bill.type.backgroundColor().copy(alpha = 0.1f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = bill.type.outlinedIcon(),
                contentDescription = null,
                tint = bill.type.iconColor(),
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(Modifier.width(8.dp))

        Text(
            "${bill.currency}${bill.cost}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(Modifier.weight(1f))

        Box(
            modifier = Modifier
                .background(
                    color = when (bill.status) {
                        Status.Paid -> Color(0xFFD6E9D9)
                        Status.Pending -> Color(0xFFE0E7F1)
                        Status.Overdue -> Color(0xFFF5D6D6)
                    },
                    shape = RoundedCornerShape(15.dp)
                )
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = bill.status.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = when (bill.status) {
                    Status.Paid -> Color(0xFF2E7D32)
                    Status.Pending -> Color(0xFF4A607A)
                    Status.Overdue -> Color(0xFF8B1E1E)
                }
            )
        }
    }

    if (bill.status == Status.Overdue) {
        Spacer(Modifier.height(6.dp))
        Text(
            buildOverdueText(bill.nextBillDate),
            color = Color(0xFF8B1E1E),
            fontSize = 12.sp
        )
    }
}