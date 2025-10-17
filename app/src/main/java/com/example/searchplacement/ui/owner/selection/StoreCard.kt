package com.example.searchplacement.ui.owner.selection

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.searchplacement.data.dto.StoreSelectDTO
import com.example.searchplacement.di.AppModule
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.CardInnerColor
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.IconColor
import com.example.searchplacement.ui.theme.RatingColor
import com.example.searchplacement.ui.theme.White
import com.example.searchplacement.ui.theme.isOpenColor
import com.example.searchplacement.ui.utils.rememberImageLoaderWithToken


@Composable
fun StoreCard(
    store: StoreSelectDTO,
    onClick: () -> Unit
) {
    val imageLoader = rememberImageLoaderWithToken()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(Dimens.Default),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.Nano)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.Medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = "${AppModule.BASE_URL}/api/files/${store.image[0]}",
                imageLoader = imageLoader,
                contentDescription = "썸네일",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(Dimens.Default))
                    .background(CardInnerColor),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(Dimens.Medium))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = store.storeName,
                    style = AppTextStyle.Body.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(Dimens.Tiny))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = IconColor,
                        modifier = Modifier.size(Dimens.Default)
                    )
                    Spacer(modifier = Modifier.width(Dimens.Tiny))
                    Text(
                        text = store.location,
                        style = AppTextStyle.Body.copy(fontSize = 13.sp, color = IconColor)
                    )
                }

                Spacer(modifier = Modifier.height(Dimens.Tiny))

                Text(
                    text = store.category.first(),
                    style = AppTextStyle.Body.copy(
                        fontSize = 12.sp,
                        color = IconColor
                    )
                )
            }

            Spacer(modifier = Modifier.width(Dimens.Default))

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = RoundedCornerShape(Dimens.Medium),
                    color = if (store.isAvailable) isOpenColor else RatingColor
                ) {
                    Text(
                        text = if (store.isAvailable) "영업중" else "준비중",
                        modifier = Modifier.padding(horizontal = Dimens.Default, vertical = 6.dp),
                        style = AppTextStyle.Body.copy(fontSize = 12.sp, fontWeight = FontWeight.Bold, color = White)
                    )
                }

                Spacer(modifier = Modifier.height(Dimens.Default))

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "이동",
                    tint = IconColor,
                    modifier = Modifier.size(Dimens.Large)
                )
            }
        }
    }
}