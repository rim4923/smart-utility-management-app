package com.example.capstoneutilitrack.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.capstoneutilitrack.R

@Composable
fun SectionHeader(
    title: String,
    showAll: Boolean,
    onToggle: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Medium,
                color = colorResource(R.color.burgundy)
            )

            Text(
                text = if (showAll) "Show less" else "See all",
                color = colorResource(R.color.deep_blue),
                modifier = Modifier.clickable { onToggle() }
            )
        }

        Spacer(Modifier.height(6.dp))

        Divider(color = Color.Gray.copy(alpha = 0.4f))
    }
}