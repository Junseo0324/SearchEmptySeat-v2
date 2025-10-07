package com.example.searchplacement.ui.user.store

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.user.store.review.EmptyReviewSection
import com.example.searchplacement.ui.user.store.review.ReviewCard
import com.example.searchplacement.ui.user.store.review.ReviewStatisticsSection
import com.example.searchplacement.viewmodel.ReviewViewModel

//임시 데이터 클래스
data class ReviewStatistics(
    val averageRating: Double,
    val totalReviews: Int,
    val ratingDistribution: Map<Int, Int>
)


@Composable
fun StoreReview(
    storeId: Long,
) {
    val viewModel: ReviewViewModel = hiltViewModel()
    val reviews = viewModel.reviews.collectAsState().value

    val statistics = ReviewStatistics(
        averageRating = 4.7,
        totalReviews = 215,
        ratingDistribution = mapOf(
            5 to 120,
            4 to 65,
            3 to 20,
            2 to 8,
            1 to 2
        )
    )

    LaunchedEffect(Unit) {
        viewModel.getReviewsByStore(storeId)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF8F9FA))
    ) {

        ReviewStatisticsSection(statistics)

        if (reviews.isEmpty()) {
            EmptyReviewSection()
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = Dimens.Medium, vertical = Dimens.Small),
                verticalArrangement = Arrangement.spacedBy(Dimens.Default),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp)
            ) {
                items(reviews) { review ->
                    ReviewCard(review = review)
                }
            }
        }
    }
}
