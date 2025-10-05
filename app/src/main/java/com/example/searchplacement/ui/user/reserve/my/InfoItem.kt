package com.example.searchplacement.ui.user.reserve.my

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Black
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.IconColor

@Composable
fun InfoItem(
    label: String,
    value: String
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens.Small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = AppTextStyle.BodySmall.copy(fontWeight = FontWeight.Bold, color = IconColor),
        )
        Text(
            text = value,
            style = AppTextStyle.BodySmall.copy(fontWeight = FontWeight.Normal, fontSize = 13.sp, color = Black)
        )
    }
}