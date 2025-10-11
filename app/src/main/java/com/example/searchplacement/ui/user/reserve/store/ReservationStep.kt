package com.example.searchplacement.ui.user.reserve.store

import com.example.searchplacement.data.reserve.ReservationData

enum class ReservationStep {
    PEOPLE_COUNT,
    DATE_SELECT,
    TIME_SELECT,
    TABLE_SELECT,
    MENU_SELECT,
    CONFIRMATION
}

fun isStepComplete(step: ReservationStep, data: ReservationData): Boolean {
    return when (step) {
        ReservationStep.PEOPLE_COUNT -> data.numberOfPeople in 1..8
        ReservationStep.DATE_SELECT -> data.selectedDate != null
        ReservationStep.TIME_SELECT -> data.selectedTime != null
        ReservationStep.TABLE_SELECT -> data.selectedTable != null
        ReservationStep.MENU_SELECT -> true
        ReservationStep.CONFIRMATION -> true
    }
}