package com.example.searchplacement.ui.user.store.review

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.ChipBorderColor
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.RatingColor

@Composable
fun RatingDistributionBar(rating: Int, count: Int, totalCount: Int) {
    val percentage = if (totalCount > 0) (count.toFloat() / totalCount) else 0f

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Dimens.Small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = rating.toString(),
            style = AppTextStyle.Body.copy(fontSize = 13.sp),
            modifier = Modifier.width(Dimens.Default)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .height(Dimens.Small)
                .background(
                    color = ChipBorderColor,
                    shape = RoundedCornerShape(Dimens.Tiny)
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(percentage)
                    .background(
                        color = RatingColor,
                        shape = RoundedCornerShape(Dimens.Tiny)
                    )
            )
        }
    }
}