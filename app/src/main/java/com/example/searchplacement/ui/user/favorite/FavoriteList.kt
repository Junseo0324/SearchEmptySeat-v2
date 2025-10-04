package com.example.searchplacement.ui.user.favorite

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.searchplacement.R
import com.example.searchplacement.data.store.Store
import com.example.searchplacement.di.AppModule
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.CategoryBgColor
import com.example.searchplacement.ui.theme.CategoryTextColor
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.ViewCountColor
import com.example.searchplacement.ui.theme.White
import com.example.searchplacement.ui.utils.rememberImageLoaderWithToken
import com.example.searchplacement.viewmodel.FavoriteViewModel


@Composable
fun FavoriteList(
    store: Store,
    navController: NavHostController,
    favoriteViewModel: FavoriteViewModel
) {
    val imageUrls = store.image
    val imageLoader = rememberImageLoaderWithToken()
    val IMAGE_URL = "${AppModule.BASE_URL}/api/files/"
    val thumbnailUrl = IMAGE_URL + imageUrls.firstOrNull()

    val todayHours = store.businessHours.entries.firstOrNull()?.let {
        "${it.key}: ${it.value}"
    } ?: "운영시간 정보 없음"

    println("store data: $store")
    var isFavorite = remember { mutableStateOf(true) }

    val handleFavorite = {
        if (isFavorite.value) {
            // 즐겨찾기 삭제
            favoriteViewModel.removeFavorite(store.storePK)
        } else {
            // 즐겨찾기 추가
            favoriteViewModel.addFavorite(store.storePK)
        }
        isFavorite.value = !isFavorite.value
        favoriteViewModel.getFavoriteList()
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.Small)
            .clickable {
                navController.navigate("store/${store.storePK}")
            },
        shape = RoundedCornerShape(Dimens.Medium),
        colors = CardDefaults.cardColors(
            containerColor = White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.Nano)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(thumbnailUrl)
                        .crossfade(true)
                        .build(),
                    imageLoader = imageLoader,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.ic_setting),
                    modifier = Modifier.fillMaxSize(),
                )

                Image(
                    painter = painterResource(id = if (isFavorite.value) R.drawable.ic_favorite_fill else R.drawable.ic_favorite),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(Dimens.Small)
                        .size(40.dp)
                        .clickable {
                            handleFavorite()
                        }
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.Medium),
            verticalArrangement = Arrangement.spacedBy(Dimens.Small)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = store.storeName,
                    style = AppTextStyle.BodyLarge.copy(fontSize = 18.sp, color = Color(0xFF2C3E50)),
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "별점",
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(Dimens.Medium)
                    )
                    Text(
                        text = String.format("%.1f", store.averageRating),
                        style = AppTextStyle.Button.copy(color = Color(0xFF2C3E50))
                    )
                    Text(
                        text = "(${store.favoriteCount})",
                        style = AppTextStyle.BodyGray.copy(color = CategoryTextColor, fontWeight = FontWeight.Normal)
                    )
                }
            }

            if (store.category.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .background(
                            color = Color(0xFFE3F2FD),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = Dimens.Small, vertical = Dimens.Tiny)
                ) {
                    Text(
                        text = store.category.firstOrNull() ?: "",
                        style = AppTextStyle.BodySmall.copy(color = Color(0xFF1565C0), fontWeight = FontWeight.Medium)
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "위치",
                    tint = CategoryTextColor,
                    modifier = Modifier.size(Dimens.Medium)
                )
                Text(
                    text = store.location,
                    fontSize = 13.sp,
                    color = CategoryTextColor,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = "시간",
                    tint = CategoryTextColor,
                    modifier = Modifier.size(Dimens.Medium)
                )
                Text(
                    text = todayHours,
                    fontSize = 13.sp,
                    color = CategoryTextColor
                )
            }

            if (store.description.isNotEmpty()) {
                Text(
                    text = store.description,
                    style = AppTextStyle.BodySmall.copy(fontSize = 13.sp, color = CategoryTextColor),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                store.category.take(3).forEach { category ->
                    Text(
                        text = "#$category",
                        style = AppTextStyle.BodySmall.copy(color = CategoryTextColor),
                        modifier = Modifier
                            .background(
                                color = CategoryBgColor,
                                shape = RoundedCornerShape(Dimens.Default)
                            )
                            .padding(horizontal = Dimens.Small, vertical = Dimens.Tiny)
                    )
                }
                if (store.category.size > 3) {
                    Text(
                        text = "+${store.category.size - 3}",
                        style = AppTextStyle.BodySmall.copy(color = CategoryTextColor),
                        modifier = Modifier
                            .background(
                                color = CategoryBgColor,
                                shape = RoundedCornerShape(Dimens.Default)
                            )
                            .padding(horizontal = Dimens.Small, vertical = Dimens.Tiny)
                    )
                }
            }

            if (store.viewCount > 0) {
                Text(
                    text = "조회수 ${store.viewCount}",
                    style = AppTextStyle.BodySmall.copy(color = ViewCountColor, fontSize = 11.sp),
                )
            }
        }

    }
}