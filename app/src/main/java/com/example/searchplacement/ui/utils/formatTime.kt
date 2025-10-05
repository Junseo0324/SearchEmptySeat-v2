package com.example.searchplacement.ui.utils

import java.time.LocalDateTime
import java.time.format.TextStyle
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
