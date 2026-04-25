package com.example.capstoneutilitrack.components.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capstoneutilitrack.R
import com.example.capstoneutilitrack.model.UtilityModelDto
import androidx.navigation.NavController
import com.example.capstoneutilitrack.components.backgroundColor
import com.example.capstoneutilitrack.components.iconColor
import com.example.capstoneutilitrack.components.outlinedIcon
import com.example.capstoneutilitrack.ui.bills.toShortDate
import com.example.capstoneutilitrack.ui.payment.PaymentViewModel


@Composable
fun MonthlyUtilityCard(
    utility: UtilityModelDto,
    navController: NavController,
    paymentViewModel: PaymentViewModel,
    onClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick(utility.id)
            }
            .padding(vertical = 3.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(colorResource(R.color.card)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
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

                    Spacer(modifier = Modifier.width(8.dp))

                    Column {

                        Text(
                            text = utility.name,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            color = colorResource(R.color.burgundy)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "${utility.consumption.toInt()} ${utility.consumptionUnit}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = "Due ${utility.nextBillDate.toShortDate()}",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "${utility.currency}${utility.cost}",
                        fontSize = 18.sp,
                        modifier = Modifier.padding(end=8.dp),
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            paymentViewModel.startPaymentSession(listOf(utility))

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
                                "Pay Now",
                                color = Color(0xFFF5F5DC),
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}