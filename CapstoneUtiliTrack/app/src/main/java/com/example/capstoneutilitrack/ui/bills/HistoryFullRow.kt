package com.example.capstoneutilitrack.ui.bills

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capstoneutilitrack.R
import com.example.capstoneutilitrack.components.backgroundColor
import com.example.capstoneutilitrack.components.iconColor
import com.example.capstoneutilitrack.components.outlinedIcon
import com.example.capstoneutilitrack.model.UtilityModelDto

@Composable
fun HistoryFullRow(
    utility: UtilityModelDto,
    modifier: Modifier = Modifier,
    onBillClick: (String) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onBillClick(utility.id)
            }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .background(
                    utility.type.backgroundColor().copy(alpha = 0.1f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = utility.type.outlinedIcon(),
                contentDescription = null,
                tint = utility.type.iconColor(),
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {

            Text(
                utility.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                "${utility.consumption.toInt()} ${utility.consumptionUnit}",
                fontSize = 13.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                "Paid ${utility.nextBillDate.toShortDate()}",
                fontSize = 13.sp,
                color = Color.Gray
            )
        }

        Column(horizontalAlignment = Alignment.End) {

            Text(
                "${utility.currency}${utility.cost}",
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Box(
                modifier = Modifier
                    .background(
                        Color(0xFFA2BEA2),
                        RoundedCornerShape(50)
                    )
                    .padding(horizontal = 12.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "Paid",
                    fontSize = 13.sp,
                    color = Color(0xFF39502E)
                )
            }
        }
    }
}