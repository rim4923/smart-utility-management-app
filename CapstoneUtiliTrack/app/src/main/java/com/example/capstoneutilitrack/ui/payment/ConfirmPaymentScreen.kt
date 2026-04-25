package com.example.capstoneutilitrack.ui.payment

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.capstoneutilitrack.R
import com.example.capstoneutilitrack.model.UtilityModelDto
import com.example.capstoneutilitrack.ui.navigation.BottomNavBar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Composable
fun ConfirmPaymentScreen(navController: NavController,
                         viewModel: PaymentViewModel) {
    Column(modifier = Modifier.background(colorResource(R.color.creamex))) {
        Box(modifier = Modifier.weight(1f)) {
            MCntent(navController,viewModel)
        }
        BottomNavBar(navController)
    }
}
@Composable
fun MCntent(
    navController: NavController,
    viewModel: PaymentViewModel
) {
    Log.d("VM", viewModel.hashCode().toString())
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(colorResource(R.color.creamex))
            .padding(16.dp)
    ) {
        val session = viewModel.session
        val card = session?.selectedCard
        val total = session?.totalAmount ?: 0.0
        val bills = session?.bills ?: emptyList()
        val context = LocalContext.current

        LaunchedEffect(Unit) {
            viewModel.loadCards()
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Icon(
                    Icons.Default.ArrowBackIos,
                    contentDescription = null,
                    tint = colorResource(R.color.burgundy).copy(0.6f),
                    modifier = Modifier.clickable { navController.popBackStack() }
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    "Confirm payment",
                    fontWeight = FontWeight.Bold,
                    fontSize = 21.sp,
                    color = colorResource(R.color.burgundy),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        Divider(
            modifier = Modifier.padding(horizontal = 10.dp),
            thickness = 1.dp,
            color = Color.Gray.copy(0.2f)
        )

        Spacer(Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFDCE6F7))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Total to pay", color = Color.Gray, fontSize = 13.sp)

                    Text(
                        "$${"%.2f".format(total)}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp,
                        color = colorResource(R.color.deep_blue)
                    )

                    Spacer(Modifier.weight(1f))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        SmallTag("${bills.size} bills")
                    }
                }

                Image(
                    painter = painterResource(R.drawable.ic_no_card),
                    contentDescription = null,
                    modifier = Modifier.padding(3.dp).fillMaxHeight(),
                    contentScale = ContentScale.Fit
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Text("Bills breakdown", fontWeight = FontWeight.SemiBold)

        Spacer(Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(colorResource(R.color.cream))
        ) {

            bills.take(5).forEachIndexed { index, item ->

                BillItem(
                    title = item.name,
                    sub = "${item.consumption} ${item.consumptionUnit}",
                    price = "$${item.cost}"
                )

                if (index < bills.take(5).lastIndex) {
                    DividerLine()
                }
            }
            Spacer(Modifier.height(8.dp))
            if (bills.size > 5) {
                Text("+${bills.size - 5} more bills",
                    color = colorResource(R.color.deep_blue),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(8.dp)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Text("Payment method", fontWeight = FontWeight.SemiBold)

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(colorResource(R.color.cream))
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                Image(
                    painter = painterResource(R.drawable.credit_card),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                Spacer(Modifier.width(10.dp))

                Column {
                    Text("**** ${card?.last4 ?: ""}", fontWeight = FontWeight.SemiBold)
                    Text(card?.brand ?: "Visa", color = Color.Gray, fontSize = 12.sp)
                }
            }

            Text(
                "Edit",
                color = colorResource(R.color.deep_blue),
                modifier = Modifier.clickable {
                    navController.popBackStack()
                }
            )
        }

        Spacer(Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Gray)
            Spacer(Modifier.width(6.dp))
            Text(
                "Your payment information is encrypted and secure.",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        Spacer(Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Total", fontWeight = FontWeight.Bold)
            Text("$${"%.2f".format(total)}", fontWeight = FontWeight.Bold, color = colorResource(R.color.deep_blue))
        }

        Spacer(Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            colorResource(R.color.steel_blue),
                            colorResource(R.color.deep_blue)
                        )
                    )
                )
                .clickable {
                    if (!viewModel.isLoading) {
                        viewModel.payBills {
                            Toast.makeText(context, "Payment successful 🎉", Toast.LENGTH_SHORT).show()

                            viewModel.resetPaymentState()
                            navController.popBackStack("bills", false)
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            if (viewModel.isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text("Confirm and pay", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
        viewModel.errorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        
        Spacer(Modifier.height(8.dp))

        Text(
            "Cancel",
            color = colorResource(R.color.deep_blue),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable { navController.popBackStack() }
        )
    }
}

@Composable
fun SmallTag(text: String) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color.White.copy(0.6f))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector= Icons.Default.ReceiptLong,
            contentDescription = null,
            tint = Color.DarkGray.copy(0.7f),
            modifier = Modifier.size(20.dp)
        )

        Text(text, fontSize = 12.sp)
    }
}

@Composable
fun BillItem(title: String, sub: String, price: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(title, fontWeight = FontWeight.SemiBold, color= colorResource(R.color.burgundy))
            Text(sub, fontSize = 12.sp, color = Color.Gray)
        }
        Text(price, fontWeight = FontWeight.SemiBold, color= colorResource(R.color.burgundy))
    }
}

@Composable
fun DividerLine() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color.Gray.copy(alpha = 0.2f))
    )
}