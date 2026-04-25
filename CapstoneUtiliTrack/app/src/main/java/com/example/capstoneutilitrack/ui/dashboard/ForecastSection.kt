package com.example.capstoneutilitrack.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capstoneutilitrack.R
import com.example.capstoneutilitrack.model.ForecastDetailsDto
import com.example.capstoneutilitrack.model.UpcomingForecastDto


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastCard(
    forecast: UpcomingForecastDto,
    modifier: Modifier = Modifier,
    forecastDetails: ForecastDetailsDto?
) {
    Card(
        modifier = modifier.fillMaxHeight(),
        colors = CardDefaults.cardColors(colorResource(R.color.card)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        var showSheet by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .background(Color(0xFFFFF3E0), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = Color(0xFFFFA726),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Upcoming Forecast",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 17.sp,
                        color = colorResource(R.color.burgundy)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Next Month",
                        fontSize = 15.sp,
                        color=Color.Gray,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        "$${forecast.nextMonthAmount}",
                        fontSize = 16.sp,
                        color=colorResource(R.color.deep_blue),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column {
                Divider()
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        "Forecast",
                        fontSize = 14.sp,
                        color=Color.Gray,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.alignByBaseline()
                    )
                    Text(
                        text = "⬈",
                        fontSize = 20.sp,
                        color=Color.Gray,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.alignByBaseline()
                    )
                }

                Button(
                    onClick = { showSheet = true },
                    shape = RoundedCornerShape(50),
                    contentPadding = PaddingValues(),
                    colors = androidx.compose.material.ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent
                    ),
                    elevation = androidx.compose.material.ButtonDefaults.elevation(5.dp),
                    modifier = Modifier
                        .height(26.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFF8FA8C0),
                                        Color(0xFF48596B)
                                    )
                                ),
                                shape = RoundedCornerShape(50)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "View",
                            color = Color(0xFFF5F5DC),
                            fontSize = 12.sp
                        )
                    }
                }
                if (showSheet) {
                    ModalBottomSheet(
                        onDismissRequest = { showSheet = false },
                        containerColor = colorResource(R.color.cream)
                    ) {
                        ForecastBottomSheet(
                            forecast = forecastDetails,
                            summary=forecast,
                            onClose = { showSheet = false }
                        )
                    }
                }
            }
        }
    }
}