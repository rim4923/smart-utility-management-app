package com.example.capstoneutilitrack.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capstoneutilitrack.R
import com.example.capstoneutilitrack.components.backgroundColor
import com.example.capstoneutilitrack.components.currencySymbol
import com.example.capstoneutilitrack.components.iconColor
import com.example.capstoneutilitrack.components.outlinedIcon
import com.example.capstoneutilitrack.model.MeterType
import com.example.capstoneutilitrack.model.Status
import com.example.capstoneutilitrack.model.UtilityModelDto
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs


@Preview(showBackground = true)
@Composable
fun MyScreenPreview() {
    val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.US)

    val utilities =

        UtilityModelDto(
            id = "bill-water-003",
            name = "Water",
            type=MeterType.WATER,
            currency = "USD",
            cost = 120.2,
            consumption = 680.0,
            consumptionUnit = "m³",
            status = Status.Overdue,
            nextBillDate = sdf.parse("01-08-2025")!!,
            pricePerUnit = "0.3",
            startDate = sdf.parse("15-07-2025")!!,
            pastMonthChange=100.0,
            forecastChange=-400.0
        )
    UtilityCard(utilities)
}

@Composable
fun UtilityCard(
    utility: UtilityModelDto
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(colorResource(R.color.card)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box() {
            Row(modifier = Modifier
                .padding(16.dp,16.dp,16.dp, 5.dp)
            ){
                Column(
                    modifier = Modifier.weight(1f)

                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .background(
                                    utility.type.backgroundColor().copy(alpha = 0.2f),
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

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = utility.name,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 17.sp,
                            color = colorResource(R.color.burgundy)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${utility.currencySymbol()}${utility.cost}",
                            color = colorResource(R.color.deep_blue),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )

                        utility.pricePerUnit?.let {
                            Text(
                                text = " ~ $it /${utility.consumptionUnit}",
                                fontSize = 13.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(start = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(7.dp))
                    Text(
                        buildAnnotatedString {
                            append("${"%.1f".format(utility.consumption)} ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(if (utility.consumptionUnit == "mins") "m" else utility.consumptionUnit)
                            }
                        },
                        fontSize = 14.sp,
                        color = Color.Black
                    )

                    Spacer(Modifier.height(6.dp))
                    Divider(color = Color.Gray.copy(alpha = 0.5f))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(7.dp)
                    ) {

                        utility.pastMonthChange?.let { change ->
                            val isNegative = change < 0

                            Text("Past month", fontSize = 13.sp, color = Color.Gray)

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "◥",
                                    fontSize = 13.sp,
                                    color = if (isNegative) Color.Red else Color(0xFF4CAF50)
                                )
                                Spacer(Modifier.width(2.dp))
                                Text(
                                    text = "${abs(change)}%",
                                    fontSize = 13.sp,
                                    color = if (isNegative) Color.Red else Color(0xFF4CAF50)
                                )
                            }
                        }

                        Text("|", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = Color.LightGray)

                        Text("This month forecast", fontSize = 13.sp, color = Color.Gray)

                        utility.forecastChange?.let { change ->
                            val isNegative = change < 0

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "◥",
                                    fontSize = 13.sp,
                                    color = if (isNegative) Color.Red else Color(0xFF4CAF50)
                                )
                                Spacer(Modifier.width(2.dp))
                                Text(
                                    text = "${abs(change)}%",
                                    fontSize = 13.sp,
                                    color = if (isNegative) Color.Red else Color(0xFF4CAF50)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
