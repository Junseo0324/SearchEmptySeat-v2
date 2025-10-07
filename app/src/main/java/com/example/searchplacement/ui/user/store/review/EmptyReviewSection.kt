package com.example.searchplacement.ui.user.store.review

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.IconColor
import com.example.searchplacement.ui.theme.ViewCountColor

@Composable
fun EmptyReviewSection() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.RateReview,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = ViewCountColor
        )
        Spacer(modifier = Modifier.height(Dimens.Medium))
        Text(
            text = "아직 작성된 리뷰가 없습니다",
            style = AppTextStyle.Body.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(Dimens.Small))
        Text(
            text = "첫 리뷰를 작성해보세요!",
            style = AppTextStyle.Body.copy(fontSize = 14.sp, color = IconColor)
        )
    }
}