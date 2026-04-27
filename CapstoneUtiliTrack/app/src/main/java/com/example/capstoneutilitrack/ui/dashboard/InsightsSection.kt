package com.example.capstoneutilitrack.ui.dashboard

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WaterfallChart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capstoneutilitrack.R
import com.example.capstoneutilitrack.model.InsightsDto
import kotlin.random.Random


@Preview(showBackground = true)
@Composable
fun Preview() {
    val insights = InsightsDto(
            values = listOf(100f, 250f, 400f, 370f, 260f),

            MonthValue = 217.5,

            ChangePercentage = 14.0,
        )
    InsightsCard(insights)
}

@Composable
fun InsightsCard(
    insights: InsightsDto,
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
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .background(
                                color = colorResource( R.color.soft_blue).copy(0.4f),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.WaterfallChart,
                            contentDescription = null,
                            tint = colorResource( R.color.steel_blue),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Insights",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 17.sp,
                        color = colorResource(R.color.burgundy)
                    )
                }

                Spacer(modifier = Modifier.height(3.dp))

                InsightsChart(
                    values = insights.values,
                    modifier = Modifier.padding(vertical = 3.dp)
                )
            }

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text("Past month: ",
                        fontSize = 14.sp,
                        color=Color.Gray,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "$${insights.MonthValue ?: 0}",
                        fontSize = 16.sp,
                        color=colorResource(R.color.deep_blue),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                ChangeIndicator(
                    percentage = insights.ChangePercentage
                )
            }
        }
    }
}

@Composable
fun ChangeIndicator(percentage: Double) {
    val isIncrease = percentage >= 0
    val color = if (!isIncrease) Color(0xFF4CAF50) else Color(0xFFF44336)

    Row(
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = "${kotlin.math.abs(percentage)}%",
            color = color,
            fontSize = 14.sp,
            modifier = Modifier.alignByBaseline()
        )
        Text(
            text = if (isIncrease) "⬈" else "⬊",
            color = color,
            fontWeight = FontWeight.Bold,
            fontSize =19.sp,
            modifier = Modifier.alignByBaseline()
        )
    }
}

@Composable
fun InsightsChart(
    values: List<Float>,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        if (values.size < 2) return@Canvas

        val max = values.maxOrNull() ?: 0f
        val min = values.minOrNull() ?: 0f
        val range = (max - min).takeIf { it != 0f } ?: 1f

        val widthStep = size.width / (values.size - 1)

        val points = values.mapIndexed { index, value ->
            val x = index * widthStep
            val y = size.height - ((value - min) / range) * size.height
            Offset(x, y)
        }

        // Draw smooth path
        val path = Path().apply {
            moveTo(points.first().x, points.first().y)

            for (i in 1 until points.size) {
                val prev = points[i - 1]
                val curr = points[i]

                val midX = (prev.x + curr.x) / 2

                cubicTo(
                    midX, prev.y,
                    midX, curr.y,
                    curr.x, curr.y
                )
            }
        }

        drawPath(
            path = path,
            color = Color(0xFF7A90A8),
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
        )

        val fillPath = Path().apply {
            addPath(path)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF7A90A8).copy(alpha = 0.4f),
                    Color(0xFFAECAE8).copy(alpha = 0.4f),
                    Color.Transparent
                )
            )
        )


        val glowPath = Path().apply {
            addPath(path)

            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }

        drawPath(
            path = glowPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFFAECAE8).copy(alpha = 0.12f),
                    Color(0xFF7A90A8).copy(alpha = 0.12f)
                ),
                startY = -size.height * 0.25f,
                endY = size.height * 1.6f
            )
        )

        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF7A90A8).copy(alpha = 0.12f),
                    Color.Transparent
                )
            ),
            radius = size.width * 0.5f,
            center = Offset(size.width * 0.8f, size.height * 0.1f)
        )

        val sparkles = List(50) {
            Offset(
                x = Random.nextFloat() * size.width,
                y = (-size.height * 0.2f) + Random.nextFloat() * (size.height * 1.5f)
            )
        }

        clipRect(
            top = -size.height * 0.5f,
            bottom = size.height
        ) {
            sparkles.forEach { point ->
                drawCircle(
                    color = Color.White.copy(alpha = 0.85f),
                    radius = Random.nextFloat() * 4.5f + 2f,
                    center = point
                )
            }
        }
    }
}