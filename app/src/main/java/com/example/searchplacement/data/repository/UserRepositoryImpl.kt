package com.example.searchplacement.data.repository

import com.example.searchplacement.data.local.UserDao
import com.example.searchplacement.data.local.UserEntity
import com.example.searchplacement.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(private val userDao: UserDao) : UserRepository {
    override suspend fun saveUser(user: UserEntity) {
        withContext(Dispatchers.IO) {
            userDao.clearUserData()
            userDao.insertUser(user)
        }
    }

    override suspend fun getUser(): UserEntity? {
        return withContext(Dispatchers.IO) {
            userDao.getUser()
        }
    }

    override suspend fun clearUserData() {
        withContext(Dispatchers.IO) {
            userDao.clearUserData()
        }
    }
}