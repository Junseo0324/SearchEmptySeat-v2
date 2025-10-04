package com.example.searchplacement.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchplacement.data.local.UserEntity
import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.member.LoginRequest
import com.example.searchplacement.data.member.LoginResponse
import com.example.searchplacement.data.member.MyInfoUpdateRequest
import com.example.searchplacement.repository.AuthRepository
import com.example.searchplacement.repository.UserRepository
import com.example.searchplacement.util.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.FileNotFoundException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _user = MutableStateFlow<UserEntity?>(null)
    val user = _user.asStateFlow()

    private val _loginResult = MutableStateFlow<ApiResponse<LoginResponse>?>(null)
    val loginResult = _loginResult.asStateFlow()

    private val _passwordUpdateResult = MutableStateFlow<ApiResponse<String>?>(null)
    val passwordUpdateResult = _passwordUpdateResult.asStateFlow()

    private val _userInfoUpdateResult = MutableStateFlow<ApiResponse<Map<String,Any>>?>(null)
    val userInfoUpdateResult = _userInfoUpdateResult.asStateFlow()

    private val _logoutEvent = MutableSharedFlow<Unit>()
    val logoutEvent = _logoutEvent.asSharedFlow()

    init {
        getUserData()
    }

    private fun getUserData() {
        viewModelScope.launch {
            _user.value = userRepository.getUser()
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.clearUserData()
            _user.value = null

            TokenManager.clearToken()
            _logoutEvent.emit(Unit)
        }
    }

    fun authPassword(email: String, password: String) {
        viewModelScope.launch {
            val response = authRepository.login(LoginRequest(email, password))
            if (response.isSuccessful && response.body() != null) {
                _loginResult.value = response.body()
            } else {
                _loginResult.value = ApiResponse(
                    status = "fail",
                    message = "Auth failed",
                    data = null
                )
            }
        }
    }


    fun updatePassword(userId: Long, newPassword: String) {
        viewModelScope.launch {
            try {
                val response = authRepository.updatePassword(userId, newPassword)
                if (response.isSuccessful && response.body() != null) {
                    _passwordUpdateResult.value = response.body()
                } else {
                    _passwordUpdateResult.value = ApiResponse(
                        status = "fail",
                        message = response.errorBody()?.string() ?: "비밀번호 변경 실패",
                        data = null
                    )
                }
            } catch (e: Exception) {
                _passwordUpdateResult.value = ApiResponse(
                    status = "fail",
                    message = "네트워크 오류: ${e.message}",
                    data = null
                )
            }
        }
    }

    fun updateUserInfo(
        userId: Long,
        editedEmail: String?,
        editedName: String?,
        editedPassword: String?,
        editedLocation: String?,
        imageFile: MultipartBody.Part?
    ) {
        val request = MyInfoUpdateRequest(
            email = editedEmail,
            name = editedName,
            password = editedPassword,
            location = editedLocation
        )

        viewModelScope.launch {
            try {
                val response = authRepository.updateUserInfo(userId, request, imageFile)
                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()!!
                    _userInfoUpdateResult.value = apiResponse

                    val updatedEntity = UserEntity(
                        userId = userId.toString(),
                        name = editedName ?: _user.value?.name.orEmpty(),
                        email = editedEmail ?: _user.value?.email.orEmpty(),
                        phone = _user.value?.phone.orEmpty(),
                        userType = _user.value?.userType.orEmpty(),
                        location = editedLocation ?: _user.value?.location.orEmpty(),
                        token = _user.value?.token.orEmpty(),
                        image = apiResponse.data?.get("image") as? String ?: _user.value?.image.orEmpty()
                    )
                    userRepository.saveUser(updatedEntity)

                    getUserData()
                } else {
                    _userInfoUpdateResult.value = ApiResponse(
                        status = "fail",
                        message = response.errorBody()?.string() ?: "정보 수정 실패",
                        data = null
                    )
                }
            } catch (e: Exception) {
                Log.d("updateInfo", "updateUserInfo: ${e.message}")
                _userInfoUpdateResult.value = ApiResponse(
                    status = "fail",
                    message = "네트워크 오류: ${e.message}",
                    data = null
                )
            }
        }
    }

    fun getImageFilePart(context: Context, uri: Uri): MultipartBody.Part {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw FileNotFoundException("파일을 찾을 수 없습니다.")
        val requestFile = inputStream.readBytes().toRequestBody("image/*".toMediaTypeOrNull())

        return MultipartBody.Part.createFormData("image", uri.lastPathSegment ?: "image", requestFile)
    }




}