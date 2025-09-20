package com.example.searchplacement.data.member

data class SignUpRequest(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val phone: String = "",
    val location: String = "",
    val userType: String = "USER"
)
data class SignUpResponse(
    val userId: Int,
    val email: String,
    val name: String,
    val phone: String,
    val location: String,
    val userType: String
)

