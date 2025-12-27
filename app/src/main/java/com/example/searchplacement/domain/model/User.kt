package com.example.searchplacement.domain.model

data class User(
    val userId: String,
    val name: String,
    val email: String,
    val phone: String,
    val location: String,
    val image: String,
    val token: String
)
