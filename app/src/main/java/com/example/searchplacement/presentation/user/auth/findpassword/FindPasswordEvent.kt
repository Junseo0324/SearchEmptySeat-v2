package com.example.searchplacement.presentation.user.auth.findpassword

sealed interface FindPasswordEvent {
    data object FindPasswordSuccess : FindPasswordEvent
    data class ShowSnackbar(val message: String) : FindPasswordEvent
}
