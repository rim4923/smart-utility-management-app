package com.example.capstoneutilitrack.components.cards

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.capstoneutilitrack.R
import com.example.capstoneutilitrack.model.UtilityModelDto
import com.example.capstoneutilitrack.ui.bills.BillDetailsBottomSheet
import com.example.capstoneutilitrack.ui.bills.BillsViewModel
import com.example.capstoneutilitrack.ui.payment.PaymentViewModel
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


@Composable
fun OverdueUtilityCard(
    utility: UtilityModelDto,
    navController: NavController,
    paymentViewModel: PaymentViewModel,
    onClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable{
                onClick(utility.id)
            }
            .padding(vertical = 3.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(Color(0xFFf0dfdd)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(17.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .background(Color(0xFF985F5F), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(19.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = utility.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 17.sp,
                    color = colorResource(R.color.burgundy),
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "${utility.currency}${utility.cost}  ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = buildAnnotatedString {
                        val fullText = buildOverdueText(utility.nextBillDate)

                        val parts = fullText.split(" - ")

                        withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                            append(parts[0])
                        }

                        if (parts.size > 1) {
                            append(" - ")
                            append(parts[1])
                        }
                    },
                    fontSize = 14.sp,
                    color = Color(0xFF6C1717),
                    modifier = Modifier.weight(1f)
                )
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
                    modifier = Modifier.height(28.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFF964F4F),
                                        Color(0xFF7C3737)
                                    )
                                ),
                                shape = RoundedCornerShape(50)
                            )
                            .padding(horizontal = 12.dp, vertical = 3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Pay Now",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

fun buildOverdueText(date: Date): String {
    val today = Calendar.getInstance().time
    val diff = ((today.time - date.time) / (1000 * 60 * 60 * 24)).toInt()

    val sdf = SimpleDateFormat("MMM dd", Locale.US)
    val formattedDate = sdf.format(date)

    return "Overdue $formattedDate - $diff days overdue"
}