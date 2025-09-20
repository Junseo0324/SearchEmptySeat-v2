package com.example.searchplacement.ui.user.category

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Black
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.utils.bottomBorder

@Composable
fun CategoryList(str: String, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = if (isSelected) {
            Modifier
                .padding(Dimens.Tiny)
                .clickable(onClick = onClick)
                .bottomBorder(Black)
        } else
            Modifier
                .padding(Dimens.Tiny)
                .clickable(onClick = onClick),
        shape = RectangleShape,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
            contentColor = Black,
            disabledContentColor = Black, disabledContainerColor = Color.Transparent
        )
    ) {
        Text(
            text = str, modifier = Modifier.padding(Dimens.Small),
            style = AppTextStyle.Body
        )

    }

}