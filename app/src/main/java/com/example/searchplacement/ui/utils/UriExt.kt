package com.example.searchplacement.ui.utils

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.FileNotFoundException

fun toMultipartPart(context: Context, uri: Uri): MultipartBody.Part {
    val inputStream = context.contentResolver.openInputStream(uri)
        ?: throw FileNotFoundException("파일을 찾을 수 없습니다.")
    val requestFile = inputStream.readBytes().toRequestBody("image/*".toMediaTypeOrNull())

    return MultipartBody.Part.createFormData("images", uri.lastPathSegment ?: "images", requestFile)
}
