package com.example.searchplacement.ui.user.store.review

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import com.example.searchplacement.data.review.ReviewResponse
import com.example.searchplacement.di.AppModule
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.IconColor
import com.example.searchplacement.ui.theme.RatingColor
import com.example.searchplacement.ui.theme.White
import com.example.searchplacement.ui.utils.parseReservationDateTime
import com.example.searchplacement.ui.utils.rememberImageLoaderWithToken


@Composable
fun ReviewCard(review: ReviewResponse) {
    val imageLoader = rememberImageLoaderWithToken()
    val IMAGE_URL = "${AppModule.BASE_URL}/api/files/"
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.Default),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.Medium),
            verticalArrangement = Arrangement.spacedBy(Dimens.Default)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimens.Small),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = review.userName,
                        style = AppTextStyle.Body.copy(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold)
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Dimens.Nano)
                    ) {
                        repeat(5) { index ->
                            Icon(
                                imageVector = if (index < review.rating) Icons.Filled.Star else Icons.Default.StarBorder,
                                contentDescription = null,
                                tint = RatingColor,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
                Text(
                    text = parseReservationDateTime(review.createdDate).first + " " + parseReservationDateTime(review.createdDate).second,
                    style = AppTextStyle.Body.copy(fontSize = 12.sp, color = IconColor)
                )
            }

            Text(
                text = review.content,
                style = AppTextStyle.Body.copy(fontSize = 14.sp)
            )

            if (review.image.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(Dimens.Small)
                ) {
                    items(review.image) { image ->
                        AsyncImage(
                            model = IMAGE_URL + image,
                            imageLoader = imageLoader,
                            contentDescription = null,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(Dimens.Small)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}