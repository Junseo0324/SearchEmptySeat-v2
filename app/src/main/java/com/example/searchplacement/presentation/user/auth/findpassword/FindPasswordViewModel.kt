package com.example.searchplacement.presentation.user.auth.findpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchplacement.core.util.Result
import com.example.searchplacement.domain.usecase.FindPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FindPasswordViewModel @Inject constructor(
    private val findPasswordUseCase: FindPasswordUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(FindPasswordState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<FindPasswordEvent>()
    val event = _event.asSharedFlow()

    fun onAction(action: FindPasswordAction) {
        when (action) {
            is FindPasswordAction.OnEmailChanged -> _state.update { it.copy(email = action.email) }
            is FindPasswordAction.OnFindPasswordClick -> findPassword()
            is FindPasswordAction.OnBackClick -> Unit
        }
    }

    private fun findPassword() {
        val email = _state.value.email
        if (email.isBlank()) {
            viewModelScope.launch {
                _event.emit(FindPasswordEvent.ShowSnackbar("이메일을 입력해주세요."))
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = findPasswordUseCase.execute(email)) {
                is Result.Success -> _event.emit(FindPasswordEvent.FindPasswordSuccess)
                is Result.Error -> _event.emit(FindPasswordEvent.ShowSnackbar(result.error))
            }
            _state.update { it.copy(isLoading = false) }
        }
    }
}
