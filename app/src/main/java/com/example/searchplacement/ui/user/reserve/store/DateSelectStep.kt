package com.example.searchplacement.ui.user.reserve.store

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.searchplacement.data.reserve.ReservationData
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.IconColor
import com.example.searchplacement.ui.theme.IconTextColor
import com.example.searchplacement.ui.theme.StoreTabBackgroundColor
import com.example.searchplacement.ui.theme.reservationCountColor
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun DateSelectStep(
    reservationData: ReservationData,
    businessHours: Map<String, String>,
    onSelectedDate: (LocalDate) -> Unit
) {
    var selectedDate by remember { mutableStateOf(reservationData.selectedDate ?: LocalDate.now()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(StoreTabBackgroundColor)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.Small)
        ) {
            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = null,
                tint = reservationCountColor,
                modifier = Modifier.size(Dimens.Large)
            )
            Text(
                text = "방문 날짜를 선택해주세요",
                style = AppTextStyle.Body.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = IconTextColor
                )
            )
        }

        CustomCalendarView(
            selectedDate = selectedDate,
            onDateSelected = { date ->
                selectedDate = date
                onSelectedDate(date)
            }
        )

        if (reservationData.selectedDate != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(Dimens.Default),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(Dimens.Small)
                ) {
                    Text(
                        text = "선택된 날짜: ${selectedDate.format(DateTimeFormatter.ofPattern("yyyy년 M월 d일"))} ${
                            getDayOfWeek(selectedDate)
                        }",
                        style = AppTextStyle.Body.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1565C0)
                        )
                    )
                    val dayKey = getDayOfWeekKey(selectedDate)
                    val hours = businessHours[dayKey] ?: "정보 없음"
                    Text(
                        text = "영업시간: $hours",
                        style = AppTextStyle.Body.copy(fontSize = 14.sp, color = IconColor)
                    )
                }
            }
        }
    }
}