package com.example.capstoneutilitrack.components.billDetails

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capstoneutilitrack.model.BillDetailsDto
import java.nio.file.Files.size

@Composable
fun ConsumptionChartSection(bill: BillDetailsDto) {
    WeeklyConsumptionChart(
        data = bill.weeklyConsumption,
        unit = bill.consumptionUnit
    )
}
@Composable
fun WeeklyConsumptionChart(
    data: List<Int>,
    unit: String
) {
    val labels = listOf("3 WEEKS AGO", "2 WEEKS AGO", "LAST WEEK", "THIS WEEK")
    val max = data.maxOrNull() ?: 1

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
    ) {

        Canvas(modifier = Modifier.matchParentSize()) {
            val step = size.height / 4

            for (i in 1..3) {
                drawLine(
                    color = Color.Gray.copy(alpha = 0.15f),
                    start = Offset(0f, step * i),
                    end = Offset(size.width, step * i),
                    strokeWidth = 2f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                )
            }
        }

        // 🔹 Bars
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom // 👈 ensures same baseline
        ) {

            data.forEachIndexed { index, value ->

                val heightRatio = value.toFloat() / max

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier.fillMaxHeight()
                ) {

                    // 🔹 Value bubble (only last bar)
                    if (index == data.lastIndex) {
                        Box(
                            modifier = Modifier
                                .padding(bottom = 6.dp)
                                .background(
                                    Color(0xFF5C7A99),
                                    RoundedCornerShape(6.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "$value $unit",
                                color = Color.White,
                                fontSize = 10.sp
                            )
                        }
                    }

                    // 🔹 Bar
                    Box(
                        modifier = Modifier
                            .width(24.dp) // 👈 thicker
                            .height((heightRatio * 70).dp) // 👈 smaller height
                            .background(
                                color = if (index == data.lastIndex)
                                    Color(0xFF5C7A99)
                                else
                                    Color(0xFFD3DAE2),
                                shape = RoundedCornerShape(
                                    topStart = 6.dp,
                                    topEnd = 6.dp
                                )
                            )
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = labels[index],
                        fontSize = 10.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}