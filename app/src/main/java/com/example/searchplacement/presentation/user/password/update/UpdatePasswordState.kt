package com.example.searchplacement.presentation.user.password.update

data class UpdatePasswordState(
    val newPassword: String = "",
    val confirmPassword: String = "",
    val showNewPassword: Boolean = false,
    val showConfirmPassword: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val passwordMismatch: Boolean = false
)
