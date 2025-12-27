package com.example.searchplacement.presentation.user.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchplacement.core.util.Result
import com.example.searchplacement.data.dto.login.LoginResponse
import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.domain.repository.AuthRepository
import com.example.searchplacement.domain.repository.UserRepository
import com.example.searchplacement.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _loginResult = MutableStateFlow<ApiResponse<LoginResponse>?>(null)
    val loginResult = _loginResult.asStateFlow()

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<LoginEvent>()
    val event = _event.asSharedFlow()


    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnEmailChanged -> {
                _state.update { it.copy(email = action.email) }
            }

            is LoginAction.OnPasswordChanged -> {
                _state.update { it.copy(password = action.password) }
            }

            LoginAction.OnLoginClick -> login()

            LoginAction.OnRegisterClick,
            LoginAction.OnFindPasswordClick -> Unit
        }
    }


    private fun login() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = loginUseCase.execute(_state.value.email, _state.value.password)) {
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            loginUser = result.data,
                        )
                    }
                }

                is Result.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    _event.emit(
                        LoginEvent.ShowSnackbar(result.error)
                    )
                }
            }
        }
    }
}