package com.example.searchplacement.presentation.user.auth.register

import android.net.Uri

data class RegisterState(
    val email: String = "",
    val password: String = "",
    val passwordConfirm: String = "",
    val name: String = "",
    val phone: String = "",
    val location: String = "",
    val userType: String? = null,
    val imageUri: Uri? = null,
    val isLoading: Boolean = false
)
