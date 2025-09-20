package com.example.searchplacement.ui.user.category

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.searchplacement.R
import com.example.searchplacement.di.AppModule
import com.example.searchplacement.ui.theme.AppButtonStyle
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Black
import com.example.searchplacement.ui.theme.CardBorderTransparentColor
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.utils.rememberImageLoaderWithToken

@Composable
fun StoreList(
    storePk : Long,
    storeName: String,
    storeAddress: String,
    review: String,
    favoriteCount: String,
    imageUrls: List<String>,
    navController: NavHostController,
    token: String
) {
    val imageLoader = rememberImageLoaderWithToken(token)
    val IMAGE_URL = "${AppModule.BASE_URL}/api/files/"
    val thumbnailUrl = IMAGE_URL + imageUrls.firstOrNull()
    val remainingImages = if (imageUrls.size > 1) imageUrls.drop(1) else emptyList()


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.Tiny)
            .clickable {
                navController.navigate("store/$storePk")
            },
        shape = AppButtonStyle.RoundedShape,
        border = BorderStroke(1.dp, CardBorderTransparentColor),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
            contentColor = Black
        )
    ) {
        Column(Modifier.padding(Dimens.Small)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(thumbnailUrl)
                        .crossfade(true)
                        .build(),
                    imageLoader = imageLoader,
                    contentDescription = "매장 대표 이미지",
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.ic_store),
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RectangleShape)
                        .align(Alignment.Top)
                )
                Spacer(modifier = Modifier.width(Dimens.Small))

                Column {
                    Row {
                        Text(text = storeName, style = AppTextStyle.BodyLarge, modifier = Modifier.padding(Dimens.Tiny))
                        Text(text = review, modifier = Modifier.fillMaxWidth().padding(Dimens.Tiny), style = AppTextStyle.redPoint, textAlign = TextAlign.End)
                    }
                    Text(
                        text = storeAddress,
                        modifier = Modifier.fillMaxWidth().padding(Dimens.Tiny),
                        style = AppTextStyle.Caption.copy(fontSize = 16.sp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "찜 $favoriteCount",
                        modifier = Modifier.fillMaxWidth().padding(Dimens.Tiny),
                        style = AppTextStyle.mainPoint
                    )
                }
            }

            StorePictureList(images = remainingImages, imageLoader = imageLoader)
        }
    }
}