package com.example.searchplacement.presentation.user.password.update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchplacement.core.util.Result
import com.example.searchplacement.domain.usecase.UpdatePasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdatePasswordViewModel @Inject constructor(
    private val updatePasswordUseCase: UpdatePasswordUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(UpdatePasswordState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<UpdatePasswordEvent>()
    val event = _event.asSharedFlow()

    fun onAction(action: UpdatePasswordAction) {
        when (action) {
            is UpdatePasswordAction.OnNewPasswordChanged -> {
                _state.update {
                    it.copy(
                        newPassword = action.password,
                        error = null,
                        passwordMismatch = false
                    )
                }
            }

            is UpdatePasswordAction.OnConfirmPasswordChanged -> {
                _state.update {
                    it.copy(
                        confirmPassword = action.password,
                        error = null,
                        passwordMismatch = false
                    )
                }
            }

            UpdatePasswordAction.OnToggleNewPasswordVisibility -> {
                _state.update { it.copy(showNewPassword = !it.showNewPassword) }
            }

            UpdatePasswordAction.OnToggleConfirmPasswordVisibility -> {
                _state.update { it.copy(showConfirmPassword = !it.showConfirmPassword) }
            }

            UpdatePasswordAction.OnUpdateClick -> {
                updatePassword()
            }
        }
    }

    private fun updatePassword() {
        val currentState = _state.value
        if (currentState.newPassword != currentState.confirmPassword) {
            _state.update {
                it.copy(
                    error = "비밀번호가 일치하지 않습니다.",
                    passwordMismatch = true
                )
            }
            return
        }
        if (currentState.newPassword.isBlank()) {
            _state.update {
                it.copy(
                    error = "비밀번호를 입력해주세요.",
                    passwordMismatch = true
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            when (val result = updatePasswordUseCase.execute(currentState.newPassword)) {
                is Result.Success -> {
                    _state.update { it.copy(isLoading = false) }
                    _event.emit(UpdatePasswordEvent.PasswordUpdated)
                }
                is Result.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    _event.emit(UpdatePasswordEvent.ShowError(result.error))
                }
            }
        }
    }
}
