package com.example.searchplacement.ui.utils

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale

fun parseReservationDateTime(dateTimeString: String): Pair<String, String> {
    return try {
        val dateTime = LocalDateTime.parse(dateTimeString)
        val dayOfWeek = dateTime.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN) // (화)
        val dateText = "${dateTime.monthValue}월 ${dateTime.dayOfMonth}일 ($dayOfWeek)"

        val hour = dateTime.hour
        val minute = dateTime.minute
        val ampm = if (hour < 12) "오전" else "오후"
        val formattedHour = when {
            hour == 0 -> 12
            hour > 12 -> hour - 12
            else -> hour
        }
        val formattedMinute = String.format("%02d", minute)
        val timeText = "$ampm ${formattedHour}시 ${formattedMinute}분"

        dateText to timeText
    } catch (e: Exception) {
        "날짜 오류" to "시간 오류"
    }
}

fun sortDay(day: String): Int = when (day) {
    "월요일" -> 1
    "화요일" -> 2
    "수요일" -> 3
    "목요일" -> 4
    "금요일" -> 5
    "토요일" -> 6
    "일요일" -> 7
    else -> 8
}




fun getDatesForMonth(year: Int, month: Int): List<LocalDate> {
    val firstOfMonth = LocalDate.of(year, month, 1)
    val lastOfMonth = firstOfMonth.withDayOfMonth(firstOfMonth.lengthOfMonth())

    val startDayOfWeek = firstOfMonth.dayOfWeek.value % 7
    val totalDays = startDayOfWeek + lastOfMonth.dayOfMonth
    val totalWeeks = (totalDays + 6) / 7
    val totalCells = totalWeeks * 7

    val firstDate = firstOfMonth.minusDays(startDayOfWeek.toLong())

    return List(totalCells) { firstDate.plusDays(it.toLong()) }
}


fun getDayOfWeek(date: LocalDate): String {
    val days = listOf("일", "월", "화", "수", "목", "금", "토")
    return "${days[date.dayOfWeek.value % 7]}요일"
}

fun getDayOfWeekKey(date: LocalDate): String {
    return when (date.dayOfWeek) {
        DayOfWeek.MONDAY -> "월요일"
        DayOfWeek.TUESDAY -> "화요일"
        DayOfWeek.WEDNESDAY -> "수요일"
        DayOfWeek.THURSDAY -> "목요일"
        DayOfWeek.FRIDAY -> "금요일"
        DayOfWeek.SATURDAY -> "토요일"
        DayOfWeek.SUNDAY -> "일요일"
    }
}


fun generateTimeSlots(hours: String): List<String> {
    val parts = hours.split("-").map { it.trim() }
    if (parts.size != 2) return emptyList()

    val start = LocalTime.parse(parts[0], DateTimeFormatter.ofPattern("HH:mm"))
    val end = LocalTime.parse(parts[1], DateTimeFormatter.ofPattern("HH:mm"))

    val slots = mutableListOf<String>()
    var time = start
    while (time.isBefore(end)) {
        slots.add(time.format(DateTimeFormatter.ofPattern("HH:mm")))
        time = time.plusMinutes(60)
    }
    return slots
}

fun isCancellable(reservationTime: String): Boolean {
    return try {
        val targetDateTime = LocalDateTime.parse(reservationTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        val now = LocalDateTime.now()
        val diffMinutes = ChronoUnit.MINUTES.between(now, targetDateTime)
        diffMinutes > 30
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}
