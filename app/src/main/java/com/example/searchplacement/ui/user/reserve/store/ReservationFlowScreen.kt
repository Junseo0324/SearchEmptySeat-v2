package com.example.searchplacement.ui.user.reserve.store

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.EventSeat
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.searchplacement.data.reserve.ReservationData
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.CardBorderTransparentColor
import com.example.searchplacement.ui.theme.ChipBorderColor
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.IconColor
import com.example.searchplacement.ui.theme.IconTextColor
import com.example.searchplacement.ui.theme.RedPoint
import com.example.searchplacement.ui.theme.StoreTabBackgroundColor
import com.example.searchplacement.ui.theme.TableOptionColor
import com.example.searchplacement.ui.theme.ViewCountColor
import com.example.searchplacement.ui.theme.White
import com.example.searchplacement.ui.theme.isOpenColor
import com.example.searchplacement.ui.theme.reservationCountColor
import com.example.searchplacement.viewmodel.ReservationViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationFlowScreen(
    navController: NavHostController,
    storeId: Long,
    storeName: String,
    businessHours: Map<String, String>,
) {
    val context = LocalContext.current
    val reservationViewModel: ReservationViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        reservationViewModel.getStoreData(storeId)
    }
    val store = reservationViewModel.storeData.collectAsState().value

    var currentStep by remember { mutableStateOf(ReservationStep.PEOPLE_COUNT) }
//    val reservationData = remember { ReservationData() }
    val reservationData by reservationViewModel.reservationData


    val stepProgress = when (currentStep) {
        ReservationStep.PEOPLE_COUNT -> 0.16f
        ReservationStep.DATE_SELECT -> 0.33f
        ReservationStep.TIME_SELECT -> 0.5f
        ReservationStep.TABLE_SELECT -> 0.66f
        ReservationStep.MENU_SELECT -> 0.83f
        ReservationStep.CONFIRMATION -> 1f
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = store?.storeName ?: "정보 없음",
                            style = AppTextStyle.Body.copy(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = when (currentStep) {
                                ReservationStep.PEOPLE_COUNT -> "방문 인원"
                                ReservationStep.DATE_SELECT -> "날짜 선택"
                                ReservationStep.TIME_SELECT -> "시간 선택"
                                ReservationStep.TABLE_SELECT -> "좌석 선택"
                                ReservationStep.MENU_SELECT -> "메뉴 선택"
                                ReservationStep.CONFIRMATION -> "예약 확인"
                            },
                            style = AppTextStyle.Body.copy(fontSize = 14.sp, color = IconColor)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (currentStep == ReservationStep.PEOPLE_COUNT) {
                            navController.popBackStack()
                        } else {
                            currentStep =
                                ReservationStep.entries.toTypedArray()[currentStep.ordinal - 1]
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "뒤로가기")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // 진행 표시줄
            LinearProgressIndicator(
                progress = { stepProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.Nano),
                color = reservationCountColor,
                trackColor = ChipBorderColor
            )

            // 단계별 컨텐츠
            Box(modifier = Modifier.weight(1f)) {
                when (currentStep) {
                    ReservationStep.PEOPLE_COUNT -> PeopleCountStep(
                        reservationData = reservationData,
                        onChangePeople = { newCount ->
                            reservationViewModel.updateReservation {
                                it.copy(numberOfPeople = newCount)
                            }
                        }
                    )
                    ReservationStep.DATE_SELECT -> DateSelectStep(
                        reservationData = reservationData,
                        businessHours = businessHours,
                        onSelectedDate = { date ->
                            reservationViewModel.updateReservation {
                                it.copy(selectedDate = date)
                            }
                        }
                    )
                    ReservationStep.TIME_SELECT -> TimeSelectStep(reservationData, businessHours)
                    ReservationStep.TABLE_SELECT -> TableSelectStep(reservationData, storeId)
                    ReservationStep.MENU_SELECT -> MenuSelectStep(reservationData, storeId)
                    ReservationStep.CONFIRMATION -> ConfirmationStep(
                        reservationData,
                        storeName,
                        onConfirm = {
                            /* 예약 완료 로직 */
//                            reservationViewModel.submitReservation(storeId, reservationData)
//                            navController.popBackStack()

                        }
                    )
                }
            }

            // 다음 단계 버튼
            Button(
                onClick = {
                    if (currentStep != ReservationStep.CONFIRMATION) {
                        currentStep =
                            ReservationStep.entries.toTypedArray()[currentStep.ordinal + 1]
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.Medium)
                    .height(56.dp),
                shape = RoundedCornerShape(Dimens.Default),
                colors = ButtonDefaults.buttonColors(
                    containerColor = IconTextColor
                ),
                enabled = isStepComplete(currentStep, reservationData)
            ) {
                Text(
                    text = if (currentStep == ReservationStep.CONFIRMATION) "예약 완료" else "다음 단계",
                    style = AppTextStyle.Body.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.width(Dimens.Small))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, null, modifier = Modifier.size(20.dp))
            }
        }
    }
}

fun isStepComplete(step: ReservationStep, data: ReservationData): Boolean {
    return when (step) {
        ReservationStep.PEOPLE_COUNT -> data.numberOfPeople in 1..8
        ReservationStep.DATE_SELECT -> data.selectedDate != null
        ReservationStep.TIME_SELECT -> data.selectedTime != null
        ReservationStep.TABLE_SELECT -> data.selectedTable != null
        ReservationStep.MENU_SELECT -> true // 메뉴는 선택 안해도 됨
        ReservationStep.CONFIRMATION -> true
    }
}

// 1단계: 방문 인원
@Composable
fun PeopleCountStep(
    reservationData: ReservationData,
    onChangePeople: (Int) -> Unit
) {
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
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = reservationCountColor,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "방문 인원을 선택해주세요",
                style = AppTextStyle.Body.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = IconTextColor
                )
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(Dimens.Medium),
            colors = CardDefaults.cardColors(containerColor = White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.Large),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens.Large)
            ) {
                // +/- 버튼
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimens.Large),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            if (reservationData.numberOfPeople > 1) onChangePeople(reservationData.numberOfPeople - 1)
                        },
                        modifier = Modifier
                            .size(56.dp)
                            .border(Dimens.Nano, ChipBorderColor, CircleShape)
                    ) {
                        Icon(Icons.Default.Remove, null, modifier = Modifier.size(Dimens.Large))
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = reservationData.numberOfPeople.toString(),
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = reservationCountColor
                        )
                        Text(
                            text = "명",
                            style = AppTextStyle.Body.copy(color = IconColor)
                        )
                    }

                    IconButton(
                        onClick = {
                            if (reservationData.numberOfPeople < 8)
                                onChangePeople(reservationData.numberOfPeople + 1)
                        },
                        modifier = Modifier
                            .size(56.dp)
                            .border(Dimens.Nano, ChipBorderColor, CircleShape)
                    ) {
                        Icon(Icons.Default.Add, null, modifier = Modifier.size(Dimens.Large))
                    }
                }

                // 인원 선택 버튼들
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.Default),
                    verticalArrangement = Arrangement.spacedBy(Dimens.Default),
                    modifier = Modifier.height(200.dp)
                ) {
                    items(listOf(1, 2, 4, 8)) { count ->
                        Card(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clickable { reservationData.numberOfPeople = count },
                            shape = RoundedCornerShape(Dimens.Default),
                            colors = CardDefaults.cardColors(
                                containerColor = if (reservationData.numberOfPeople == count)
                                    Color(0xFFE3F2FD)
                                else
                                    White
                            ),
                            border = if (reservationData.numberOfPeople == count)
                                BorderStroke(Dimens.Nano, reservationCountColor)
                            else
                                BorderStroke(1.dp, ChipBorderColor)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "${count}명",
                                    fontSize = 18.sp,
                                    fontWeight = if (reservationData.numberOfPeople == count)
                                        FontWeight.Bold else FontWeight.Normal,
                                    color = if (reservationData.numberOfPeople == count)
                                        reservationCountColor else IconTextColor
                                )
                                Text(
                                    text = "${count}인석",
                                    style = AppTextStyle.Body.copy(
                                        fontSize = 12.sp,
                                        color = IconColor
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// 2단계: 날짜 선택
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

// 3단계: 시간 선택
@Composable
fun TimeSelectStep(reservationData: ReservationData, businessHours: Map<String, String>) {
    val selectedDate = reservationData.selectedDate ?: LocalDate.now()
    val dayKey = getDayOfWeekKey(selectedDate)
    val hours = businessHours[dayKey] ?: "11:30 - 21:30"

    // 영업시간 파싱
    val (startHour, endHour) = parseBusinessHours(hours)
    val timeSlots = generateTimeSlots(startHour, endHour)

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
            text = "${selectedDate.format(DateTimeFormatter.ofPattern("M월 d일"))} ${
                getDayOfWeek(
                    selectedDate
                )
            } 영업시간: $hours",
            style = AppTextStyle.Body.copy(fontSize = 14.sp, color = IconColor)
        )

        // 시간 선택 그리드
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(Dimens.Default),
            verticalArrangement = Arrangement.spacedBy(Dimens.Default)
        ) {
            items(timeSlots) { timeSlot ->
                TimeSlotCard(
                    time = timeSlot.time,
                    availableSeats = timeSlot.availableSeats,
                    isSelected = reservationData.selectedTime == timeSlot.time,
                    isAvailable = timeSlot.isAvailable,
                    onClick = {
                        if (timeSlot.isAvailable) {
                            reservationData.selectedTime = timeSlot.time
                        }
                    }
                )
            }
        }
    }
}

// 시간대 카드
@Composable
fun TimeSlotCard(
    time: String,
    availableSeats: Int,
    isSelected: Boolean,
    isAvailable: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        !isAvailable -> CardBorderTransparentColor
        isSelected -> Color(0xFFE3F2FD)
        else -> White
    }

    val textColor = when {
        !isAvailable -> ViewCountColor
        isSelected -> Color(0xFF1565C0)
        else -> Color(0xFF2C3E50)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = isAvailable, onClick = onClick),
        shape = RoundedCornerShape(Dimens.Default),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = if (isSelected) BorderStroke(Dimens.Nano, reservationCountColor) else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.Medium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.Tiny)
        ) {
            Text(
                text = time,
                style = AppTextStyle.Body.copy(fontWeight = FontWeight.Bold, color = textColor)
            )
            Text(
                text = if (isAvailable) "${availableSeats}석 남음" else "예약 불가",
                style = AppTextStyle.Body.copy(
                    fontSize = 12.sp,
                    color = if (isAvailable) isOpenColor else RedPoint
                )
            )
        }
    }
}

// 유틸리티 함수들
fun getDayOfWeek(date: LocalDate): String {
    val days = listOf("일", "월", "화", "수", "목", "금", "토")
    return "${days[date.dayOfWeek.value % 7]}요일"
}

fun getDayOfWeekKey(date: LocalDate): String {
    val days = listOf("일", "월", "화", "수", "목", "금", "토")
    return days[date.dayOfWeek.value % 7]
}

fun parseBusinessHours(hours: String): Pair<Int, Int> {
    // "11:30 - 21:30" -> (11, 21)
    val parts = hours.split("-").map { it.trim() }
    val start = parts[0].split(":")[0].toInt()
    val end = parts[1].split(":")[0].toInt()
    return start to end
}

data class TimeSlotData(
    val time: String,
    val availableSeats: Int,
    val isAvailable: Boolean
)

fun generateTimeSlots(startHour: Int, endHour: Int): List<TimeSlotData> {
    val slots = mutableListOf<TimeSlotData>()
    for (hour in startHour until endHour) {
        slots.add(
            TimeSlotData(
                time = String.format("%02d:00", hour),
                availableSeats = (5..12).random(),
                isAvailable = hour < 14 || hour >= 15 // 예시: 14시는 예약 불가
            )
        )
    }
    return slots
}

// 캘린더 뷰 (간단한 버전)
@Composable
fun CalendarView(
    currentMonth: LocalDate,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    onMonthChanged: (LocalDate) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.Medium),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Column(
            modifier = Modifier.padding(Dimens.Medium)
        ) {
            // 월 네비게이션
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onMonthChanged(currentMonth.minusMonths(1)) }) {
                    Icon(Icons.Default.ChevronLeft, null)
                }
                Text(
                    text = currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = { onMonthChanged(currentMonth.plusMonths(1)) }) {
                    Icon(Icons.Default.ChevronRight, null)
                }
            }

            Spacer(modifier = Modifier.height(Dimens.Medium))

            // 요일 헤더
            Row(modifier = Modifier.fillMaxWidth()) {
                listOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa").forEach { day ->
                    Text(
                        text = day,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp,
                        color = IconColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(Dimens.Small))

            // 날짜 그리드 (간단화)
            Text(
                text = "캘린더 UI는 실제 구현 시 더 상세하게 작성됩니다",
                fontSize = 12.sp,
                color = Color(0xFF7F8C8D),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

// 4단계: 좌석 선택
@Composable
fun TableSelectStep(reservationData: ReservationData, storeId: Long) {
    // 선택한 인원수에 맞는 테이블 필터링
    val availableTables = remember {
        listOf(
            TableOption("중앙 4인석", "4인석 · 최대 4명", true, "table_1"),
            TableOption("대형 8인석", "8인석 · 최대 8명", true, "table_2"),
            TableOption("입구 4인석", "4인석 · 최대 4명", true, "table_3"),
            TableOption("창가 2인석", "2인석 · 최대 2명", false, "table_4")
        ).filter {
            (it.capacity.replace("인석", "").toIntOrNull() ?: 0) >= reservationData.numberOfPeople
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.EventSeat,
                contentDescription = null,
                tint = Color(0xFF3498DB),
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "좌석을 선택해주세요",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2C3E50)
            )
        }

        Text(
            text = "${reservationData.numberOfPeople}명이 앉을 수 있는 테이블을 추천합니다",
            fontSize = 14.sp,
            color = Color(0xFF7F8C8D)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(availableTables) { table ->
                TableOptionCard(
                    table = table,
                    isSelected = reservationData.selectedTable == table.id,
                    onClick = { reservationData.selectedTable = table.id }
                )
            }
        }
    }
}

data class TableOption(
    val name: String,
    val capacity: String,
    val isAvailable: Boolean,
    val id: String
)

@Composable
fun TableOptionCard(
    table: TableOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = table.isAvailable, onClick = onClick),
        shape = RoundedCornerShape(Dimens.Default),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) TableOptionColor else White
        ),
        border = if (isSelected) BorderStroke(Dimens.Nano, reservationCountColor) else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(Dimens.Tiny)
            ) {
                Text(
                    text = table.name,
                    style = AppTextStyle.Body.copy(
                        fontWeight = FontWeight.Bold,
                        color = IconTextColor
                    )
                )
                Text(
                    text = table.capacity,
                    style = AppTextStyle.Body.copy(fontSize = 14.sp, color = IconColor)
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens.Small),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            if (table.isAvailable) Color(0xFFD5EDDA) else CardBorderTransparentColor,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = if (table.isAvailable) "예약가능" else "예약불가",
                        style = AppTextStyle.Body.copy(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (table.isAvailable) Color(0xFF27AE60) else Color(0xFF95A5A6)
                        )
                    )
                }

                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = reservationCountColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

data class MenuItemData(
    val menuId: String,
    val name: String,
    val price: Int,
    var quantity: Int = 0
)

// 5단계: 메뉴 선택
@Composable
fun MenuSelectStep(reservationData: ReservationData, storeId: Long) {
    val menuCategories = remember {
        mapOf(
            "베이커리" to listOf(
                MenuItemData("1", "크로와상", 3500),
                MenuItemData("2", "버터 향이 가득한", 0)
            ),
            "브런치" to listOf(
                MenuItemData("3", "샌드위치", 8500),
                MenuItemData("4", "신선한 야채와 햄", 0)
            ),
            "메인" to listOf(
                MenuItemData("5", "마르게리타 피자", 16000),
                MenuItemData("6", "신선한 토마토와 모짜렐라", 0),
                MenuItemData("7", "파스타", 14000),
                MenuItemData("8", "정통 이탈리안 파스타", 0)
            )
        )
    }

    LaunchedEffect(reservationData.selectedMenus) {
        reservationData.totalPrice = reservationData.selectedMenus.values
            .sumOf { it.price * it.quantity }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(Dimens.Large)
        ) {
            menuCategories.forEach { (category, items) ->
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(Dimens.Default)) {
                        Text(
                            text = category,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2C3E50)
                        )
                        items.forEach { menu ->
                            if (menu.price > 0) {
                                MenuItemCard(
                                    menu = menu,
                                    quantity = reservationData.selectedMenus[menu.menuId]?.quantity
                                        ?: 0,
                                    onQuantityChanged = { newQuantity ->
                                        if (newQuantity > 0) {
                                            reservationData.selectedMenus[menu.menuId] =
                                                menu.copy(quantity = newQuantity)
                                        } else {
                                            reservationData.selectedMenus.remove(menu.menuId)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // 총 주문 금액
        if (reservationData.totalPrice > 0) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "총 주문 금액",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C3E50)
                    )
                    Text(
                        text = "${String.format("%,d", reservationData.totalPrice)}원",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1565C0)
                    )
                }
            }
        }
    }
}

@Composable
fun MenuItemCard(
    menu: MenuItemData,
    quantity: Int,
    onQuantityChanged: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = menu.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C3E50)
                )
                Text(
                    text = "${String.format("%,d", menu.price)}원",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3498DB)
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { if (quantity > 0) onQuantityChanged(quantity - 1) },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        Icons.Default.Remove,
                        contentDescription = null,
                        tint = Color(0xFF7F8C8D)
                    )
                }

                Text(
                    text = quantity.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C3E50),
                    modifier = Modifier.widthIn(min = 24.dp),
                    textAlign = TextAlign.Center
                )

                IconButton(
                    onClick = { onQuantityChanged(quantity + 1) },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = null,
                        tint = Color(0xFF3498DB)
                    )
                }
            }
        }
    }
}

// 6단계: 예약 확인
@Composable
fun ConfirmationStep(
    reservationData: ReservationData,
    storeName: String,
    onConfirm: () -> Unit
) {
    var selectedPaymentMethod by remember { mutableStateOf("카드결제") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA)),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Text(
                text = "예약 정보 확인",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2C3E50)
            )
        }

        // 예약 정보
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    InfoRow("매장", storeName)
                    InfoRow("인원", "${reservationData.numberOfPeople}명")
                    InfoRow(
                        "날짜",
                        reservationData.selectedDate?.format(DateTimeFormatter.ofPattern("yyyy. M. d."))
                            ?: ""
                    )
                    InfoRow("시간", reservationData.selectedTime ?: "")
                    InfoRow("좌석", getTableName(reservationData.selectedTable))
                }
            }
        }

        // 주문 메뉴
        if (reservationData.selectedMenus.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "주문 메뉴",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2C3E50)
                        )

                        reservationData.selectedMenus.values.forEach { menu ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${menu.name} × ${menu.quantity}",
                                    fontSize = 14.sp,
                                    color = Color(0xFF2C3E50)
                                )
                                Text(
                                    text = "${String.format("%,d", menu.price * menu.quantity)}원",
                                    fontSize = 14.sp,
                                    color = Color(0xFF2C3E50)
                                )
                            }
                        }

                        Divider(color = Color(0xFFE0E0E0))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "총 금액",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2C3E50)
                            )
                            Text(
                                text = "${String.format("%,d", reservationData.totalPrice)}원",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF3498DB)
                            )
                        }
                    }
                }
            }
        }

        // 결제 방법
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "결제 방법",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C3E50)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        PaymentMethodButton(
                            icon = Icons.Default.CreditCard,
                            label = "카드결제",
                            isSelected = selectedPaymentMethod == "카드결제",
                            onClick = { selectedPaymentMethod = "카드결제" },
                            modifier = Modifier.weight(1f)
                        )
                        PaymentMethodButton(
                            icon = Icons.Default.PhoneAndroid,
                            label = "모바일결제",
                            isSelected = selectedPaymentMethod == "모바일결제",
                            onClick = { selectedPaymentMethod = "모바일결제" },
                            modifier = Modifier.weight(1f)
                        )
                        PaymentMethodButton(
                            icon = Icons.Default.Money,
                            label = "현장결제",
                            isSelected = selectedPaymentMethod == "현장결제",
                            onClick = { selectedPaymentMethod = "현장결제" },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color(0xFF7F8C8D)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2C3E50)
        )
    }
}

@Composable
fun PaymentMethodButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFE3F2FD) else Color(0xFFF8F9FA)
        ),
        border = if (isSelected) BorderStroke(2.dp, Color(0xFF3498DB)) else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) Color(0xFF3498DB) else Color(0xFF7F8C8D),
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) Color(0xFF3498DB) else Color(0xFF2C3E50)
            )
        }
    }
}

fun getTableName(tableId: String?): String {
    return when (tableId) {
        "table_1" -> "중앙 4인석"
        "table_2" -> "대형 8인석"
        "table_3" -> "입구 4인석"
        "table_4" -> "창가 2인석"
        else -> "미선택"
    }
}
