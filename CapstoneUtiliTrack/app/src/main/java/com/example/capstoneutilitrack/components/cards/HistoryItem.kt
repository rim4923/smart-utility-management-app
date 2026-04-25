package com.example.capstoneutilitrack.components.cards

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
import androidx.compose.material3.Icon
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.capstoneutilitrack.R
import com.example.capstoneutilitrack.components.backgroundColor
import com.example.capstoneutilitrack.components.iconColor
import com.example.capstoneutilitrack.components.outlinedIcon
import com.example.capstoneutilitrack.model.UtilityModelDto
import com.example.capstoneutilitrack.ui.bills.BillDetailsBottomSheet
import com.example.capstoneutilitrack.ui.bills.BillsViewModel
import com.example.capstoneutilitrack.ui.bills.toShortDate

@Composable
fun HistoryItem(
    utility: UtilityModelDto,
    onClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick(utility.id)
            }
            .padding(vertical = 8.dp, horizontal = 12.dp),
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

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = utility.name,
                fontSize = 15.sp,
                color = colorResource(R.color.burgundy),
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "Paid ${utility.nextBillDate.toShortDate()}",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        Box(
            modifier = Modifier
                .background(
                    Color(0xFFA2BEA2),
                    RoundedCornerShape(50)
                )
                .padding(horizontal = 10.dp, vertical = 2.dp)
        ) {
            Text(
                text = "Paid",
                fontSize = 12.sp,
                color = Color(0xFF39502E)
            )
        }
    }
}