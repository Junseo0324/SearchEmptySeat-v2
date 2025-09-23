package com.example.searchplacement.ui.user.review

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.searchplacement.data.store.StoreResponse
import com.example.searchplacement.di.AppModule
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.utils.rememberImageLoaderWithToken


@Composable
fun StoreHeader(storeData: StoreResponse) {
    val imageLoader = rememberImageLoaderWithToken()
    val IMAGE_URL = "${AppModule.BASE_URL}/api/files/"
    val images = storeData.image.map { IMAGE_URL + it }
    Log.d("StoreHeader: ","$images")
    val pagerState = rememberPagerState(pageCount = { images.size })
    val storeName = storeData.storeName
    val storeAddress = storeData.location
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxWidth()
                ) { page ->
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(images[page])
                            .crossfade(true)
                            .build(),
                        imageLoader = imageLoader,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f),
                        onError = {
                        },
                    )
                }
                Text(
                    text = "${pagerState.currentPage + 1} / ${images.size}",
                    color = Color.White,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                        .background(
                            Color.Black.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = Dimens.Small, vertical = Dimens.Tiny)
                )
            }

        Text(text = storeName, style = AppTextStyle.BodyLarge)
        Spacer(modifier = Modifier.height(Dimens.Small))
        Text(text = storeAddress, style = AppTextStyle.Caption)
    }
}
