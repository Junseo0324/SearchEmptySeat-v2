package com.example.searchplacement.presentation.user.password.update

sealed interface UpdatePasswordEvent {
    data object PasswordUpdated : UpdatePasswordEvent
    data class ShowError(val message: String) : UpdatePasswordEvent
}
