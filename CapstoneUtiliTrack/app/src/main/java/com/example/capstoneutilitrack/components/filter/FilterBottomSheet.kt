package com.example.capstoneutilitrack.components.filter

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capstoneutilitrack.R
import com.example.capstoneutilitrack.components.iconColor
import com.example.capstoneutilitrack.components.outlinedIcon
import com.example.capstoneutilitrack.model.FilterState
import com.example.capstoneutilitrack.model.MeterType
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(
    ExperimentalLayoutApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun FilterBottomSheet(
    currentFilter: FilterState,
    onDismiss: () -> Unit,
    onApply: (String, String, String) -> Unit
){
    val context = LocalContext.current

    var selectedType by remember(currentFilter) { mutableStateOf(currentFilter.type) }
    var startDate by remember(currentFilter) { mutableStateOf(currentFilter.startDate) }
    var endDate by remember(currentFilter) { mutableStateOf(currentFilter.endDate) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = colorResource(R.color.card),
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Text(
                "Filter",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("  Utility Type", color = colorResource(R.color.burgundy))

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colorResource(R.color.cream)),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column {

                    listOf("All", "Electricity", "Water", "Gas", "Internet", "Phone").forEachIndexed { index, type ->

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    if (selectedType == type)
                                        Color(0xFFDCD6D2)
                                    else
                                        Color.Transparent,
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .clickable { selectedType = type }
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .background(Color(0xFFE6E3DE), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                when (type) {
                                    "All" -> {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = Color(0xFF6C7A89),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }

                                    else -> {
                                        val meter = type.toMeterTypeOrNull()

                                        meter?.let {
                                            Icon(
                                                painter = it.outlinedIcon(),
                                                contentDescription = null,
                                                tint = it.iconColor(),
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = type,
                                modifier = Modifier.weight(1f),
                                fontSize = 16.sp
                            )

                            // 🔹 Custom Radio (lighter)
                            RadioButton(
                                selected = selectedType == type,
                                onClick = { selectedType = type },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Color(0xFF6C7A89),
                                    unselectedColor = Color.Gray.copy(alpha = 0.6f)
                                )
                            )
                        }

                        if (index != 3) {
                            Divider(
                                color = Color.Gray.copy(alpha = 0.2f),
                                thickness = 1.dp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Date Range", color = colorResource(R.color.burgundy))

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colorResource(R.color.creamex)),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val context = LocalContext.current

                        DateInputBox(
                            label = "Start Date",
                            value = startDate.ifEmpty { "Start Date  " },
                            onClick = {
                                openDatePicker(context) {
                                    startDate = it
                                }
                            }
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text("—", color = Color.Gray)

                        Spacer(modifier = Modifier.width(8.dp))

                        DateInputBox(
                            label = "End Date",
                            value = endDate.ifEmpty { "End Date  " },
                            onClick = {
                                openDatePicker(context) {
                                    endDate = it
                                }
                            }
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.height(20.dp))

            // 🔹 Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        selectedType = "All"
                        startDate = ""
                        endDate = ""

                        onApply("All", "", "")
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Reset")
                }

                Button(
                    onClick = {
                        onApply(selectedType, startDate, endDate)
                        onDismiss()
                    },
                    shape = RoundedCornerShape(50),
                    contentPadding = PaddingValues(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFF8FA8C0),
                                        Color(0xFF48596B)
                                    )
                                ),
                                shape = RoundedCornerShape(50)
                            )
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Apply Filter", color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

fun openDatePicker(
    context: Context,
    onDateSelected: (String) -> Unit
) {
    val calendar = Calendar.getInstance()

    DatePickerDialog(
        context,
        { _, year, month, day ->
            val date = Calendar.getInstance().apply {
                set(year, month, day)
            }.time

            val formatted = SimpleDateFormat("MMM d, yyyy", Locale.US)
                .format(date)

            onDateSelected(formatted)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
}

fun String.toMeterTypeOrNull(): MeterType? {
    return when (this) {
        "Electricity" -> MeterType.ELECTRICITY
        "Water" -> MeterType.WATER
        "Gas" -> MeterType.GAS
        "Internet" -> MeterType.INTERNET
        "Phone" -> MeterType.PHONE
        else -> null
    }
}