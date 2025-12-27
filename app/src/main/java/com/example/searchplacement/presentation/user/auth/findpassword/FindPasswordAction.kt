package com.example.searchplacement.presentation.user.auth.findpassword

sealed interface FindPasswordAction {
    data class OnEmailChanged(val email: String) : FindPasswordAction
    data object OnFindPasswordClick : FindPasswordAction
    data object OnBackClick : FindPasswordAction
}
