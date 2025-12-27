package com.example.searchplacement.data.dto.login

data class LoginResponse(
    val image: List<String>,
    val phone: String,
    val name: String,
    val userType: String,
    val userId : String,
    val email: String,
    val location: String,
    val token: String
)