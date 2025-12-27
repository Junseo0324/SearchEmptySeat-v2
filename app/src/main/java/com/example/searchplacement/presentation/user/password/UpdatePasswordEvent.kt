package com.example.searchplacement.presentation.user.password

sealed interface UpdatePasswordEvent {
    data object PasswordUpdated : UpdatePasswordEvent
    data class ShowError(val message: String) : UpdatePasswordEvent
}
