package com.example.searchplacement.ui.user.reserve.my

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.searchplacement.ui.theme.White
import com.example.searchplacement.ui.utils.parseReservationDateTime
import com.example.searchplacement.ui.utils.rememberImageLoaderWithToken


@Composable
fun ReservedList(
    navController: NavHostController,
    reservation: ReservationResponse,
    store: StoreResponse?,
    onReviewClick: (ReservationResponse, StoreResponse?) -> Unit,
    onCancelClick: (Long) -> Unit
) {

    val (statusText, statusBgColor, statusTextColor) = when (reservation.status) {
        "pending" -> Triple("예약 중", Color(0xFFE3F2FD), Color(0xFF1565C0))
        "completed" -> Triple("방문 완료", Color(0xFFD5EDDA), Color(0xFF27AE60))
        else -> Triple("알 수 없음", Color.LightGray, Color.DarkGray)
    }

    val (dateText, timeText) = remember(reservation.reservationTime) {
        parseReservationDateTime(reservation.reservationTime)
    }

    val menus = reservation.menu.values.mapNotNull { it as? Map<*, *> }
    val firstMenu = menus.firstOrNull()?.get("name") ?: "메뉴명"
    val remainingCount = (menus.size - 1).coerceAtLeast(0)
    val menuText =
        if (remainingCount > 0) "$firstMenu 외 ${remainingCount}개" else firstMenu.toString()

    val imageUrls = store?.image
    val imageLoader = rememberImageLoaderWithToken()
    val IMAGE_URL = "${AppModule.BASE_URL}/api/files/"
    val thumbnailUrl = IMAGE_URL + imageUrls?.firstOrNull()

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(1.dp),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(Dimens.Medium),
            verticalArrangement = Arrangement.spacedBy(Dimens.Default)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.Default),
                verticalAlignment = Alignment.CenterVertically
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
                    modifier = Modifier.size(60.dp).clip(RoundedCornerShape(Dimens.Default)),
                )


                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(Dimens.Tiny)
                ) {
                    Text(
                        text = store?.storeName ?: "가게 정보 불러올 수 없음",
                        style = AppTextStyle.Body.copy(fontWeight = FontWeight.Bold, color = Black),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(Dimens.Default),
                            tint = IconColor
                        )
                        Text(
                            text = store?.location ?: "주소 정보 없음",
                            style = AppTextStyle.Body.copy(fontSize = 13.sp, color = IconColor),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .background(
                            color = statusBgColor,
                            shape = RoundedCornerShape(Dimens.Small)
                        )
                        .padding(horizontal = Dimens.Default, vertical = Dimens.Tiny)
                        .align(Alignment.Top)
                ) {
                    Text(
                        text = statusText,
                        style = AppTextStyle.BodySmall.copy(fontWeight = FontWeight.Bold, color = statusTextColor)
                    )
                }
            }

            HorizontalDivider(color = Gray)

            // 예약 정보
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.XLarge)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(Dimens.Small)
                ) {
                    InfoRow(icon = Icons.Default.DateRange, text = dateText)
                    InfoRow(icon = Icons.Default.Schedule, text = timeText)
                    InfoRow(icon = Icons.Default.Person, text = "${reservation.partySize}명")
                }

                // 오른쪽 정보
                Column(
                    verticalArrangement = Arrangement.spacedBy(Dimens.Small)
                ) {
                    InfoItem(label = "예약번호", value = reservation.reservationPK.toString())
                    InfoItem(label = "테이블", value = reservation.tableNumber.toString())
                    InfoItem(label = "결제방식", value = reservation.paymentMethod)
                }
            }

            Spacer(modifier = Modifier.height(Dimens.Tiny))
            Column(
                modifier = Modifier.fillMaxWidth().background(CardInnerColor).padding(Dimens.Small),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimens.Small),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.Receipt,
                        contentDescription = null,
                        modifier = Modifier.size(Dimens.Medium),
                        tint = IconColor
                    )
                    Text(
                        text = "주문 메뉴",
                        style = AppTextStyle.Body.copy(fontSize = 13.sp, color = IconColor),
                    )
                }
                Text(
                    text = menuText,
                    style = AppTextStyle.Body.copy(fontSize = 14.sp, color = Black),
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.Small)
            ) {
                if (reservation.status == "pending") {
                    OutlinedButton(
                        onClick = { },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(Dimens.Small),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = ButtonMainColor
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = null,
                            modifier = Modifier.size(Dimens.Medium)
                        )
                        Spacer(modifier = Modifier.width(Dimens.Tiny))
                        Text("매장 문의", fontSize = 14.sp)
                    }
                    Button(
                        onClick = {
                            onCancelClick(reservation.reservationPK)
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(Dimens.Small),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE74C3C)
                        )
                    ) {
                        Text("예약 취소", fontSize = 14.sp)
                    }
                } else {
                    OutlinedButton(
                        onClick = { onReviewClick(reservation, store) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(Dimens.Default),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = ButtonMainColor
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Star,
                            contentDescription = null,
                            modifier = Modifier.size(Dimens.Medium)
                        )
                        Spacer(modifier = Modifier.width(Dimens.Tiny))
                        Text("리뷰 작성", fontSize = 14.sp)
                    }
                    Button(
                        onClick = {
                            navController.navigate("store/${store?.storePK}")
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ButtonMainColor
                        )
                    ) {
                        Text("예약하기", fontSize = 14.sp)
                    }
                }
            }
        }
    }
}




