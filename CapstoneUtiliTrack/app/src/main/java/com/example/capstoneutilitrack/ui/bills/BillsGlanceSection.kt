package com.example.capstoneutilitrack.ui.bills

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.capstoneutilitrack.R
import com.example.capstoneutilitrack.model.BillsGlanceModelDto
import com.example.capstoneutilitrack.model.Status
import com.example.capstoneutilitrack.model.UtilityModelDto
import com.example.capstoneutilitrack.ui.payment.PaymentViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun BillsGlanceSection(
    glance: BillsGlanceModelDto,
    navController: NavController,
    bills: List<UtilityModelDto>,
    paymentViewModel: PaymentViewModel,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            colorResource(R.color.steel_blue),
                            colorResource(R.color.deep_blue)
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 26.dp)
                    .offset(y=20.dp)
            ) {
                SmoothProgressCircle(progress = glance.progressPercentage)
            }

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(end = 15.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {

                    Column {
                        Text(
                            "Your Bills Overview",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                "$${glance.totalAmountDue}",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 26.sp,
                                modifier = Modifier.alignByBaseline()
                            )
                            Text(
                                " due",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp,
                                modifier = Modifier.alignByBaseline()
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 🔹 Bottom Container
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(Color(0xFFEAE7E7)),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 5.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Paid: $${glance.amountPaid}",
                                color = Color(0xFF555555),
                                fontSize = 14.sp
                            )

                            Text(
                                "Remaining: $${glance.amountLeft}",
                                color = Color(0xFF522929),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Bottom Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = null,
                                    tint = Color.Gray,
                                    modifier = Modifier.size(14.dp)
                                )

                                Spacer(modifier = Modifier.width(4.dp))

                                Text(
                                    text = "Next due: ${glance.nextBillDate.toShortDate()}",
                                    color = Color.Gray,
                                    fontSize = 12.sp
                                )
                            }
                            Button(
                                onClick = {
                                    val currentBills = bills

                                    val pendingBills = currentBills.filter { it.status == Status.Pending }

                                    paymentViewModel.startPaymentSession(pendingBills)
                                    navController.navigate("payment")
                                },
                                shape = RoundedCornerShape(50),
                                contentPadding = PaddingValues(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent
                                ),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            Brush.horizontalGradient(
                                                colors = listOf(
                                                    Color(0xFF8FA8C0),
                                                    Color(0xFF48596B)
                                                )
                                            ),
                                            shape = RoundedCornerShape(50)
                                        )
                                        .padding(horizontal = 14.dp, vertical = 4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Pay Total",
                                        color = Color(0xFFF5F5DC),
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


fun Date.toShortDate(): String {
    val sdf = SimpleDateFormat("EEE MMM dd", Locale.US)
    return sdf.format(this)
}

@Composable
fun SmoothProgressCircle(progress: Int) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(75.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {

            val strokeWidth = 10.dp.toPx()

            drawCircle(
                color = Color.White.copy(alpha = 0.1f),
                style = Stroke(width = strokeWidth)
            )

            drawArc(
                color = Color.White.copy(alpha = 0.5f),
                startAngle = -90f,
                sweepAngle = 360f * (progress / 100f),
                useCenter = false,
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round
                )
            )
        }

        Text(
            "$progress%",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}