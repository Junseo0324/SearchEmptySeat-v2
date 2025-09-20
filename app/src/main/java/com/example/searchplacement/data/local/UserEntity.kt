package com.example.searchplacement.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey val userId: String,
    val name: String,
    val email: String,
    val phone: String,
    val userType: String,
    val location: String,
    val token: String,
    val image: String
)
