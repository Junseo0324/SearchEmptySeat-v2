package com.example.searchplacement.data.mapper

import com.example.searchplacement.data.dto.login.LoginResponse
import com.example.searchplacement.data.local.UserEntity
import com.example.searchplacement.domain.model.LoginModel

fun LoginResponse.toUserEntity(): UserEntity {
    return UserEntity(
        userId = userId,
        name = name,
        email = email,
        phone = phone,
        userType = userType,
        token = token,
        location = location,
        image = image.firstOrNull().orEmpty()
    )
}

fun LoginResponse.toModel(): LoginModel {
    return LoginModel(
        userId = userId.toLong(),
        name = name,
        email = email,
        phone = phone,
        userType = userType,
        location = location,
        imageUrl = image.firstOrNull().orEmpty()
    )
}