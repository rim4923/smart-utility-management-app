package com.example.capstoneutilitrack.ui.payment

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
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.capstoneutilitrack.R
import com.example.capstoneutilitrack.components.CustomInputField
import com.example.capstoneutilitrack.ui.navigation.BottomNavBar


@Composable
fun AddNewCardScreen(navController: NavController) {
    Column(modifier = Modifier.background(colorResource(R.color.creamex))) {
        Box(modifier = Modifier.weight(1f)) {
            Content(navController = navController)
        }
        BottomNavBar(navController = navController)
    }
}

@Composable
fun Content(
    navController: NavController
) {
    var name by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var expiry by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val viewModel: PaymentViewModel = hiltViewModel()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Icon(
                    Icons.Default.ArrowBackIos,
                    contentDescription = null,
                    tint = colorResource(R.color.burgundy).copy(0.6f),
                    modifier = Modifier.clickable { navController.popBackStack() }
                )

                Spacer(Modifier.width(12.dp))

                Text(
                    "Add New Card",
                    fontWeight = FontWeight.Bold,
                    fontSize = 21.sp,
                    color = colorResource(R.color.burgundy),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        Divider(
            modifier = Modifier.padding(horizontal = 20.dp),
            thickness = 1.dp,
            color = Color.Gray.copy(0.2f)
        )
        Spacer(Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Security,
                contentDescription = null,
                Modifier.size(15.dp),
                tint = Color.Gray
            )
            Spacer(Modifier.width(6.dp))
            Text(
                "Your card information is encrypted and secure.",
                color = Color.DarkGray.copy(0.8f),
                fontSize = 13.sp
            )
        }

        Spacer(Modifier.height(10.dp))

        Image(
            painter = painterResource(id = R.drawable.add_card_bg),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .clip(RoundedCornerShape(20.dp))
        )
        Spacer(Modifier.height(20.dp))

        Text("Card Number", fontSize = 13.sp, color = Color.Gray)

        Spacer(Modifier.height(2.dp))

        CustomInputField(
            value = cardNumber,
            onValueChange = {
                if (it.length <= 19 && it.all { c -> c.isDigit() || c == ' ' }) {
                    cardNumber = it
                    errorMessage = null
                }
            },
            placeholder = "1234 5678 9012 3456",
            icon = Icons.Default.CreditCard
        )

        Spacer(Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {

                Column(modifier = Modifier.weight(1f)) {
                    Text("Expiration Date", fontSize = 13.sp, color = Color.Gray)
                    Spacer(Modifier.height(2.dp))

                    CustomInputField(
                        value = expiry,
                        onValueChange = {
                            if (it.length <= 7 && it.all { c -> c.isDigit() || c == ' ' || c == '/' }) {
                                expiry = it
                                errorMessage = null
                            }
                        },
                        placeholder = "MM / YY",
                        icon = Icons.Default.DateRange
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text("CVV", fontSize = 13.sp, color = Color.Gray)
                    Spacer(Modifier.height(2.dp))

                    CustomInputField(
                        value = cvv,
                        onValueChange = {
                            cvv = it.filter { c -> c.isDigit() }.take(3)
                            errorMessage = null
                        },
                        placeholder = "123",
                        icon = Icons.Default.Lock,
                        trailingIcon = {
                            Icon(
                                Icons.Default.VisibilityOff,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        Text("Cardholder Name", fontSize = 13.sp, color = Color.Gray)

        Spacer(Modifier.height(2.dp))

        CustomInputField(
            value = name,
            onValueChange = {
                name = it.filter { c -> c.isLetter() || c.isWhitespace() }
                errorMessage = null
            },
            placeholder = "Enter name as on card",
            icon = Icons.Default.Person
        )

        Spacer(Modifier.height(12.dp))
        Spacer(Modifier.weight(1f))

        errorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                fontSize = 13.sp,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xFFB25A5A),
                            Color(0xFF962D2B)
                        )
                    )
                )
                .clickable {
                    val cleanCard = cardNumber.replace(" ", "")
                    val cleanCvv = cvv.trim()
                    val cleanExpiry = expiry.replace(" ", "")

                    if (cleanCard.length != 16 || !cleanCard.all { it.isDigit() }) {
                        errorMessage = "Enter a valid 16-digit card number"
                        return@clickable
                    }

                    if (!isValidCardNumber(cleanCard)) {
                        errorMessage = "Invalid card number"
                        return@clickable
                    }

                    if (!Regex("""\d{2}/\d{2}""").matches(cleanExpiry)) {
                        errorMessage = "Use format MM/YY"
                        return@clickable
                    }

                    val parts = cleanExpiry.split("/")
                    val month = parts[0].toInt()
                    val year = 2000 + parts[1].toInt()

                    val now = java.time.LocalDate.now()

                    if (month !in 1..12) {
                        errorMessage = "Invalid month"
                        return@clickable
                    }

                    if (year < now.year || (year == now.year && month < now.monthValue)) {
                        errorMessage = "Card expired"
                        return@clickable
                    }

                    if (year > now.year + 15) {
                        errorMessage = "Invalid expiry year"
                        return@clickable
                    }

                    if (cleanCvv.length != 3) {
                        errorMessage = "Enter valid CVV"
                        return@clickable
                    }

                    if (name.trim().length < 3) {
                        errorMessage = "Enter valid name"
                        return@clickable
                    }
                    viewModel.addCard(
                        cardNumber = cleanCard,
                        holderName = name,
                        expMonth = month,
                        expYear = year,
                        onSuccess = { navController.popBackStack() },
                        onError = { errorMessage = it }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Save Card",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

fun isValidCardNumber(number: String): Boolean {
    var sum = 0
    var alternate = false

    for (i in number.length - 1 downTo 0) {
        var n = number[i].digitToInt()

        if (alternate) {
            n *= 2
            if (n > 9) n -= 9
        }

        sum += n
        alternate = !alternate
    }

    return sum % 10 == 0
}