package com.example.searchplacement.presentation.user.password.check

data class CheckPasswordState(
    val password: String = "",
    val showPassword: Boolean = false,
    val isLoading: Boolean = false
)
