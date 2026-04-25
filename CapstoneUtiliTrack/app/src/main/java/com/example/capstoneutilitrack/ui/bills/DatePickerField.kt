package com.example.capstoneutilitrack.ui.bills

import android.app.DatePickerDialog
import android.content.Context
import android.view.ContextThemeWrapper
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capstoneutilitrack.R
import java.util.Calendar


@Composable
fun DatePickerField(
    context: Context,
    label: String,
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    val calendar = Calendar.getInstance()
    val themedContext = ContextThemeWrapper(
        context,
        R.style.CustomDatePicker
    )

    val datePicker = DatePickerDialog(
        themedContext,
        { _, year, month, dayOfMonth ->
            val formatted = "%02d-%02d-%d".format(dayOfMonth, month + 1, year)
            onDateSelected(formatted)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    OutlinedButton(
        onClick = { datePicker.show() },
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        modifier = Modifier
            .height(50.dp)
            .defaultMinSize(minWidth = 0.dp) // ensures uniform width if used side-by-side
    ) {
        Icon(
            imageVector = Icons.Default.DateRange,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = if (selectedDate.isBlank()) label else selectedDate,
            fontSize = 16.sp
        )
    }
}

