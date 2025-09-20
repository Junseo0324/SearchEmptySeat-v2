package com.example.searchplacement.data.member

data class ApiResponse<T>(
    val status: String,
    val message: String,
    val data: T?
)
