package com.example.searchplacement.presentation.user.password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchplacement.core.util.Result
import com.example.searchplacement.domain.repository.UserRepository
import com.example.searchplacement.domain.usecase.CheckPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckPasswordViewModel @Inject constructor(
    private val checkPasswordUseCase: CheckPasswordUseCase,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CheckPasswordState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<CheckPasswordEvent>()
    val event = _event.asSharedFlow()

    fun onAction(action: CheckPasswordAction) {
        when (action) {
            is CheckPasswordAction.OnPasswordChanged -> {
                _state.update { it.copy(password = action.password) }
            }
            CheckPasswordAction.OnTogglePasswordVisibility -> {
                _state.update { it.copy(showPassword = !it.showPassword) }
            }
            CheckPasswordAction.OnConfirmClick -> {
                checkPassword()
            }
        }
    }

    private fun checkPassword() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val user = userRepository.getUser()
            val email = user?.email ?: ""
            
            when (val result = checkPasswordUseCase.execute(email, _state.value.password)) {
                is Result.Success -> {
                    _state.update { it.copy(isLoading = false) }
                    _event.emit(CheckPasswordEvent.PasswordCorrect)
                }
                is Result.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    _event.emit(CheckPasswordEvent.ShowError(result.error))
                }
            }
        }
    }
}
