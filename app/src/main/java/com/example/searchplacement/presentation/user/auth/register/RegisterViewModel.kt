package com.example.searchplacement.presentation.user.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchplacement.core.util.Result
import com.example.searchplacement.data.member.SignUpRequest
import com.example.searchplacement.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<RegisterEvent>()
    val event = _event.asSharedFlow()

    fun onAction(action: RegisterAction) {
        when (action) {
            is RegisterAction.OnEmailChanged -> _state.update { it.copy(email = action.email) }
            is RegisterAction.OnPasswordChanged -> _state.update { it.copy(password = action.password) }
            is RegisterAction.OnPasswordConfirmChanged -> _state.update { it.copy(passwordConfirm = action.passwordConfirm) }
            is RegisterAction.OnNameChanged -> _state.update { it.copy(name = action.name) }
            is RegisterAction.OnPhoneChanged -> _state.update { it.copy(phone = action.phone) }
            is RegisterAction.OnLocationChanged -> _state.update { it.copy(location = action.location) }
            is RegisterAction.OnUserTypeChanged -> _state.update { it.copy(userType = action.userType) }
            is RegisterAction.OnImageUriChanged -> _state.update { it.copy(imageUri = action.uri) }
            is RegisterAction.OnBackClick -> Unit
            is RegisterAction.OnRegisterClick -> register(action.imageFile)
        }
    }

    private fun register(imageFile: MultipartBody.Part?) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val signUpRequest = SignUpRequest(
                email = _state.value.email,
                password = _state.value.password,
                name = _state.value.name,
                phone = _state.value.phone,
                location = _state.value.location,
                userType = _state.value.userType
            )

            when (val result = registerUseCase.execute(signUpRequest, imageFile)) {
                is Result.Success -> _event.emit(RegisterEvent.RegisterSuccess)
                is Result.Error -> _event.emit(RegisterEvent.ShowSnackbar("회원가입에 실패했습니다."))
            }

            _state.update { it.copy(isLoading = false) }
        }
    }
}
