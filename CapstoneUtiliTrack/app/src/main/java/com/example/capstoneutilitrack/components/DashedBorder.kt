package com.example.capstoneutilitrack.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.drawscope.Stroke

fun Modifier.dashedBorder(
    color: Color,
    shape: Shape,
    strokeWidth: Dp = 2.dp,
    dashLength: Dp = 6.dp,
    gapLength: Dp = 4.dp,
    cap: StrokeCap = StrokeCap.Butt
): Modifier = this.drawWithContent {
    drawContent()

    val outline = shape.createOutline(size, layoutDirection, this)
    val stroke = Stroke(
        width = strokeWidth.toPx(),
        pathEffect = PathEffect.dashPathEffect(
            floatArrayOf(dashLength.toPx(), gapLength.toPx()), 0f
        ),
        cap = cap
    )

    drawOutline(
        outline = outline,
        style = stroke,
        brush = SolidColor(color)
    )
}
