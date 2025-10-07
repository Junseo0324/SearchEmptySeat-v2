package com.example.searchplacement.ui.owner.review

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.user.store.review.ReviewItem
import com.example.searchplacement.viewmodel.ReviewViewModel
import com.example.searchplacement.viewmodel.StoreListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerReviewScreen(navController: NavHostController,storeListViewModel: StoreListViewModel) {
    val viewModel: ReviewViewModel = hiltViewModel()
    val reviews = viewModel.reviews.collectAsState().value

    LaunchedEffect(Unit) {
        val storeId = storeListViewModel.selectedStore.value?.storePK
        if (storeId != null) {
            viewModel.getReviewsByStore(storeId)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {Text("리뷰 내역")},
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "뒤로 가기"
                        )
                    }
                },
            )
        }

    ) { padding ->
        Column(
            Modifier
                .fillMaxWidth()
                .padding(padding)
                .padding(Dimens.Small)
        ) {
            Text(
                text = "총 리뷰 수 ${reviews.size}",
                style = AppTextStyle.BodyLarge,
            )
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

}