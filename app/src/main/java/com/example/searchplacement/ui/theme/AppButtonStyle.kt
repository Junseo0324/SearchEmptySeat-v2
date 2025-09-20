package com.example.searchplacement.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object AppButtonStyle {

    @Composable
    fun main(): ButtonColors = ButtonDefaults.buttonColors(
        containerColor = ButtonMainColor,
        contentColor = White,
        disabledContainerColor = Color.DarkGray,
        disabledContentColor = White.copy(alpha = 0.5f)
    )

    @Composable
    fun outlined(): ButtonColors = ButtonDefaults.buttonColors(
        containerColor = Color.Transparent,
        contentColor = Color.Black,
        disabledContainerColor = Color.Transparent,
        disabledContentColor = Color.Black.copy(alpha = 0.4f)
    )

    val OutlinedBorder = BorderStroke(1.dp, Color.Black)
    val RoundedShape = RoundedCornerShape(10.dp)
}
