package com.example.capstoneutilitrack.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.capstoneutilitrack.R



@Composable
fun AppButton(
    text: String,
    isLoading: Boolean,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 18.sp
) {
    Button(
        onClick = onClick,
        elevation = ButtonDefaults.buttonElevation(5.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp,0.dp)
            .height(66.dp),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(50),
        contentPadding = PaddingValues()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .shadow(12.dp, RoundedCornerShape(50))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF8FA8C0),
                            Color(0xFF48596B)
                        )
                    ),
                    shape = RoundedCornerShape(50)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text(text = text, color = colorResource(id = R.color.cream), fontSize = fontSize)
            }
        }
    }
}
