package com.example.searchplacement.ui.user.reserve.store

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.CardBorderTransparentColor
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.IconColor
import com.example.searchplacement.ui.theme.IconTextColor
import com.example.searchplacement.ui.theme.TableOptionColor
import com.example.searchplacement.ui.theme.White
import com.example.searchplacement.ui.theme.reservationCountColor


data class TableOption(
    val name: String,
    val capacity: String,
    val isAvailable: Boolean,
    val id: String
)

@Composable
fun TableOptionCard(
    table: TableOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = table.isAvailable, onClick = onClick),
        shape = RoundedCornerShape(Dimens.Default),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) TableOptionColor else White
        ),
        border = if (isSelected) BorderStroke(Dimens.Nano, reservationCountColor) else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(Dimens.Tiny)
            ) {
                Text(
                    text = table.name,
                    style = AppTextStyle.Body.copy(
                        fontWeight = FontWeight.Bold,
                        color = IconTextColor
                    )
                )
                Text(
                    text = table.capacity,
                    style = AppTextStyle.Body.copy(fontSize = 14.sp, color = IconColor)
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens.Small),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            if (table.isAvailable) Color(0xFFD5EDDA) else CardBorderTransparentColor,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = if (table.isAvailable) "예약가능" else "예약불가",
                        style = AppTextStyle.Body.copy(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (table.isAvailable) Color(0xFF27AE60) else Color(0xFF95A5A6)
                        )
                    )
                }

                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = reservationCountColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}