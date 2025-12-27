package com.example.searchplacement.presentation.user.password

sealed interface CheckPasswordEvent {
    data object PasswordCorrect : CheckPasswordEvent
    data class ShowError(val message: String) : CheckPasswordEvent
}
