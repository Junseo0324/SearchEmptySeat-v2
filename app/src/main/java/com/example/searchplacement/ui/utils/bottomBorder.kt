package com.example.searchplacement.ui.utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

fun Modifier.bottomBorder(
    color: Color,
    heightDp: Float = 1f,
): Modifier = drawWithContent {
    drawContent()
    val strokeWidth = heightDp.dp.toPx()
    drawLine(
        color = color,
        start = Offset(0f, size.height),
        end = Offset(size.width, size.height),
        strokeWidth = strokeWidth
    )
}
