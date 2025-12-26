package com.example.searchplacement.presentation.owner.info

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.searchplacement.data.store.BusinessHour
import com.example.searchplacement.data.store.StoreRequest
import com.example.searchplacement.core.navigation.OwnerBottomNavItem
import com.example.searchplacement.presentation.theme.ButtonMainColor
import com.example.searchplacement.presentation.user.home.StoreListViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun BusinessHourScreen(navController: NavHostController) {
    val storeListViewModel: StoreListViewModel = hiltViewModel()
    val daysOfWeek = listOf("월요일", "화요일", "수요일", "목요일", "금요일", "토요일", "일요일")
    var businessHours by remember {
        mutableStateOf(
            daysOfWeek.associateWith { BusinessHour("11:00", "22:00", true) }
        )
    }

    val store by storeListViewModel.selectedStore.collectAsState()

    var regularHolidays by remember { mutableStateOf<Map<String, Int>>(emptyMap()) }


    // 임시 휴무용
    var tempHolidayDate by remember { mutableStateOf("") }
    var holidays by remember { mutableStateOf(listOf<String>()) }

    LaunchedEffect(Unit) {
        storeListViewModel.fetchMyStores()
    }

    LaunchedEffect(store) {
        store?.let {
            holidays = it.temporaryHolidays ?: emptyList()

            it.regularHolidays?.let { regHolidays ->
                val newBusinessHours = daysOfWeek.associateWith { day ->
                    val isOpen = regHolidays[day] == 0
                    val current = businessHours[day] ?: BusinessHour("11:00", "22:00", true)
                    current.copy(open = isOpen)
                }
                businessHours = newBusinessHours
                regularHolidays = regHolidays
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        SectionTitle("영업 시간")
        HourSelection(
            businessHours = businessHours,
            onChange = { day, newHour ->
                businessHours = businessHours.toMutableMap().apply { put(day, newHour) }
            }
        )
        SectionTitle("임시 휴무 추가")

        HolidaySelector(
            selectedDate = tempHolidayDate,
            onDateSelected = { tempHolidayDate = it },
            onAddHoliday = {
                if (tempHolidayDate.isNotEmpty() && tempHolidayDate !in holidays) {
                    holidays = holidays + tempHolidayDate
                    Log.d("HolidaySelector", "📆 추가된 임시 휴무일: $tempHolidayDate")
                    Log.d("HolidaySelector", "📝 현재 임시 휴무 목록: $holidays")
                    Log.d("HolidaySelector", "📝 현재 임시 휴무 목록: $regularHolidays")
                    tempHolidayDate = ""
                }
            }
        )
        HolidayList(
            holidays = holidays,
            onRemove = {
                holidays = holidays - it
                Log.d("HolidaySelector", "❌ 삭제된 임시 휴무일: $it")
                Log.d("HolidaySelector", "📝 현재 임시 휴무 목록: $holidays")
                Log.d("HolidaySelector", "📝 현재 임시 휴무 목록: $regularHolidays")
            }
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                val apiBusinessHour = toApiBusinessHours(businessHours)
                val regularHolidaysMap = businessHours.mapValues { if (it.value.open) 0 else 1 }

                val request = StoreRequest(
                    storeName = store?.storeName ?: "",
                    location = store?.location ?: "",
                    description = store?.description ?: "",
                    businessRegistrationNumber = store?.businessRegistrationNumber ?: "",
                    bank = store?.bank ?: "",
                    accountNumber = store?.accountNumber ?: "",
                    depositor = store?.depositor ?: "",
                    businessHours = apiBusinessHour,
                    category = store?.category ?: emptyList(),
                    temporaryHolidays = holidays,
                    regularHolidays = regularHolidaysMap
                )
                Log.d("BusinessHourScreen", "request : ${request} ")


                storeListViewModel.updateStore(
                    storeId = store?.storePK ?: 0L,
                    request = request,
                    images = null
                )
                navController.navigate(OwnerBottomNavItem.Store.screenRoute)
            },
            colors = ButtonDefaults.buttonColors(containerColor = ButtonMainColor)
        ) {
            Text("저장")
        }
    }
}

// 요일별 영업시간 설정
@Composable
fun HourSelection(
    businessHours: Map<String, BusinessHour>,
    onChange: (String, BusinessHour) -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween) {
        Text("요일", Modifier.width(32.dp), fontWeight = FontWeight.Bold)
        Text("영업 유/무",fontWeight = FontWeight.Bold)
    }
    businessHours.forEach { (day, hour) ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(day, modifier = Modifier.width(32.dp))

            // 시작 시간
            OutlinedTextField(
                value = hour.start,
                onValueChange = { onChange(day, hour.copy(start = it)) },
                label = { Text("시작") },
                modifier = Modifier
                    .width(120.dp)
                    .padding(horizontal = 4.dp)
            )

            Text("-", modifier = Modifier.padding(horizontal = 4.dp))

            OutlinedTextField(
                value = hour.end,
                onValueChange = { onChange(day, hour.copy(end = it)) },
                label = { Text("마감") },
                modifier = Modifier
                    .width(120.dp)
                    .padding(horizontal = 4.dp)
            )

            Switch(
                checked = hour.open,
                onCheckedChange = { onChange(day, hour.copy(open = it)) },
                colors = SwitchDefaults.colors(
                    checkedThumbColor =Color(0xFFC5E6F5),
                    checkedTrackColor = Color(0xffAFD4EF),
                    uncheckedThumbColor = Color(0xFFBF2A2A),
                    uncheckedTrackColor = Color(0xFFFED6D6)
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HolidaySelector(
    selectedDate: String,
    onDateSelected: (String) -> Unit,
    onAddHoliday: () -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .weight(1f)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { showDatePicker = true }
        ) {
            OutlinedTextField(
                value = selectedDate,
                onValueChange = {},
                readOnly = true,
                enabled = false,
                label = { Text("날짜 (예: 2024-06-06)") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        Button(
            onClick = onAddHoliday,
            modifier = Modifier.padding(start = 8.dp),
            colors = ButtonDefaults.buttonColors(ButtonMainColor)
        ) {
            Text("추가")
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val localDate = LocalDate.ofEpochDay(millis / (24 * 60 * 60 * 1000))
                            onDateSelected(localDate.format(formatter))
                        }
                        showDatePicker = false
                    }
                ) { Text("확인", color = Color.Black) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("취소", color = Color.Black) }
            },
            colors = DatePickerDefaults.colors(
                containerColor = Color.White
            )
        ) {
            DatePicker(state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = Color.White,
                    selectedDayContainerColor = Color(0xFF96A2FF),
                    dayContentColor = Color.Black,
                    titleContentColor = Color.Black,
                    headlineContentColor = Color.Black,
                    weekdayContentColor = Color.Black,
                    subheadContentColor = Color.Black,
                    todayContentColor = Color.Black


                )
            )
        }
    }
}


@Composable
fun HolidayList(
    holidays: List<String>,
    onRemove: (String) -> Unit
) {
    Column {
        holidays.forEach { date ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(date, modifier = Modifier.weight(1f))
                Text("임시 휴무", color = Color.Red, modifier = Modifier.padding(end = 8.dp))
                Button(onClick = { onRemove(date) },
                    colors = ButtonDefaults.buttonColors(ButtonMainColor)) {
                    Text("삭제")
                }
            }
        }
    }
}

fun toApiBusinessHours(businessHours: Map<String, BusinessHour>): Map<String, String> {
    return businessHours.filter { it.value.open }
        .mapValues { "${it.value.start} - ${it.value.end}" }
}


//
//@Preview(showBackground = true)
//@Composable
//fun BusinessHourScreenPreview() {
//    BusinessHourScreen()
//}