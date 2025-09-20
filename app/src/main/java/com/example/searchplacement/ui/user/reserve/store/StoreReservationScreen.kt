package com.example.searchplacement.ui.user.reserve.store

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.searchplacement.data.reserve.ReservationDraft
import com.example.searchplacement.ui.theme.LoginTextColor
import com.example.searchplacement.viewmodel.StoreViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale


@Composable
fun StoreReservationScreen(navController: NavHostController, storeViewModel: StoreViewModel) {
    val storeData by storeViewModel.storeData.collectAsState()
    var partySize by remember { mutableStateOf(1) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    val businessHours = storeData?.data?.businessHours
    val selectedDayOfWeek = selectedDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREAN)
    val timeSlots = remember(selectedDate, businessHours) {
        businessHours?.get(selectedDayOfWeek)?.let { getTimeSlots(it) } ?: emptyList()
    }
    var selectedTime by remember { mutableStateOf<String?>(null) }
    var userId = storeViewModel.userId.collectAsState().value

    LaunchedEffect(Unit) {
        storeViewModel.getUserId()
    }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("예약 하기", style = MaterialTheme.typography.headlineMedium)
        }

        item {
            Text("방문 인원 선택", style = MaterialTheme.typography.titleMedium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { if (partySize > 1) partySize-- }) {
                    Text("-", fontSize = 24.sp)
                }
                Text("$partySize", modifier = Modifier.padding(horizontal = 16.dp))
                IconButton(onClick = { partySize++ }) {
                    Text("+", fontSize = 24.sp)
                }
            }
        }

        item {
            Text("날짜 선택", style = MaterialTheme.typography.titleMedium)
            CustomCalendarView(
                selectedDate = selectedDate,
                onDateSelected = { selectedDate = it }
            )
        }

        item {
            Text(
                "${selectedDate.format(DateTimeFormatter.ofPattern("M.d(E)", Locale.KOREAN))} 시간을 선택해 주세요",
                style = MaterialTheme.typography.titleMedium
            )
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    userScrollEnabled = false,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(timeSlots) { time ->
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .background(
                                    if (selectedTime == time) LoginTextColor else Color.White
                                )
                                .border(
                                    BorderStroke(
                                        1.dp,
                                        if (selectedTime == time) Color.Transparent else Color.Black
                                    )
                                )
                                .clickable {
                                    selectedTime = time
                                }
                                .padding(vertical = 12.dp, horizontal = 8.dp)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = time,
                                color = if (selectedTime == time) Color.White else Color.Black
                            )
                        }
                    }
                }
            }
        }


        item {
            Button(
                onClick = {
                    if (userId?.isEmpty() == true || selectedTime == null || storeData?.data == null) {
                        Log.e("예약", "값이 없음 → userId:$userId, time:$selectedTime, store:${storeData?.data}")
                        return@Button
                    }

                    val draft = ReservationDraft(
                        storePK = storeData!!.data!!.storePK,
                        userId = userId?.toLong() ?: 0L,
                        partySize = partySize,
                        reservationTime = "${selectedDate}T$selectedTime"
                    )
                    navController.currentBackStackEntry?.savedStateHandle?.set("reservationDraft", draft)
                    navController.navigate("seatMenuSelection")
                },
                colors = ButtonColors(
                    Color.Transparent, Color.Black, Color.Transparent, Color.Black
                ),
                border = BorderStroke(1.dp, Color.Black),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("다음")
            }

        }
    }
}


fun getTimeSlots(hours: String): List<String> {
    val (open, close) = hours.split("-").map { it.trim() }

    val formatter = DateTimeFormatter.ofPattern("H:mm")
    val openTime = LocalTime.parse(open, formatter)
    val closeTime = LocalTime.parse(close, formatter)

    val result = mutableListOf<String>()
    var current = openTime

    while (current < closeTime) {
        result.add(current.format(formatter))
        current = current.plusMinutes(60)
    }

    return result
}

@Composable
fun CustomCalendarView(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentYearMonth by remember { mutableStateOf(YearMonth.now()) } //현재 연도-월 상태

    val daysOfWeek = listOf("일", "월", "화", "수", "목", "금", "토")
    val dates = remember(currentYearMonth) {
        getDatesForMonth(currentYearMonth.year, currentYearMonth.monthValue)
    } //해당 월의 달력에 표현할 LocalDate 목록을 계산하는 함수 결과

    Column(modifier = modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {
                currentYearMonth = currentYearMonth.minusMonths(1)
            }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "이전 달")
            }

            Text(
                text = "${currentYearMonth.year}년 ${currentYearMonth.monthValue}월",
                style = MaterialTheme.typography.titleMedium
            )

            IconButton(onClick = {
                currentYearMonth = currentYearMonth.plusMonths(1)
            }) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "다음 달")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            userScrollEnabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            items(dates) { date ->
                val isCurrentMonth = date.monthValue == currentYearMonth.monthValue
                val isSelected = date == selectedDate

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(4.dp)
                        .background(
                            when {
                                isSelected -> LoginTextColor
                                !isCurrentMonth -> Color.LightGray.copy(alpha = 0.2f)
                                else -> Color.Transparent
                            }
                        )
                        .clickable(enabled = isCurrentMonth) {
                            onDateSelected(date)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = date.dayOfMonth.toString(),
                        color = if (isCurrentMonth) Color.Black else Color.Gray
                    )
                }
            }
        }
    }
}


fun getDatesForMonth(year: Int, month: Int): List<LocalDate> {
    val firstOfMonth = LocalDate.of(year, month, 1)
    val lastOfMonth = firstOfMonth.withDayOfMonth(firstOfMonth.lengthOfMonth())

    val startDayOfWeek = firstOfMonth.dayOfWeek.value % 7 // 일=0, 월=1 ...
    val totalDays = startDayOfWeek + lastOfMonth.dayOfMonth
    val totalWeeks = (totalDays + 6) / 7
    val totalCells = totalWeeks * 7

    val firstDate = firstOfMonth.minusDays(startDayOfWeek.toLong())

    return List(totalCells) { firstDate.plusDays(it.toLong()) }
}



