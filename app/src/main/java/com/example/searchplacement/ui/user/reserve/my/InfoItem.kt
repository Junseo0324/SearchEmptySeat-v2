package com.example.searchplacement.ui.user.reserve.my

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun InfoItem(
    label: String,
    value: String
) {
    Row {
        Text(
            text = label,
            fontSize = 11.sp,
            color = Color(0xFF7F8C8D)
        )
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2C3E50)
        )
    }
}