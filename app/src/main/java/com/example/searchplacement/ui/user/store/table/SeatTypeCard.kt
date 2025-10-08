package com.example.searchplacement.ui.user.store.table

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
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
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.IconColor

@Composable
fun SeatTypeCard(
    capacity: Int,
    totalSeats: Int,
    modifier: Modifier = Modifier
) {
    val iconColor = when {
        capacity <= 2 -> Color(0xFF3498DB)
        capacity <= 4 -> Color(0xFF27AE60)
        else -> Color(0xFF9B59B6)
    }

    val backgroundColor = when {
        capacity <= 2 -> Color(0xFFE3F2FD)
        capacity <= 4 -> Color(0xFFE8F5E9)
        else -> Color(0xFFF3E5F5)
    }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(Dimens.Default),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.Nano)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.Medium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.Default)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = backgroundColor,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(Dimens.Large)
                )
            }

            Text(
                text = "${capacity}인석",
                style = AppTextStyle.Body.copy(fontWeight = FontWeight.Bold)
            )

            Text(
                text = "${totalSeats}석",
                style = AppTextStyle.Body.copy(fontSize = 14.sp, color = IconColor)
            )
        }
    }
}