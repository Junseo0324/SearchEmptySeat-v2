package com.example.searchplacement.ui.user.reserve.my

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.searchplacement.R
import com.example.searchplacement.data.reserve.ReservationResponse
import com.example.searchplacement.ui.theme.AppButtonStyle
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Black
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.White
import com.example.searchplacement.ui.utils.formatTime


//todo : 디자인 전체적 변경 필요

@Composable
fun ReservedList(navController: NavHostController, reservation: ReservationResponse) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.Tiny, horizontal = Dimens.Small),
        elevation = CardDefaults.elevatedCardElevation(1.dp),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(Dimens.Small)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(Dimens.Small)
            ) {
                //todo 가게 정보 불러와서 이미지 가져올 수 있게 처리
                Image(
                    painter = painterResource(id = R.drawable.ic_store),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RectangleShape)
                )
                Column(
                    Modifier
                        .padding(Dimens.Tiny)
                        .weight(1f)
                ) {
                    Text(text = "가게 ID: ${reservation.storePK}", style = AppTextStyle.Body)
                    Text(text = "예약 시간: ${formatTime(reservation.reservationTime)} / ${reservation.partySize}명",style = AppTextStyle.Body)
                }
                Text(
                    text = "예약 완료",
                    style = AppTextStyle.redPoint
                )
            }

            Text("메뉴", modifier = Modifier.padding(Dimens.Small), style = AppTextStyle.BodyLarge)

            reservation.menu.forEach { (_, menuInfo) ->
                val menu = menuInfo as? Map<*, *> ?: return@forEach
                val name = menu["name"] ?: "메뉴명"
                Text("$name", modifier = Modifier.padding(start = Dimens.Medium).padding(Dimens.Tiny), style = AppTextStyle.Body)
            }

            Button(
                onClick = {
                    navController.currentBackStackEntry?.savedStateHandle?.set("storePK", reservation.storePK)
                    navController.navigate("review")
                }, // 리뷰 작성 화면으로 이동
                colors = ButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Black,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = Black
                ),
                border = BorderStroke(1.dp, Black),
                shape = AppButtonStyle.RoundedShape,
                modifier = Modifier.align(Alignment.End).padding(Dimens.Small)
            ) {
                Text("리뷰 작성")
            }
        }
    }
}