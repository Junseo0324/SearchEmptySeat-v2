package com.example.searchplacement.ui.user.reserve.my

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.searchplacement.data.reserve.ReservationResponse
import com.example.searchplacement.ui.theme.AppButtonStyle
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Black
import com.example.searchplacement.ui.theme.ButtonMainColor
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.LoginTextColor
import com.example.searchplacement.ui.theme.White
import com.example.searchplacement.ui.utils.formatTime

//todo : 디자인 전체적 변경 필요
@Composable
fun ReservingStore(reservation: ReservationResponse) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(Dimens.Small)
            .background(Color.Transparent)
            .border(1.dp, ButtonMainColor, shape = AppButtonStyle.RoundedShape)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(Dimens.Tiny),
            elevation = CardDefaults.elevatedCardElevation(1.dp),
            colors = CardDefaults.cardColors(containerColor = White)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(Dimens.Tiny)
            ) {
                Column(Modifier.weight(1f)) {
                    Text("가게 ID: ${reservation.storePK}", style = MaterialTheme.typography.titleMedium)
                }
                Button(
                    onClick = { /* 예약 취소 처리 */ },
                    colors = ButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = Black,
                        disabledContainerColor = Color.Transparent,
                        disabledContentColor = Black
                    ),
                    border = BorderStroke(1.dp, Black),
                    shape = AppButtonStyle.RoundedShape
                ) {
                    Text("예약 취소")
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth().padding(Dimens.Tiny),
            elevation = CardDefaults.elevatedCardElevation(1.dp),
            colors = CardDefaults.cardColors(containerColor = White)
        ) {
            Column(Modifier.padding(10.dp)) {
                Text("예약 시간", style = AppTextStyle.BodyLarge)
                Text(
                    formatTime(reservation.reservationTime),
                    color = LoginTextColor,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold
                )
                Text("*예약 30분 전에는 예약 취소가 불가능합니다.", style = AppTextStyle.Caption)
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth().padding(Dimens.Tiny),
            elevation = CardDefaults.elevatedCardElevation(1.dp),
            colors = CardDefaults.cardColors(containerColor = White)
        ) {
            Column(Modifier.padding(Dimens.Small)) {
                Text("예약 정보", style = AppTextStyle.BodyLarge)
                Text("좌석 번호: ${reservation.tableNumber}", style = AppTextStyle.Body)
                Text("인원: ${reservation.partySize}", style = AppTextStyle.Body)
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth().padding(Dimens.Tiny),
            elevation = CardDefaults.elevatedCardElevation(1.dp),
            colors = CardDefaults.cardColors(containerColor = White)
        ) {
            Column(Modifier.padding(Dimens.Small)) {
                Text("메뉴", style = AppTextStyle.Body)

                var total = 0
                reservation.menu.forEach { (_, menuInfo) ->
                    val menu = menuInfo as? Map<*, *> ?: return@forEach
                    val name = menu["name"] ?: "-"
                    val quantity = (menu["quantity"] as? Number)?.toInt() ?: 0
                    val price = (menu["price"] as? Number)?.toInt() ?: 0
                    val totalPrice = quantity * price
                    total += totalPrice

                    SetMenu(name.toString(), quantity.toString(), price.toString())
                }

                HorizontalDivider()
                TotalPay("${total}원")
            }
        }
    }
}


@Composable
fun SetMenu(menu: String, count: String, pay: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.Tiny)
    ) {
        Text(
            text = menu,
            modifier = Modifier.align(Alignment.CenterStart)
        )
        Text(
            text = pay,
            modifier = Modifier.align(Alignment.CenterEnd)
        )
        Text(
            text = "x$count",
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .offset(x = (-80).dp)
        )
    }
}
@Composable
fun TotalPay(total: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(Dimens.Tiny), horizontalArrangement = Arrangement.End) {
        Text(text = total)
    }
}
