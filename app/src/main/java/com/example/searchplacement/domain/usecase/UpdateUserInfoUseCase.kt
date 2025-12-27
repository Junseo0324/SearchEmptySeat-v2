package com.example.searchplacement.domain.usecase

import android.content.Context
import android.net.Uri
import com.example.searchplacement.core.util.Result
import com.example.searchplacement.data.local.UserEntity
import com.example.searchplacement.data.mapper.toModel
import com.example.searchplacement.data.member.MyInfoUpdateRequest
import com.example.searchplacement.domain.model.User
import com.example.searchplacement.domain.repository.AuthRepository
import com.example.searchplacement.domain.repository.UserRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.FileNotFoundException
import javax.inject.Inject

class UpdateUserInfoUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    @ApplicationContext private val context: Context
) {
    suspend fun execute(
        user: User,
        imageUri: Uri?
    ): Result<User, String> {
        val request = MyInfoUpdateRequest(
            email = user.email,
            name = user.name,
            password = null,
            location = user.location
        )

        val imageFile = imageUri?.let { getImageFilePart(context, it) }

        return try {
            val userIdLong = user.userId.toLongOrNull() ?: return Result.Error("Invalid User ID")
            val response = authRepository.updateUserInfo(userIdLong, request, imageFile)
            if (response.status == "success" && response.data != null) {
                val apiResponse = response
                val updatedEntity = UserEntity(
                    userId = user.userId,
                    name = user.name,
                    email = user.email,
                    phone = user.phone,
                    userType = "USER",
                    location = user.location,
                    token = user.token,
                    image = apiResponse.data!!["image"] as? String ?: user.image
                )
                userRepository.saveUser(updatedEntity)
                Result.Success(updatedEntity.toModel())
            } else {
                Result.Error(response.message ?: "정보 수정 실패")
            }
        } catch (e: Exception) {
            Result.Error("네트워크 오류: ${e.message}")
        }
    }

    private fun getImageFilePart(context: Context, uri: Uri): MultipartBody.Part {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw FileNotFoundException("파일을 찾을 수 없습니다.")
        val requestFile = inputStream.readBytes().toRequestBody("image/*".toMediaTypeOrNull())

        return MultipartBody.Part.createFormData("image", uri.lastPathSegment ?: "image", requestFile)
    }
}
