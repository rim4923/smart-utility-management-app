package com.example.capstoneutilitrack.ui.payment

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.capstoneutilitrack.R
import com.example.capstoneutilitrack.Screen
import com.example.capstoneutilitrack.components.dashedBorder
import com.example.capstoneutilitrack.model.PaymentMode
import com.example.capstoneutilitrack.ui.navigation.BottomNavBar

@Composable
fun PaymentScreen(navController: NavController,
                  viewModel: PaymentViewModel,
                  mode: PaymentMode) {
    Column(modifier = Modifier.background(colorResource(R.color.creamex))) {
        Box(modifier = Modifier.weight(1f)) {
            MContent(navController = navController, viewModel, mode)
        }
        BottomNavBar(navController = navController)
    }
}


@Composable
fun MContent(
    navController: NavController,
    viewModel: PaymentViewModel,
    mode: PaymentMode
) {
    LaunchedEffect(Unit) {
        viewModel.loadCards()
    }
    val context = LocalContext.current

    LaunchedEffect(viewModel.errorMessage) {
        viewModel.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }
    val selectedCard = viewModel.selectedCard

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Spacer(Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Icon(
                    Icons.Default.ArrowBackIos,
                    contentDescription = null,
                    tint = colorResource(R.color.burgundy).copy(0.6f),
                    modifier = Modifier.clickable {
                        if (mode == PaymentMode.PROFILE)
                            navController.popBackStack()
                        else
                            navController.navigate(Screen.Bills.route)
                    }
                )

                Spacer(Modifier.width(12.dp))

                Text(
                    if (mode == PaymentMode.PROFILE) "Your Cards" else "Payment",
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

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text( if (mode == PaymentMode.PROFILE) "Your cards" else "Select your card", fontSize = 18.sp, fontWeight = FontWeight.Bold)

            Text(
                "+ Add a new card",
                color = colorResource(id = R.color.steel_blue),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable {
                    navController.navigate("add_card")
                }
            )
        }

        Spacer(Modifier.height(14.dp))
        if (viewModel.cards.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 26.dp)
                    .weight(1f)
                    .dashedBorder(
                        color = Color.Gray.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {

                    Spacer(Modifier.height(40.dp))
                    Image(
                        painter = painterResource(id = R.drawable.ic_no_card),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth()
                        .weight(1f)
                    )

                    Text(
                        "No cards yet",
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.burgundy),
                        fontSize = 19.sp
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        "Add a card to make secure\npayments for your bills.",
                        color = Color.DarkGray.copy(0.8f),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(25.dp))

                    Box(
                        modifier = Modifier
                            .height(45.dp)
                            .clip(RoundedCornerShape(30.dp))
                            .background(
                                brush = Brush.horizontalGradient(
                                    listOf(
                                        colorResource(R.color.steel_blue),
                                        colorResource(R.color.deep_blue),
                                        colorResource(R.color.deep_blue)
                                    )
                                )
                            )
                            .clickable {
                                navController.navigate("add_card")
                            }
                            .padding(horizontal = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+ Add a new card",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
            Spacer(Modifier.height(25.dp))
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                val listState = rememberLazyListState()
                val density = LocalDensity.current
                val flingBehavior = rememberSnapFlingBehavior(listState)
                LaunchedEffect(
                    listState.firstVisibleItemIndex,
                    listState.firstVisibleItemScrollOffset,
                    viewModel.cards.size
                ) {
                    if (viewModel.cards.isEmpty()) return@LaunchedEffect

                    val itemWidthPx = with(density) {
                        320.dp.toPx() + 16.dp.toPx()
                    }

                    val offset = listState.firstVisibleItemScrollOffset.toFloat()

                    val centerIndex =
                        if (offset > itemWidthPx / 2)
                            listState.firstVisibleItemIndex + 1
                        else
                            listState.firstVisibleItemIndex

                    if (centerIndex < viewModel.cards.size) {
                        viewModel.selectCard(viewModel.cards[centerIndex])
                    }
                }
                LaunchedEffect(viewModel.cards) {
                    if (viewModel.cards.isNotEmpty()) {
                        viewModel.selectCard(viewModel.cards.first())
                    }
                }

                LazyRow(
                    state = listState,
                    flingBehavior = flingBehavior,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {

                    items(viewModel.cards.size) { i ->
                        val card = viewModel.cards[i]

                        Box(
                            modifier = Modifier
                                .width(320.dp)
                                .height(190.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.credit_card),
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp)
                                    .clip(RoundedCornerShape(20.dp))
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(20.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Spacer(Modifier.height(75.dp))
                                Text(
                                    text = "**** **** **** ${card.last4}",
                                    color = Color.White.copy(0.7f),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 2.sp
                                )

                                Spacer(Modifier.height(30.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth()
                                ) {

                                    Text(
                                        card.holderName,
                                        color = Color.White.copy(0.8f),
                                        fontSize = 15.sp
                                    )

                                    Spacer(Modifier.width(100.dp))
                                    Text(
                                        "%02d/%02d".format(card.expMonth, card.expYear % 100),
                                        color = Color.White.copy(0.8f),
                                        fontSize = 15.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(viewModel.cards.size) { index ->
                    val isSelected = viewModel.cards[index] == viewModel.selectedCard

                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(if (isSelected) 10.dp else 8.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) Color.DarkGray else Color.LightGray
                            )
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            selectedCard?.let { card ->
                CardInfoBox(
                    label = "Card Number",
                    value = "**** **** **** ${card.last4}",
                    icon = Icons.Default.CreditCard,
                    modifier = Modifier.padding(horizontal = 14.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CardInfoBox(
                        label = "Expiration Date",
                        value = "${card.expMonth}/${card.expYear.toString().takeLast(2)}",
                        icon = Icons.Default.DateRange,
                        modifier = Modifier.weight(1f)
                    )

                    CardInfoBox(
                        label = "CVV",
                        value = "***",
                        icon = Icons.Default.Lock,
                        modifier = Modifier.weight(1f)
                    )
                }

                CardInfoBox(
                    label = "Card Holder Name",
                    value = card.holderName,
                    icon = Icons.Default.Person,
                    modifier = Modifier.padding(horizontal = 14.dp)
                )
            }

            Spacer(Modifier.weight(1f))

            if (mode == PaymentMode.FULL) {
                Box(
                    modifier = Modifier
                        .height(55.dp)
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(30.dp))
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(
                                    colorResource(R.color.steel_blue),
                                    colorResource(R.color.deep_blue),
                                    colorResource(R.color.deep_blue)
                                )
                            )
                        )
                        .clickable {
                            navController.navigate("confirm_payment")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text("Choose Card", color = Color.White)
                }
            }

            Spacer(Modifier.height(13.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.Lock,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )

                Spacer(Modifier.width(6.dp))

                Text(
                    "Your payment information is encrypted and secure.",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun CardInfoBox(
    label: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    trailingIcon: (@Composable (() -> Unit))? = null
) {
    Column(modifier = modifier.padding(vertical = 8.dp)) {

        Text(
            label,
            fontSize = 13.sp,
            color = Color.Gray
        )

        Spacer(Modifier.height(2.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .border(
                    width = 1.dp,
                    color = Color.Gray.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(12.dp)
                )
                .clip(RoundedCornerShape(12.dp))
                .padding(horizontal = 14.dp),
            contentAlignment = Alignment.CenterStart
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                Icon(
                    icon,
                    contentDescription = null,
                    tint = Color.DarkGray.copy(0.7f),
                    modifier = Modifier.size(20.dp)
                )

                Spacer(Modifier.width(10.dp))

                Text(
                    text = value,
                    fontSize = 14.sp,
                    color = Color.Black.copy(0.7f),
                    modifier = Modifier.weight(1f)
                )

                trailingIcon?.invoke()
            }
        }
    }
}

