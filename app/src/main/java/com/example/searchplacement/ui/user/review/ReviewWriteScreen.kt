package com.example.searchplacement.ui.user.review

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.viewmodel.ReviewViewModel
import com.example.searchplacement.viewmodel.StoreViewModel

@Composable
fun ReviewWriteScreen(
    navController: NavHostController,
    storePK: Long,
    storeViewModel: StoreViewModel
) {
    val viewModel: ReviewViewModel = hiltViewModel()
    val context = LocalContext.current

    var storeData = storeViewModel.storeData.collectAsState().value?.data


    LaunchedEffect(storePK) {
        storeViewModel.getStoreData(storePK)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(Dimens.Small)
    ) {
        if (storeData != null) {
            StoreHeader(
                storeData = storeData,
            )
        }
        StoreReviewContent() { rating, content, imageUris ->
            viewModel.submitReview(
                context = context,
                storePK = storePK,
                rating = rating,
                content = content,
                imageUris = imageUris,
                onSuccess = {
                    Toast.makeText(context, "리뷰가 등록되었습니다!", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                },
                onError = {
                    Toast.makeText(context, "리뷰 등록 실패: $it", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}
