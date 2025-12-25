package com.example.searchplacement.presentation.user.reserve.store

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.searchplacement.presentation.theme.Dimens
import com.example.searchplacement.presentation.theme.IconColor
import com.example.searchplacement.presentation.theme.IconTextColor
import com.example.searchplacement.presentation.theme.StoreTabBackgroundColor
import com.example.searchplacement.presentation.theme.TableOptionColor
import com.example.searchplacement.presentation.theme.reservationCountColor


@Composable
fun PaymentMethodButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(Dimens.Default),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) TableOptionColor else StoreTabBackgroundColor
        ),
        border = if (isSelected) BorderStroke(Dimens.Nano, reservationCountColor) else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.Medium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.Small)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) reservationCountColor else IconColor,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) reservationCountColor else IconTextColor
            )
        }
    }
}