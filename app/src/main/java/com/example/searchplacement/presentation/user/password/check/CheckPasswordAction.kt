package com.example.searchplacement.presentation.user.password.check

sealed interface CheckPasswordAction {
    data class OnPasswordChanged(val password: String) : CheckPasswordAction
    data object OnTogglePasswordVisibility : CheckPasswordAction
    data object OnConfirmClick : CheckPasswordAction
}
