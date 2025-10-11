package com.example.searchplacement.ui.user.reserve.store

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.CardBorderTransparentColor
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.IconTextColor
import com.example.searchplacement.ui.theme.RedPoint
import com.example.searchplacement.ui.theme.TableOptionColor
import com.example.searchplacement.ui.theme.ViewCountColor
import com.example.searchplacement.ui.theme.White
import com.example.searchplacement.ui.theme.isOpenColor
import com.example.searchplacement.ui.theme.reservationCountColor


@Composable
fun TimeSlotCard(
    time: String,
    availableSeats: Int,
    isSelected: Boolean,
    isAvailable: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        !isAvailable -> CardBorderTransparentColor
        isSelected -> TableOptionColor
        else -> White
    }

    val textColor = when {
        !isAvailable -> ViewCountColor
        isSelected -> Color(0xFF1565C0)
        else -> IconTextColor
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = isAvailable, onClick = onClick),
        shape = RoundedCornerShape(Dimens.Default),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = if (isSelected) BorderStroke(Dimens.Nano, reservationCountColor) else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.Medium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.Tiny)
        ) {
            Text(
                text = time,
                style = AppTextStyle.Body.copy(fontWeight = FontWeight.Bold, color = textColor)
            )
            Text(
                text = if (isAvailable) "${availableSeats}석 남음" else "예약 불가",
                style = AppTextStyle.Body.copy(
                    fontSize = 12.sp,
                    color = if (isAvailable) isOpenColor else RedPoint
                )
            )
        }
    }
}