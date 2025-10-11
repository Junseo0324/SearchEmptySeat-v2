package com.example.searchplacement.ui.user.reserve.store

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.searchplacement.data.menu.MenuResponse
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.CategoryTextColor
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.IconColor
import com.example.searchplacement.ui.theme.IconTextColor
import com.example.searchplacement.ui.theme.StoreTabBackgroundColor
import com.example.searchplacement.ui.theme.reservationCountColor


@Composable
fun MenuItemCard(
    menu: MenuResponse,
    quantity: Int,
    onQuantityChanged: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.Default),
        colors = CardDefaults.cardColors(containerColor = StoreTabBackgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.Medium),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(Dimens.Tiny)
            ) {
                Text(
                    text = menu.name,
                    style = AppTextStyle.Body.copy(fontWeight = FontWeight.Bold, color = IconTextColor)
                )
                Text(
                    text = "${String.format("%,d", menu.price)}원",
                    style = AppTextStyle.Body.copy(fontSize = 15.sp, fontWeight = FontWeight.Bold, color = reservationCountColor)
                )
                if (menu.description.isNotBlank()) {
                    Text(
                        text = menu.description,
                        style = AppTextStyle.Body.copy(fontSize = 12.sp, color = CategoryTextColor),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens.Default),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { if (quantity > 0) onQuantityChanged(quantity - 1) },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        Icons.Default.Remove,
                        contentDescription = null,
                        tint = IconColor
                    )
                }

                Text(
                    text = quantity.toString(),
                    style = AppTextStyle.Body.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = IconTextColor
                    ),
                    modifier = Modifier.widthIn(min = Dimens.Large),
                    textAlign = TextAlign.Center
                )

                IconButton(
                    onClick = { onQuantityChanged(quantity + 1) },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = null,
                        tint = reservationCountColor
                    )
                }
            }
        }
    }
}