package com.example.searchplacement.ui.user.category

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.searchplacement.ui.theme.AppButtonStyle
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Black
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.UserPrimaryColor
import com.example.searchplacement.ui.theme.White

@Composable
fun SortByStore(str: String, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        modifier =
            Modifier
                .padding(Dimens.Tiny)
                .clickable(onClick = onClick),
        shape = AppButtonStyle.RoundedShape,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) UserPrimaryColor else Color.Transparent,
            contentColor = Black,
            disabledContentColor = Black, disabledContainerColor = Color.Transparent
        ),
    ) {
        Text(
            text = when (str) {
                "distance" -> "거리순"
                "reservation" -> "예약순"
                "favorite" -> "찜순"
                "review" -> "별점순"
                else -> "기본순"
            }, modifier = Modifier.padding(Dimens.Small),
            color = if (isSelected) White else Black,
            style = AppTextStyle.Body
        )

    }

}