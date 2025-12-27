package com.example.searchplacement.data.mapper

import com.example.searchplacement.data.local.UserEntity
import com.example.searchplacement.domain.model.User

fun UserEntity.toDomain(): User {
    return User(
        userId = this.userId,
        name = this.name,
        email = this.email,
        phone = this.phone,
        location = this.location,
        image = this.image,
        token = this.token
    )
}

fun User.toEntity(userType: String = "USER"): UserEntity {
    return UserEntity(
        userId = this.userId,
        name = this.name,
        email = this.email,
        phone = this.phone,
        userType = userType,
        location = this.location,
        token = this.token,
        image = this.image
    )
}
