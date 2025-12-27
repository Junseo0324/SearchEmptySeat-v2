package com.example.searchplacement.presentation.user.password

data class CheckPasswordState(
    val password: String = "",
    val showPassword: Boolean = false,
    val isLoading: Boolean = false
)
