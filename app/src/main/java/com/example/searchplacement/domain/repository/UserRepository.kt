package com.example.searchplacement.domain.repository

import com.example.searchplacement.data.local.UserEntity

interface UserRepository {
    suspend fun saveUser(user: UserEntity)
    suspend fun getUser(): UserEntity?
    suspend fun clearUserData()
}
