package com.example.searchplacement.ui.owner.reservation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.searchplacement.data.reserve.ReservationResponse
import com.example.searchplacement.ui.theme.AppButtonStyle
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.CardBorderTransparentColor
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.OwnerPrimaryColor
import com.example.searchplacement.ui.theme.White
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun PendingReservationCard(reservation: ReservationResponse) {
    val formatterInput = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    val formatterOutput = DateTimeFormatter.ofPattern("MM/dd HH:mm")
    val parsedTime = try {
        LocalDateTime.parse(reservation.reservationTime,formatterInput)
            .format(formatterOutput)
    } catch (e: Exception) {
        reservation.reservationTime
    }

    val menuList = reservation.menu.entries.toList()

    Card(
        modifier = Modifier.padding(Dimens.Small).fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(1.dp),
        border = BorderStroke(width = 1.dp, color =CardBorderTransparentColor ),
        shape = AppButtonStyle.RoundedShape
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("예약번호 ${reservation.reservationNum}", style = AppTextStyle.Body.copy(color = OwnerPrimaryColor, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(Dimens.Tiny))
                Text(parsedTime,style = AppTextStyle.Body, modifier = Modifier.padding(Dimens.Tiny))
            }
            Text("인원 ${reservation.partySize}", style = AppTextStyle.Body,modifier = Modifier.padding(Dimens.Tiny))
            Text("자리 ${reservation.tableNumber}",style = AppTextStyle.Body,modifier = Modifier.padding(Dimens.Tiny))

            Spacer(modifier = Modifier.height(8.dp))
            Text("메뉴", style = AppTextStyle.Body.copy(fontWeight = FontWeight.Bold), modifier = Modifier.padding(Dimens.Tiny))
            menuList.forEach { (_, value) ->
                val menuData = value as? Map<*, *> ?: return@forEach
                val menuName = menuData["name"]
                val quantity = (menuData["quantity"] as? Double)?.toInt() ?: 0
                Row(
                    modifier = Modifier.fillMaxWidth().padding(Dimens.Tiny),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = menuName.toString(), style = AppTextStyle.Body)
                    Text("x${quantity}",style = AppTextStyle.Body, textAlign = TextAlign.End)
                }
            }

        }
    }
}