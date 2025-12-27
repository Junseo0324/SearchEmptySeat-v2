package com.example.searchplacement.presentation.user.auth.register

sealed interface RegisterEvent {
    data object RegisterSuccess : RegisterEvent
    data class ShowSnackbar(val message: String) : RegisterEvent
}
