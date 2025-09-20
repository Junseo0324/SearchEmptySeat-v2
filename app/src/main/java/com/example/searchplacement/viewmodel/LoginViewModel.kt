package com.example.searchplacement.viewmodel

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


    private val _registerResult = MutableStateFlow<ApiResponse<Map<String, Any>>?>(null)
    val registerResult = _registerResult.asStateFlow()


    private val _findPasswordResult = MutableSharedFlow<ApiResponse<Map<String, String>>>(replay = 0)
    val findPasswordResult = _findPasswordResult.asSharedFlow()



    fun login(email: String, password: String) {
        viewModelScope.launch {
            val response = authRepository.login(LoginRequest(email, password))
            if (response.isSuccessful && response.body() != null) {
                val loginResponse = response.body()!!.data

                val userEntity = loginResponse?.let {
                    UserEntity(
                        userId = it.userId,
                        name = loginResponse.name,
                        email = loginResponse.email,
                        phone = loginResponse.phone,
                        userType = loginResponse.userType,
                        token = loginResponse.token,
                        location = loginResponse.location,
                        image = loginResponse.image.firstOrNull() ?: ""
                    )
                }

                if (userEntity != null) {
                    userRepository.saveUser(userEntity)
                }

                _loginResult.value = response.body()
            } else {
                _loginResult.value = ApiResponse(
                    status = "fail",
                    message = "Login failed",
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

            val response = authRepository.register(signUpRequest, imageFile)

            _registerResult.value = response
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
