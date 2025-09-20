    package com.example.searchplacement.repository

    import com.example.searchplacement.data.local.UserDao
    import com.example.searchplacement.data.local.UserEntity
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.withContext
    import javax.inject.Inject
    import javax.inject.Singleton


    @Singleton
    class UserRepository @Inject constructor(private val userDao: UserDao) {
        suspend fun saveUser(user: UserEntity) {
            withContext(Dispatchers.IO) {
                userDao.clearUserData()
                userDao.insertUser(user)
            }
        }


        suspend fun getUser(): UserEntity? {
            return withContext(Dispatchers.IO) {
                userDao.getUser()
            }
        }
        suspend fun clearUserData() {
            withContext(Dispatchers.IO) {
                userDao.clearUserData()
            }
        }
    }