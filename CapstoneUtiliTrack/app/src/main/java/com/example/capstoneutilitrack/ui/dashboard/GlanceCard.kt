package com.example.capstoneutilitrack.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.capstoneutilitrack.R
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capstoneutilitrack.model.GlanceModelDto
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.abs


@Preview(showBackground = true)
@Composable
fun MyScreenPreview() {

    val fakeGlance = GlanceModelDto(
        firstName = "Rim",
        currentConsumption = 217.1,
        forecastedConsumption = 345.6,
        consumptionTrend = 0.0,
        forecastedConsumptionTrend = 18.0,
        currency = "USD"
    )

    GlanceCard(fakeGlance)
}

@Composable
fun GlanceCard(glance: GlanceModelDto) {

    val today = remember {
        LocalDate.now().format(
            DateTimeFormatter.ofPattern("EEEE, MMMM d", Locale.ENGLISH)
        )
    }

    Column(modifier = Modifier.fillMaxWidth()) {

        Spacer(modifier = Modifier.height(15.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 250.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            colorResource(R.color.steel_blue),
                            colorResource(R.color.deep_blue)
                        )
                    )
                )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp),
                colors = CardDefaults.cardColors(Color.Transparent)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = today,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 11.sp
                        ),
                        color = colorResource(R.color.cream)
                    )

                    Text(
                        text = "Hello, ${glance.firstName}.",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        ),
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color.White.copy(alpha = 0.3f))
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    Text(
                        "This month’s total",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = colorResource(R.color.creamex)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row {
                        Text(
                            text = "${glance.currencySymbol()}${glance.currentConsumption}",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 25.sp
                            ),
                            color = Color.White,
                            modifier = Modifier.alignByBaseline()
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        TrendLabel(
                            glance.consumptionTrend,
                            modifier = Modifier.alignByBaseline()
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        "Forecasted total",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = colorResource(R.color.creamex)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row {
                        Text(
                            text = "${glance.currencySymbol()}${glance.forecastedConsumption}",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 25.sp
                            ),
                            color = Color.White,
                            modifier = Modifier.alignByBaseline()
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        TrendLabel(
                            glance.forecastedConsumptionTrend,
                            modifier = Modifier.alignByBaseline()
                        )
                    }
                }
            }
        }

    }
}@Composable
fun TrendLabel(value: Double, modifier: Modifier = Modifier) {
    val isZero = value == 0.0
    val isLower = value < 0

    val trendColor = when {
        isZero -> Color(0xFF56F892) // green
        isLower -> Color(0xFF56F892) // green
        else -> Color(0xFFFF4747) // red
    }

    val percent = abs(value).let {
        if (it % 1.0 == 0.0) it.toInt().toString()
        else "%.1f".format(it)
    }

    val directionText = when {
        isZero -> "higher"
        isLower -> "lower"
        else -> "higher"
    }

    val icon = when {
        isZero -> Icons.Filled.TrendingUp
        isLower -> Icons.Filled.TrendingDown
        else -> Icons.Filled.TrendingUp
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = trendColor,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        color = trendColor,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                ) {
                    append("$percent% $directionText ")
                }

                withStyle(
                    SpanStyle(
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                ) {
                    append("than last month")
                }
            },
            style = MaterialTheme.typography.bodySmall
        )
    }
}
fun GlanceModelDto.currencySymbol(): String {
    return when (currency.uppercase()) {
        "USD" -> "$"
        "EUR" -> "€"
        else -> currency
    }
}
