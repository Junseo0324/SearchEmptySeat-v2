package com.example.searchplacement.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import com.example.searchplacement.util.TokenManager
import okhttp3.OkHttpClient


@Composable
fun rememberImageLoaderWithToken(token: String): ImageLoader {
    val context = LocalContext.current
    val token = TokenManager.getToken()
    return remember(token) {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder().apply {
                    token?.let { addHeader("Authorization", "Bearer $it") }
                }.build()
                chain.proceed(newRequest)
            }
            .build()

        ImageLoader.Builder(context)
            .okHttpClient(client)
            .build()
    }
}
