package com.example.searchplacement.domain.model

data class LoginModel(
    val userId: Long,
    val name: String,
    val email: String,
    val phone: String,
    val userType: String,
    val location: String,
    val imageUrl: String
)
