package com.example.searchplacement.presentation.user.password

sealed interface UpdatePasswordAction {
    data class OnNewPasswordChanged(val password: String) : UpdatePasswordAction
    data class OnConfirmPasswordChanged(val password: String) : UpdatePasswordAction
    data object OnToggleNewPasswordVisibility : UpdatePasswordAction
    data object OnToggleConfirmPasswordVisibility : UpdatePasswordAction
    data object OnUpdateClick : UpdatePasswordAction
}
