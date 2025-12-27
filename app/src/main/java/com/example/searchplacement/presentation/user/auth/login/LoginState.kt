package com.example.searchplacement.presentation.user.auth.login

import com.example.searchplacement.domain.model.LoginModel

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val loginUser: LoginModel? = null
)
