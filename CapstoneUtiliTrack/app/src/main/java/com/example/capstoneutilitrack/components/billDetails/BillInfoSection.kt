package com.example.capstoneutilitrack.components.billDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capstoneutilitrack.R
import com.example.capstoneutilitrack.model.BillDetailsDto
import com.example.capstoneutilitrack.model.UtilityModelDto

@Composable
fun BillInfoSection(bill: BillDetailsDto) {
    Column {
        Divider(
            color = Color.Gray.copy(alpha = 0.2f),
            thickness = 0.8.dp
        )

        InfoRow(
            label = "Consumption",
            value = buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("${bill.consumption}")
                }
                append(" ${bill.consumptionUnit}")
            }
        )

        Divider(
            color = Color.Gray.copy(alpha = 0.2f),
            thickness = 0.8.dp
        )

        InfoRow(
            label = "Cost per unit",
            value = buildAnnotatedString {
                append("~")
                append(bill.pricePerUnit)
                append("/${bill.consumptionUnit}")
            }
        )
    }
}

@Composable
fun InfoRow(
    label: String, value: AnnotatedString) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = Color.Gray,
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp
        )

        Text(
            text = value,
            color = colorResource(R.color.cocoa)
        )
    }
}