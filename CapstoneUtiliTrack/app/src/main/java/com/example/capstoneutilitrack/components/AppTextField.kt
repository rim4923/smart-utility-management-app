package com.example.capstoneutilitrack.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capstoneutilitrack.R

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean = false,
    errorMessage: String? = null,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = true,
    leadingIcon: (@Composable (() -> Unit))? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = singleLine,
        modifier = modifier.fillMaxWidth()
            .height(70.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = keyboardType
        ),
        interactionSource = interactionSource,
        visualTransformation = visualTransformation,
        shape = RoundedCornerShape(14.dp),
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = if (isError) Color.Red else colorResource(R.color.deep_blue),
            unfocusedBorderColor = if (isError) Color.Red else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
            focusedLabelColor = if (isError) Color.Red else MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
            unfocusedLabelColor = if (isError) Color.Red else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
            cursorColor = if (isError) Color.Red else MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
            focusedTextColor = if (isError) Color.Red else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            unfocusedTextColor = if (isError) Color.Red else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

    )
    if (isError && !errorMessage.isNullOrBlank()) {
        Text(
            text = errorMessage,
            color = Color.Red,
            fontSize = 12.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        )
    }
}
