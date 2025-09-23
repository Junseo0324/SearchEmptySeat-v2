package com.example.searchplacement.ui.user.store

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.searchplacement.R
import com.example.searchplacement.data.store.StoreResponse
import com.example.searchplacement.di.AppModule
import com.example.searchplacement.ui.theme.AppButtonStyle
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Black
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.utils.rememberImageLoaderWithToken
import com.example.searchplacement.viewmodel.FavoriteViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.util.Locale

@Composable
fun StoreInformation(navController: NavHostController, storeData: StoreResponse, token: String,favoriteViewModel: FavoriteViewModel) {
    val imageLoader = rememberImageLoaderWithToken()
    val IMAGE_URL = "${AppModule.BASE_URL}/api/files/"
    val images = storeData.image.map { IMAGE_URL + it }

    val favoriteList by favoriteViewModel.favoriteList.collectAsState()

    val isFavorite = remember {
        mutableStateOf(
            favoriteList?.data?.any { it.store.storePK == storeData.storePK } ?: false
        )
    }

    val storeHours = storeData.businessHours

    val dayMap = mapOf(
        "월" to "월요일",
        "화" to "화요일",
        "수" to "수요일",
        "목" to "목요일",
        "금" to "금요일",
        "토" to "토요일",
        "일" to "일요일"
    )

    val todayShort = LocalDate.now().dayOfWeek.getDisplayName(java.time.format.TextStyle.SHORT, Locale.KOREAN)
    val today = dayMap[todayShort] ?: todayShort

    val storeHoursToday = storeHours[today]
    val currentTime = LocalTime.now()

    val statusText = if (storeHoursToday != null) {
        val times = storeHoursToday.split(" - ")
        if (times.size == 2) {
            val openTime = LocalTime.parse(times[0])
            val closeTime = LocalTime.parse(times[1])

            when {
                currentTime.isAfter(openTime.minusHours(1)) && currentTime.isBefore(openTime) -> "영업 준비 중"
                currentTime.isAfter(openTime) && currentTime.isBefore(closeTime) -> "영업 중"
                else -> "영업 종료"
            }
        } else {
            "영업 정보 없음"
        }
    } else {
        "영업 정보 없음"
    }

    val handleFavorite = {
        if (isFavorite.value) {
            // 즐겨찾기 삭제
            favoriteViewModel.removeFavorite(storeData.storePK)
        } else {
            // 즐겨찾기 추가
            favoriteViewModel.addFavorite(storeData.storePK)
        }
        isFavorite.value = !isFavorite.value
    }

    val expanded = remember { mutableStateOf(false) }
    val pagerState = rememberPagerState(pageCount = { images.size })

    Column(
        Modifier
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
                        .aspectRatio(16f / 9f)
                        .padding(6.dp)
                )
            }
            Text(
                text = "${pagerState.currentPage + 1} / ${images.size}",
                color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .background(Color.Black.copy(alpha = 0.3f), shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = Dimens.Small, vertical = Dimens.Tiny)
            )
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(Modifier.padding(Dimens.Small)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = storeData.storeName ?: "가게명 없음",
                        style = AppTextStyle.BodyLarge,
                        modifier = Modifier.padding(Dimens.Tiny)
                    )
                    Spacer(modifier = Modifier.width(Dimens.Small))
                    Text(
                        text = String.format("%.1f", storeData.averageRating),
                        style = AppTextStyle.redPoint,
                        modifier = Modifier.padding(Dimens.Small)
                    )
                }
                Text(
                    text = statusText,
                    style = AppTextStyle.Body,
                    modifier = Modifier.padding(start = Dimens.Tiny)
                )
            }
            Image(
                painter = painterResource(id = if (isFavorite.value) R.drawable.ic_favorite_fill else R.drawable.ic_favorite),
                contentDescription = null,
                modifier = Modifier
                    .padding(Dimens.Small)
                    .size(30.dp)
                    .clickable {
                        handleFavorite()
                    }
            )
        }
        ClickableText(
            text = AnnotatedString(
                if (expanded.value) {
                    storeHours.entries.joinToString("\n") { "${it.key} ${it.value}" }
                } else {
                    "$today ${storeHours[today]}"
                }
            ),
            style = AppTextStyle.Body,
            onClick = { expanded.value = !expanded.value },
            modifier = Modifier.padding(start = Dimens.Small).padding(start = Dimens.Tiny)
        )
        Row(
            Modifier.fillMaxWidth().padding(Dimens.Small),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val address = storeData.location ?: "주소 없음"
            Text(
                text = address,
                style = AppTextStyle.Caption,
                modifier = Modifier.padding(Dimens.Tiny)
            )

            Row(
                horizontalArrangement = Arrangement.Center,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_map),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = Dimens.Tiny)
                        .width(20.dp)
                        .height(20.dp)
                        .clickable {
                            navController.currentBackStackEntry?.savedStateHandle?.set("store", storeData)
                            navController.navigate("map_with_store")
                        })

                Text(
                    text = "위치",
                    style = AppTextStyle.Caption,
                    modifier = Modifier
                        .padding(3.dp)
                        .clickable {
                            navController.currentBackStackEntry?.savedStateHandle?.set("store", storeData)
                            navController.navigate("map_with_store")
                        }
                )
            }
        }

        Button(
            onClick = { navController.navigate("reserveStore") }, colors = ButtonColors(
                Color.Transparent, Black, Color.Transparent, Black
            ),
            border = BorderStroke(1.dp, Black),
            shape = AppButtonStyle.RoundedShape,
            modifier = Modifier
                .align(Alignment.End)
                .padding(Dimens.Small)
        ) {
            Text(text = "예약 하기")
        }

    }
}