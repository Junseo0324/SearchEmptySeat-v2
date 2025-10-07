package com.example.searchplacement.ui.user.category

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.searchplacement.data.store.StoreResponse
import com.example.searchplacement.di.AppModule
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.IconColor
import com.example.searchplacement.ui.theme.RatingColor
import com.example.searchplacement.ui.theme.RedPoint
import com.example.searchplacement.ui.theme.UserPrimaryColor
import com.example.searchplacement.ui.theme.White
import com.example.searchplacement.ui.theme.isOpenColor
import com.example.searchplacement.ui.utils.rememberImageLoaderWithToken
import com.google.accompanist.pager.ExperimentalPagerApi
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class)
@Composable
fun CategoryList(
    store: StoreResponse,
    onClick: () -> Unit
) {
    val imageLoader = rememberImageLoaderWithToken()
    val IMAGE_URL = "${AppModule.BASE_URL}/api/files/"
    val images = store.image.map { IMAGE_URL + it }

    val today = remember { LocalDate.now() }
    val todayDay = today.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREAN)
    val todayHours = store.businessHours[todayDay] ?: "영업시간 정보 없음"

    val isHoliday = store.regularHolidays?.get(todayDay) == 1
    val (_, statusText, badgeColor) = remember(todayHours,isHoliday) {
        val now = LocalTime.now()
        val parts = todayHours.split("-").map { it.trim() }

        if (isHoliday) {
            Triple(false, "휴무", IconColor)
        } else if (parts.size == 2) {
            try {
                val openTime = LocalTime.parse(parts[0])
                val closeTime = LocalTime.parse(parts[1])
                if (now.isAfter(openTime) && now.isBefore(closeTime)) {
                    Triple(true, "영업중", isOpenColor)
                } else {
                    Triple(false, "영업종료", IconColor)
                }
            } catch (e: Exception) {
                Triple(false, "정보없음", IconColor)
            }
        } else {
            Triple(false, "정보없음",IconColor)
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(Dimens.Medium),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.Nano)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                if (store.image.isNotEmpty()) {
                    val pagerState = rememberPagerState(pageCount = { store.image.size })

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
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
                                .fillMaxSize()
                                .aspectRatio(16f / 9f)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(Dimens.Default)
                            .background(
                                color = badgeColor,
                                shape = RoundedCornerShape(Dimens.Small)
                            )
                            .padding(horizontal = Dimens.Small, vertical = Dimens.Tiny)
                    ) {
                        Text(
                            text = statusText,
                            color = White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (store.image.size > 1) {
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(Dimens.Default),
                            horizontalArrangement = Arrangement.spacedBy(Dimens.Tiny)
                        ) {
                            repeat(store.image.size) { index ->
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .background(
                                            color = if (pagerState.currentPage == index)
                                                White
                                            else
                                                White.copy(alpha = 0.5f),
                                            shape = CircleShape
                                        )
                                )
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.Medium),
                verticalArrangement = Arrangement.spacedBy(Dimens.Small)
            ) {
                Text(
                    text = store.storeName,
                    style = AppTextStyle.BodyLarge.copy(fontSize = 18.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimens.Tiny),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = IconColor
                    )
                    Text(
                        text = store.location,
                        style = AppTextStyle.Body.copy(fontSize = 13.sp, color = IconColor),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    // TODO: 거리 계산 로직 추가
                    Text(
                        text = "• 0.9km",
                        fontSize = 13.sp,
                        color = UserPrimaryColor,
                        fontWeight = FontWeight.Medium
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimens.Default),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Dimens.Tiny),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = RatingColor,
                            modifier = Modifier.size(Dimens.Medium)
                        )
                        Text(
                            text = String.format("%.1f", store.averageRating),
                            style = AppTextStyle.Body.copy(fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Dimens.Tiny),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = RedPoint,
                            modifier = Modifier.size(Dimens.Medium)
                        )
                        Text(
                            text = store.favoriteCount.toString(),
                            style = AppTextStyle.Body.copy(fontSize = 14.sp)
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Dimens.Tiny),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Visibility,
                            contentDescription = null,
                            tint = IconColor,
                            modifier = Modifier.size(Dimens.Medium)
                        )
                        Text(
                            text = store.viewCount.toString(),
                            fontSize = 14.sp,
                        )
                    }
                }

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(store.category.take(3)) { category ->
                        Text(
                            text = category,
                            style = AppTextStyle.Body.copy(fontSize = 12.sp,
                                color = IconColor),
                            modifier = Modifier
                                .background(
                                    color = Color(0xFFF5F6FA),
                                    shape = RoundedCornerShape(Dimens.Default)
                                )
                                .padding(horizontal = Dimens.Small, vertical = Dimens.Tiny)
                        )
                    }
                }

                if (store.description.isNotEmpty()) {
                    Text(
                        text = store.description,
                        style = AppTextStyle.Body.copy(fontSize = 14.sp, color = IconColor),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

