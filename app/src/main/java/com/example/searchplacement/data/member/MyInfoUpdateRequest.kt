package com.example.searchplacement.data.member

data class MyInfoUpdateRequest(
    val email: String? = null,
    val name: String? = null,
    val password: String? = null,
    val location: String? = null
)

