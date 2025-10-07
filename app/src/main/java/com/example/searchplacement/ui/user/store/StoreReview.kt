package com.example.searchplacement.ui.user.store

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.UserPrimaryColor
import com.example.searchplacement.viewmodel.ReviewViewModel


@Composable
fun StoreReview(storeId: Long) {
    val viewModel: ReviewViewModel = hiltViewModel()
    val reviews = viewModel.reviews.collectAsState().value
    LaunchedEffect(Unit) {
        viewModel.getReviewsByStore(storeId)
    }
    Column(
        Modifier
            .fillMaxWidth()
            .padding(Dimens.Small)
    ) {
        Row(
            Modifier.fillMaxWidth().padding(Dimens.Small),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "리뷰", style = AppTextStyle.BodyLarge)
            Text(
                text = "${reviews.size}",
                style = AppTextStyle.BodyLarge.copy(color = UserPrimaryColor),
            )
        }
        HorizontalDivider()
        LazyColumn(
            Modifier
                .fillMaxWidth().heightIn(max = 400.dp)
        ) {
            items(reviews.size) { index ->
                val review = reviews[index]
                ReviewItem(review)

                if (index < reviews.lastIndex) {
                    HorizontalDivider()
                }
            }
        }

    }
}
