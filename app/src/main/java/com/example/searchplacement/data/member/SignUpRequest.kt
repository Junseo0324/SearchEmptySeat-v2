package com.example.searchplacement.data.member

data class SignUpRequest(
    val email: String = "",
    val password: String = "",
    val passwordConfirm: String="",
    val name: String = "",
    val phone: String = "",
    val location: String = "",
    val userType: String = "USER"
)

