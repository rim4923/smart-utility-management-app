package com.example.capstoneutilitrack.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capstoneutilitrack.R

@Composable
fun CustomInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    trailingIcon: (@Composable (() -> Unit))? = null
) {
    Box(
        modifier = modifier
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
                modifier = Modifier.size(18.dp)
            )

            Spacer(Modifier.width(10.dp))

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                readOnly = readOnly,
                modifier = Modifier.weight(1f),
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Black
                ),
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(
                            placeholder,
                            color = Color.Gray.copy(alpha = 0.6f),
                            fontSize = 14.sp
                        )
                    }
                    innerTextField()
                }
            )

            trailingIcon?.invoke()
        }
    }
}