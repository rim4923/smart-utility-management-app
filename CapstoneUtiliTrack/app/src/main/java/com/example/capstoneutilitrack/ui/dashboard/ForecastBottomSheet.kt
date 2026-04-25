package com.example.capstoneutilitrack.ui.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.capstoneutilitrack.R
import com.example.capstoneutilitrack.model.ForecastDetailsDto
import com.example.capstoneutilitrack.model.MonthlyForecastItemDto
import com.example.capstoneutilitrack.model.UpcomingForecastDto
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.component.shape.shader.verticalGradient
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.entry.entryOf
import kotlin.math.absoluteValue

@Composable
fun ForecastBottomSheet(
    forecast: ForecastDetailsDto?,
    summary: UpcomingForecastDto,
    onClose: () -> Unit
) {
    if (forecast == null || forecast.items.isEmpty()) {
        Box(
            Modifier
                .fillMaxWidth()
                .background(colorResource(R.color.cream))
                .padding(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("Forecast not available yet")
        }
        return
    }

    val items = forecast.items

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(R.color.cream))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Forecast",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = colorResource(R.color.burgundy)
            )

            Spacer(Modifier.height(6.dp))

            Text(
                "Next 3 months",
                color = Color.Gray,
                fontSize = 13.sp
            )
        }

        Spacer(Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFDCE6F7))
                .padding(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column (
                    modifier = Modifier.weight(1f) .padding(15.dp)
                ){
                    Text("Next month",
                        color = colorResource(R.color.burgundy).copy(0.7f), fontSize = 13.sp)

                    Spacer(Modifier.height(5.dp))
                    Text(
                        "$${summary.nextMonthAmount}",
                        color = colorResource(R.color.burgundy),
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(7.dp))

                    Text(
                        text = if (summary.percentageChange >= 0)
                            "↗ ${summary.percentageChange.absoluteValue}% higher than"
                        else
                            "↘ ${summary.percentageChange.absoluteValue}% lower than",
                        color = if (summary.percentageChange > 0) Color(0xFFFF6B6B) else Color(
                            0xFF4CAF50
                        ),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(Modifier.height(5.dp))
                    Text(
                        "last month",
                        color = if (summary.percentageChange > 0) Color(0xFFFF6B6B) else Color(
                            0xFF4CAF50
                        ),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )

                }
                Box(
                    modifier = Modifier
                        .wrapContentWidth(),
                    contentAlignment = Alignment.TopEnd
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_forecast),
                        contentDescription = null,
                        modifier = Modifier.size(130.dp).padding(end = 10.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        Text("Monthly forecast", fontWeight = FontWeight.SemiBold, color = colorResource(R.color.burgundy))

        Spacer(Modifier.height(12.dp))

        ForecastChart(items)

        Spacer(Modifier.height(16.dp))

        items.forEachIndexed { index, item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier
                            .size(6.dp)
                            .background(
                                if (item.isForecast) Color(0xFF4A90E2) else Color.LightGray,
                                CircleShape
                            )
                    )
                    Spacer(Modifier.width(8.dp))

                    Text(
                        "${item.month} (${if (item.isForecast) "Forecast" else "Actual"})",
                        fontSize = 13.sp,
                        color = colorResource(R.color.burgundy)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Text("$${item.amount}", fontSize = 13.sp,
                        color = colorResource(R.color.burgundy))

                    Spacer(Modifier.width(6.dp))

                    if (item.isForecast) {
                        Text(
                            text = if (item.changePercentage >= 0)
                                "▲ ${item.changePercentage}%"
                            else
                                "▼ ${item.changePercentage.absoluteValue}%",
                            fontSize = 12.sp,
                            color = if (item.changePercentage <= 0)
                                Color(0xFF4CAF50)
                            else
                                Color(0xFFFF5252)
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE8EEF5), RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Text(
                "Forecasts are based on your past usage and current trends. Actual costs may vary.",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun ForecastChart(items: List<MonthlyForecastItemDto>) {

    val entries = items.mapIndexed { index, item ->
        entryOf(index, item.amount.toFloat())
    }

    val model = entryModelOf(entries)

    Chart(
        chart = lineChart(
            lines = listOf(
                LineChart.LineSpec(
                    lineColor = 0xFF4A90E2.toInt(),
                    lineBackgroundShader = verticalGradient(
                        colors = arrayOf(
                            Color(0x334A90E2),
                            Color(0x004A90E2)
                        )
                    )
                )
            )
        ),
        model = model,
        startAxis = rememberStartAxis(
            valueFormatter = { value, _ -> "$${value.toInt()}" }
        ),
        bottomAxis = rememberBottomAxis(
            valueFormatter = { value, _ ->
                items[value.toInt()].month.take(3)
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
    )
}