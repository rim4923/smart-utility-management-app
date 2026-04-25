package com.example.capstoneutilitrack.components.filter

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DateInputBox(
    label: String,
    value: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(
                Color(0xFFEDE7E3),
                RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {

        Text(
            text = label,
            fontSize = 11.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(2.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = Color.Gray.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(5.dp),
                )
                .padding(vertical = 2.dp, horizontal = 7.dp)
        ) {

            Text(
                text = value,
                fontSize = 14.sp
            )

            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(15.dp)
            )
        }
    }
}