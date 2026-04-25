package com.example.capstoneutilitrack.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.example.capstoneutilitrack.R
import com.example.capstoneutilitrack.model.MeterType
import com.example.capstoneutilitrack.model.UtilityModelDto
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MeterType.outlinedIcon(): Painter = when (this) {
    MeterType.WATER -> painterResource(id = R.drawable.ic_water_outlined)
    MeterType.ELECTRICITY -> painterResource(id = R.drawable.ic_bolt_outlined)
    MeterType.INTERNET -> painterResource(id = R.drawable.ic_wifi_outlined)
    MeterType.PHONE -> painterResource(id = R.drawable.ic_phone_outlined)
    MeterType.GAS -> painterResource(id = R.drawable.ic_fire_outlined)
}

fun MeterType.iconColor(): Color = when (this) {
    MeterType.WATER -> Color(0xFF27699F)
    MeterType.ELECTRICITY -> Color(0xFF8A6E1B)
    MeterType.INTERNET -> Color(0xFF351375)
    MeterType.PHONE -> Color(0xFF016505)
    MeterType.GAS -> Color(0xFFA63712)
}

fun MeterType.backgroundColor(): Color = when (this) {
    MeterType.WATER -> Color(0xDC4B8EAB)
    MeterType.ELECTRICITY -> Color(0x5E725D15)
    MeterType.INTERNET -> Color(0xD37253C7)
    MeterType.PHONE -> Color(0x774CAF50)
    MeterType.GAS -> Color(0xCEBD6E56)
}


fun UtilityModelDto.currencySymbol(): String = when (currency.uppercase()) {
    "USD" -> "$"
    "EUR" -> "€"
    else -> currency
}

fun UtilityModelDto.formattedDate(): String {
    val sdf = SimpleDateFormat("dd MMM", Locale.US)
    return sdf.format(nextBillDate)
}
