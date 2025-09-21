package com.example.searchplacement.util


object TokenManager {
    @Volatile
    private var cachedToken: String? = null

    fun setToken(token: String?) {
        cachedToken = token
    }

    fun getToken(): String? = cachedToken

    fun clearToken() {
        cachedToken = null
    }
}
