package com.example.searchplacement.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import okhttp3.OkHttpClient


@Composable
fun rememberImageLoaderWithToken(token: String): ImageLoader {
    val context = LocalContext.current
    return remember(token) {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(newRequest)
            }
            .build()

        ImageLoader.Builder(context)
            .okHttpClient(client)
            .build()
    }
}
