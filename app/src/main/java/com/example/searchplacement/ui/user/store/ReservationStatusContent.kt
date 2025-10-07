package com.example.searchplacement.ui.user.store

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.RedPoint
import com.example.searchplacement.ui.theme.StoreTabBackgroundColor
import com.example.searchplacement.ui.theme.White


@Composable
fun ReservationStatusContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(StoreTabBackgroundColor)
            .padding(Dimens.Medium),
        verticalArrangement = Arrangement.spacedBy(Dimens.Medium)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Dimens.Default)
        ) {
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(Dimens.Default),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Dimens.Small)
                ) {
                    Text(
                        text = "45",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1565C0)
                    )
                    Text(
                        text = "예약 가능",
                        fontSize = 14.sp,
                        color = Color(0xFF1565C0)
                    )
                }
            }

            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(Dimens.Default),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Dimens.Small)
                ) {
                    Text(
                        text = "15",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = RedPoint
                    )
                    Text(
                        text = "예약 중",
                        fontSize = 14.sp,
                        color = RedPoint
                    )
                }
            }
        }

        // 오늘의 예약 현황
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(Dimens.Default),
            colors = CardDefaults.cardColors(containerColor = White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(Dimens.Medium)
            ) {
                Text(
                    text = "오늘의 예약 현황",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C3E50)
                )

                // 시간대별 예약 현황
                TimeSlotItem(time = "14:00", status = "예약가능", seats = "4석", isAvailable = true)
                TimeSlotItem(time = "15:00", status = "예약중", seats = "2석", isAvailable = false)
                TimeSlotItem(time = "16:00", status = "예약가능", seats = "6석", isAvailable = true)
                TimeSlotItem(time = "17:00", status = "예약가능", seats = "8석", isAvailable = true)
                TimeSlotItem(time = "18:00", status = "예약중", seats = "4석", isAvailable = false)
            }
        }
    }
}