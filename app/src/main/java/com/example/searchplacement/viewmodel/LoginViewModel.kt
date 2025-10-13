package com.example.searchplacement.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchplacement.data.local.UserEntity
import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.member.FindPasswordRequest
import com.example.searchplacement.data.member.LoginRequest
import com.example.searchplacement.data.member.LoginResponse
import com.example.searchplacement.data.member.SignUpRequest
import com.example.searchplacement.repository.AuthRepository
import com.example.searchplacement.repository.UserRepository
import com.example.searchplacement.util.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _loginResult = MutableStateFlow<ApiResponse<LoginResponse>?>(null)
    val loginResult = _loginResult.asStateFlow()


    private val _findPasswordResult = MutableSharedFlow<ApiResponse<Map<String, String>>>(replay = 0)
    val findPasswordResult = _findPasswordResult.asSharedFlow()



    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = authRepository.login(LoginRequest(email, password))
                Log.d("TAG", "login: ${response.body()}")
                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!.data

                    val userEntity = loginResponse?.let {
                        UserEntity(
                            userId = it.userId,
                            name = it.name,
                            email = it.email,
                            phone = it.phone,
                            userType = it.userType,
                            token = it.token,
                            location = it.location,
                            image = it.image.firstOrNull() ?: ""
                        )
                    }

                    if (userEntity != null) {
                        userRepository.saveUser(userEntity)
                        TokenManager.setToken(userEntity.token)
                    }

                    _loginResult.value = response.body()
                } else {
                    _loginResult.value = ApiResponse(
                        status = "fail",
                        message = "로그인 실패",
                        data = null
                    )
                }
            } catch (e: Exception) {
                _loginResult.value = ApiResponse(
                    status = "error",
                    message = e.localizedMessage ?: "네트워크 연결 오류입니다.",
                    data = null
                )
            }
        }
    }


    fun register(email: String, password: String, name: String, phone: String, location: String, userType: String, imageFile: MultipartBody.Part?) {
        viewModelScope.launch {
            val signUpRequest = SignUpRequest(
                email = email,
                password = password,
                name = name,
                phone = phone,
                location = location,
                userType = userType
            )

            authRepository.register(signUpRequest, imageFile)

        }
    }

    fun findPassword(email: String) {
        viewModelScope.launch {
            try {
                val response = authRepository.forgotPassword(FindPasswordRequest(email))
                if (response.isSuccessful && response.body() != null) {
                    _findPasswordResult.emit(response.body()!!)
                } else {
                    _findPasswordResult.emit(
                        ApiResponse(
                            status = "error",
                            message = "비밀번호 찾기 실패",
                            data = mapOf("code" to "ERROR-0003")
                        )
                    )
                }
            } catch (e: Exception) {
                _findPasswordResult.emit(
                    ApiResponse(
                        status = "error",
                        message = e.localizedMessage ?: "네트워크 오류",
                        data = mapOf("code" to "ERROR-0003")
                    )
                )
            }
        }
    }



}
