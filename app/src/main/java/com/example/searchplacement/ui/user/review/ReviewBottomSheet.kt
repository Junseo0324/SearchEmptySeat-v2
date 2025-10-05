package com.example.searchplacement.ui.user.review

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.searchplacement.R
import com.example.searchplacement.data.reserve.ReservationResponse
import com.example.searchplacement.data.store.StoreResponse
import com.example.searchplacement.di.AppModule
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Black
import com.example.searchplacement.ui.theme.ButtonMainColor
import com.example.searchplacement.ui.theme.CardInnerColor
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.Gray
import com.example.searchplacement.ui.theme.IconColor
import com.example.searchplacement.ui.theme.RatingColor
import com.example.searchplacement.ui.theme.RedPoint
import com.example.searchplacement.ui.theme.White
import com.example.searchplacement.ui.user.reserve.my.InfoRow
import com.example.searchplacement.ui.utils.parseReservationDateTime
import com.example.searchplacement.ui.utils.rememberImageLoaderWithToken

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewBottomSheet(
    reservation: ReservationResponse,
    store: StoreResponse?,
    onDismiss: () -> Unit,
    onSubmit: (rating: Int, review: String, images: List<Uri>) -> Unit
) {
    var rating by remember { mutableStateOf(0) }
    var reviewText by remember { mutableStateOf("") }
    var selectedImages by remember { mutableStateOf<List<Uri>>(emptyList()) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        val remaining = 5 - selectedImages.size
        selectedImages = selectedImages + uris.take(remaining)
    }

    val imageUrls = store?.image
    val imageLoader = rememberImageLoaderWithToken()
    val IMAGE_URL = "${AppModule.BASE_URL}/api/files/"
    val thumbnailUrl = IMAGE_URL + imageUrls?.firstOrNull()

    val menus = reservation.menu.values.mapNotNull { it as? Map<*, *> }
    val firstMenu = menus.firstOrNull()?.get("name") ?: "메뉴명"
    val remainingCount = (menus.size - 1).coerceAtLeast(0)
    val menuText =
        if (remainingCount > 0) "$firstMenu 외 ${remainingCount}개" else firstMenu.toString()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val (dateText, timeText) = remember(reservation.reservationTime) {
        parseReservationDateTime(reservation.reservationTime)
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(bottom = Dimens.Medium)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.Medium, vertical = Dimens.Medium),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "리뷰 작성",
                    style = AppTextStyle.Body.copy(fontWeight = FontWeight.Bold, color = Black),
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "닫기"
                    )
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                shape = RoundedCornerShape(Dimens.Default),
                colors = CardDefaults.cardColors(containerColor = CardInnerColor),
                elevation = CardDefaults.cardElevation(defaultElevation = Dimens.None)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimens.Medium),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.Default)
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
                            .size(40.dp)
                            .clip(RoundedCornerShape(Dimens.Small)),
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(Dimens.Tiny)
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth().padding(vertical = Dimens.Tiny),
                            text = store?.storeName ?: "",
                            style = AppTextStyle.Body.copy(fontWeight = FontWeight.SemiBold, color = Black)
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
                                text = store?.location ?: "",
                                style = AppTextStyle.BodySmall.copy(fontWeight = FontWeight.Normal, color = Black),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = Dimens.Medium, bottom = Dimens.Medium),
                    verticalArrangement = Arrangement.spacedBy(Dimens.Tiny)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Dimens.Small)
                    ) {
                        InfoRow(icon = Icons.Default.Schedule, text = "$dateText $timeText")
                        InfoRow(icon = Icons.Default.Person, text = "${reservation.partySize}명")
                    }
                    Text(
                        modifier = Modifier.fillMaxWidth().padding(vertical = Dimens.Tiny),
                        text = "주문 메뉴: ${menuText}",
                        style = AppTextStyle.BodySmall.copy(fontWeight = FontWeight.Normal, color = Black),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(Dimens.Large))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens.Default)
            ) {
                Text(
                    text = "방문하신 매장은 어떠셨나요?",
                    style = AppTextStyle.Body.copy(fontWeight = FontWeight.SemiBold),
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(5) { index ->
                        Icon(
                            imageVector = if (index < rating) Icons.Filled.Star else Icons.Outlined.StarBorder,
                            contentDescription = "${index + 1}점",
                            tint = if (index < rating) RatingColor else Gray,
                            modifier = Modifier
                                .size(48.dp)
                                .clickable { rating = index + 1 }
                        )
                    }
                }

                Text(
                    text = when (rating) {
                        1 -> "별로예요"
                        2 -> "그저그래요"
                        3 -> "괜찮아요"
                        4 -> "좋아요"
                        5 -> "최고예요"
                        else -> ""
                    },
                    style = AppTextStyle.Body.copy(fontWeight = FontWeight.SemiBold, color = RatingColor),
                )
            }

            Spacer(modifier = Modifier.height(Dimens.Large))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(Dimens.Small)
            ) {
                Text(
                    text = "방문 후기를 남겨주세요",
                    style = AppTextStyle.Body.copy(fontWeight = FontWeight.SemiBold),
                )

                OutlinedTextField(
                    value = reviewText,
                    onValueChange = {
                        if (it.length <= 100) reviewText = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    placeholder = {
                        Text(
                            text = "다른 손님들에게 도움이 되는 솔직한 후기를 남겨주세요.\n(최소 10자)",
                            style = AppTextStyle.Body.copy(fontSize = 14.sp, color = Gray)
                        )
                    },
                    shape = RoundedCornerShape(Dimens.Default),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ButtonMainColor,
                        unfocusedBorderColor = White
                    ),
                    maxLines = 5
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "최소 10자 이상 작성해주세요",
                        style = AppTextStyle.BodySmall.copy(color = Gray),
                    )
                    Text(
                        text = "${reviewText.length}/100",
                        style = AppTextStyle.BodySmall.copy(
                            color = if (reviewText.length >= 100) RedPoint else Gray
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(Dimens.Large))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(Dimens.Default)
            ) {
                Text(
                    text = "사진 첨부 (선택)",
                    style = AppTextStyle.Body.copy(fontWeight = FontWeight.SemiBold),
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(Dimens.Default)
                ) {
                    if (selectedImages.size < 5) {
                        item {
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .border(
                                        width = 2.dp,
                                        color = Gray,
                                        shape = RoundedCornerShape(Dimens.Default)
                                    )
                                    .clickable { imagePickerLauncher.launch("image/*") },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(Dimens.Tiny)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CameraAlt,
                                        contentDescription = "사진 추가",
                                        tint = IconColor,
                                        modifier = Modifier.size(Dimens.XLarge)
                                    )
                                    Text(
                                        text = "사진 추가하기",
                                        style = AppTextStyle.Caption.copy(color = IconColor, fontWeight = FontWeight.Normal),
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        text = "최대 5장까지 업로드 가능",
                                        style = AppTextStyle.Caption.copy(fontSize = 9.sp, color = IconColor, fontWeight = FontWeight.Normal),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }

                    items(selectedImages) { uri ->
                        Box(
                            modifier = Modifier.size(100.dp)
                        ) {
                            AsyncImage(
                                model = uri,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(Dimens.Default)),
                                contentScale = ContentScale.Crop
                            )

                            // 삭제 버튼
                            IconButton(
                                onClick = {
                                    selectedImages = selectedImages.filter { it != uri }
                                },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(Dimens.Tiny)
                                    .size(12.dp)
                                    .background(
                                        color = Color.Black.copy(alpha = 0.5f),
                                        shape = RoundedCornerShape(Dimens.Default)
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "삭제",
                                    tint = Color.White,
                                    modifier = Modifier.size(Dimens.Default)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(Dimens.Large))

            Button(
                onClick = {
                    if (rating > 0 && reviewText.length >= 10) {
                        onSubmit(rating, reviewText, selectedImages)
                        onDismiss()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(Dimens.Default),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (rating > 0 && reviewText.length >= 10)
                        ButtonMainColor
                    else
                        Gray,
                    contentColor = White
                ),
                enabled = rating > 0 && reviewText.length >= 10
            ) {
                Text(
                    text = "리뷰 등록하기",
                    style = AppTextStyle.Body.copy(fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(Dimens.Medium))
        }
    }
}
