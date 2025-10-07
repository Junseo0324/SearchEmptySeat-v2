package com.example.searchplacement.ui.user.store.review

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.example.searchplacement.data.review.ReviewResponse
import com.example.searchplacement.di.AppModule
import com.example.searchplacement.ui.theme.AppButtonStyle
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Black
import com.example.searchplacement.ui.theme.CardBorderTransparentColor
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.utils.rememberImageLoaderWithToken


@Composable
fun ReviewItem(review: ReviewResponse) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.Small),
        shape = AppButtonStyle.RoundedShape,
        border = BorderStroke(1.dp, CardBorderTransparentColor),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
            contentColor = Black
        )
    ) {
        Column(
            Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = review.userName, style = AppTextStyle.BodyLarge, modifier = Modifier.padding(Dimens.Small)
            )
            Text(
                text = "사진", style = AppTextStyle.Body.copy(fontWeight = FontWeight.Bold), modifier = Modifier.padding(Dimens.Small)
            )
            ReviewImages(images = review.image)

            Text(
                text = "리뷰", style = AppTextStyle.Body.copy(fontWeight = FontWeight.Bold), modifier = Modifier.padding(Dimens.Small)
            )

            Text(text = review.content, style = AppTextStyle.Body, modifier = Modifier.padding(
                Dimens.Small))

        }
    }
}


@Composable
fun ReviewImages(images: List<String>) {
    val IMAGE_URL = "${AppModule.BASE_URL}/api/files/"
    val imageLoader = rememberImageLoaderWithToken()
    LazyRow{
        items(images) { url ->
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(IMAGE_URL+ url).crossfade(true).build(),
                imageLoader = imageLoader,
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp).padding(Dimens.Tiny),
                contentScale = ContentScale.Crop
            ) {
                val state = painter.state
                when {
                    state is AsyncImagePainter.State.Loading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        }
                    }

                    state is AsyncImagePainter.State.Error -> {
                        // 에러 이미지나 아이콘
                        Text("불러오기 실패", style= AppTextStyle.Body)
                    }

                    else -> {
                        SubcomposeAsyncImageContent()
                    }
                }
            }

        }
    }
}