package com.example.searchplacement.ui.user.store

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Black
import com.example.searchplacement.ui.theme.ButtonMainColor
import com.example.searchplacement.ui.theme.ChipBorderColor
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.IconTextColor
import com.example.searchplacement.ui.theme.White


@Composable
fun TimeSlotItem(time: String, status: String, seats: String, isAvailable: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens.Default),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = time,
                style = AppTextStyle.Body.copy(fontWeight = FontWeight.Bold, color = Black)
            )

            Box(
                modifier = Modifier
                    .background(
                        color = if (isAvailable) ButtonMainColor else ChipBorderColor,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = Dimens.Tiny)
            ) {
                Text(
                    text = status,
                    style = AppTextStyle.Body.copy(color = White, fontSize = 12.sp)
                )
            }
        }
        Text(
            text = seats,
            style = AppTextStyle.Body.copy(fontSize = 14.sp, color = IconTextColor)
        )
    }
}
