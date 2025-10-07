package com.example.searchplacement.ui.user.store.review

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarHalf
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
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.RatingColor
import com.example.searchplacement.ui.theme.White


@Composable
fun ReviewStatisticsSection(statistics: ReviewStatistics) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.Medium),
        shape = RoundedCornerShape(Dimens.Medium),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.Nano)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(Dimens.Medium)
        ) {
            Text(
                text = "총 ${statistics.totalReviews}개의 리뷰",
                style = AppTextStyle.Body.copy(fontWeight = FontWeight.Bold)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.Large),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Dimens.Small)
                ) {
                    Text(
                        text = String.format("%.1f", statistics.averageRating),
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Dimens.Nano)
                    ) {
                        repeat(5) { index ->
                            Icon(
                                imageVector = if (index < statistics.averageRating.toInt())
                                    Icons.Filled.Star
                                else if (index < statistics.averageRating)
                                    Icons.Default.StarHalf
                                else
                                    Icons.Default.StarBorder,
                                contentDescription = null,
                                tint = RatingColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    (5 downTo 1).forEach { rating ->
                        RatingDistributionBar(
                            rating = rating,
                            count = statistics.ratingDistribution[rating] ?: 0,
                            totalCount = statistics.totalReviews
                        )
                    }
                }
            }
        }
    }
}
