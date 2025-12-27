package com.example.searchplacement.presentation.user.auth.login

sealed class LoginEvent {
    data class ShowSnackbar(val message: String) : LoginEvent()
}
