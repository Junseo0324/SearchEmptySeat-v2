package com.example.searchplacement.presentation.user.auth.login

sealed class LoginAction {
    data class OnEmailChanged(val email: String) : LoginAction()
    data class OnPasswordChanged(val password: String) : LoginAction()
    object OnLoginClick : LoginAction()
    object OnRegisterClick : LoginAction()
    object OnFindPasswordClick : LoginAction()
}
