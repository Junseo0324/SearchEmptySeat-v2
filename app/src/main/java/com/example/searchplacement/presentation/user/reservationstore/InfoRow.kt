package com.example.searchplacement.presentation.user.reservationstore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.searchplacement.presentation.theme.AppTextStyle
import com.example.searchplacement.presentation.theme.IconColor
import com.example.searchplacement.presentation.theme.IconTextColor


@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = AppTextStyle.Body.copy(fontSize = 14.sp, color = IconColor)
        )
        Text(
            text = value,
            style = AppTextStyle.Body.copy(fontSize = 14.sp, color = IconTextColor, fontWeight = FontWeight.Bold)
        )
    }
}

