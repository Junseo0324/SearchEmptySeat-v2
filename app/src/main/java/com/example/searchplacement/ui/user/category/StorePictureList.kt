package com.example.searchplacement.ui.user.category

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.example.searchplacement.di.AppModule
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Dimens

@Composable
fun StorePictureList(images: List<String>, imageLoader: ImageLoader) {
    val IMAGE_URL = "${AppModule.BASE_URL}/api/files/"
    LazyRow {
        items(images) { url ->
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(IMAGE_URL + url)
                    .crossfade(true)
                    .build(),
                imageLoader = imageLoader,
                contentDescription = "매장 이미지",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(Dimens.Tiny)
                    .size(80.dp)
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