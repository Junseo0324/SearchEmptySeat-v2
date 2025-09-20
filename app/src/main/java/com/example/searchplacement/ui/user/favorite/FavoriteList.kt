package com.example.searchplacement.ui.user.favorite

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
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
import com.example.searchplacement.ui.theme.Black
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.White
import com.example.searchplacement.ui.utils.rememberImageLoaderWithToken
import com.example.searchplacement.viewmodel.FavoriteViewModel


@Composable
fun FavoriteList(store: Store, navController: NavHostController,favoriteViewModel: FavoriteViewModel) {
    val token = favoriteViewModel.token
    val imageUrls = store.image
    val imageLoader = rememberImageLoaderWithToken(token)
    val IMAGE_URL = "${AppModule.BASE_URL}/api/files/"
    val thumbnailUrl = IMAGE_URL + imageUrls.firstOrNull()

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
            .padding(vertical = Dimens.Tiny, horizontal = Dimens.Small)
            .clickable { navController.navigate("store/${store.storePK}") },
//        elevation = CardDefaults.elevatedCardElevation(1.dp),
        colors = CardColors(
            contentColor = Black, containerColor = White,
            disabledContentColor = Black, disabledContainerColor = White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.Small),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
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
                modifier = Modifier
                    .size(60.dp)
                    .clip(RectangleShape)
            )


            Column(
                Modifier
                    .padding(10.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = store.storeName, style = AppTextStyle.Body.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(Dimens.Small))
                Text(text = store.location, style = AppTextStyle.Caption.copy(fontSize = 14.sp), maxLines = 1, overflow = TextOverflow.Ellipsis)
            }


            Image(
                painter = painterResource(id = if (isFavorite.value) R.drawable.ic_favorite_fill else R.drawable.ic_favorite),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        handleFavorite()
                    }
            )

        }
    }
}