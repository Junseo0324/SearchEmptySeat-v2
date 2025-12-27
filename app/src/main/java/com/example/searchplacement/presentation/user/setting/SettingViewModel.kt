package com.example.searchplacement.presentation.user.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchplacement.domain.usecase.DeleteAccountUseCase
import com.example.searchplacement.domain.usecase.GetUserInfoUseCase
import com.example.searchplacement.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SettingState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<SettingEvent>()
    val event = _event.asSharedFlow()

    init {
        getUserData()
    }

    fun onAction(action: SettingAction) {
        when (action) {
            is SettingAction.Logout -> logout()
            is SettingAction.DeleteAccount -> deleteAccount()
            SettingAction.OpenLogoutDialog -> {
                _state.update { it.copy(isLogoutDialogVisible = true) }
            }

            SettingAction.CloseLogoutDialog -> {
                _state.update { it.copy(isLogoutDialogVisible = false) }
            }

            SettingAction.OpenDeleteDialog -> {
                _state.update { it.copy(isDeleteDialogVisible = true) }
            }

            SettingAction.CloseDeleteDialog -> {
                _state.update { it.copy(isDeleteDialogVisible = false) }
            }
        }
    }

    private fun getUserData() {
        viewModelScope.launch {
            val user = getUserInfoUseCase.execute()
            if (user != null) {
                _state.update {
                    it.copy(
                        name = user.name,
                        email = user.email,
                        image = user.image
                    )
                }
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            logoutUseCase.execute()
            _state.update {
                it.copy(
                    name = null,
                    email = null,
                    image = null
                )
            }
            _event.emit(SettingEvent.NavigateToLogin)
        }
    }

    private fun deleteAccount() {
        viewModelScope.launch {
            deleteAccountUseCase.execute()
            _state.update {
                it.copy(
                    name = null,
                    email = null,
                    image = null
                )
            }
            _event.emit(SettingEvent.NavigateToLogin)
        }
    }
}
