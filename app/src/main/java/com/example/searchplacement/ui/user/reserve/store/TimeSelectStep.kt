package com.example.searchplacement.ui.user.reserve.store

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.searchplacement.ui.utils.generateTimeSlots
import com.example.searchplacement.ui.utils.getDayOfWeek
import com.example.searchplacement.ui.utils.getDayOfWeekKey
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// 3단계: 시간 선택
@Composable
fun TimeSelectStep(
    reservationData: ReservationData,
    businessHours: Map<String, String>,
    onSelectedTime: (String) -> Unit
) {
    val selectedDate = reservationData.selectedDate ?: LocalDate.now()
    val dayKey = getDayOfWeekKey(selectedDate)
    val hours = businessHours[dayKey] ?: "11:30 - 21:30"

    val timeSlots = remember(hours) { generateTimeSlots(hours) }


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
                imageVector = Icons.Default.AccessTime,
                contentDescription = null,
                tint = reservationCountColor,
                modifier = Modifier.size(Dimens.Large)
            )
            Text(
                text = "방문 시간을 선택해주세요",
                style = AppTextStyle.Body.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = IconTextColor
                )
            )
        }

        Text(
            text = "${selectedDate.format(DateTimeFormatter.ofPattern("M월 d일"))} ${getDayOfWeek(selectedDate)} 영업시간: $hours",
            style = AppTextStyle.Body.copy(fontSize = 14.sp, color = IconColor)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(Dimens.Default),
            verticalArrangement = Arrangement.spacedBy(Dimens.Default)
        ) {
            items(timeSlots) { time ->
                val isSelected = reservationData.selectedTime == time
                TimeSlotCard(
                    time = time,
                    availableSeats = 0,
                    isSelected = isSelected,
                    isAvailable = true,
                    onClick = {
                        onSelectedTime(time)
                    }
                )
            }
        }
    }
}