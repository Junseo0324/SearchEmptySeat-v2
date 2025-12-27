package com.example.searchplacement.presentation.user.information


import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchplacement.core.util.Result
import com.example.searchplacement.domain.model.User
import com.example.searchplacement.domain.usecase.GetUserInfoUseCase
import com.example.searchplacement.domain.usecase.UpdateUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InformationViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val updateUserInfoUseCase: UpdateUserInfoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(InformationState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<InformationEvent>()
    val event = _event.asSharedFlow()

    init {
        getUserData()
    }

    fun onAction(action: InformationAction) {
        when (action) {
            is InformationAction.UpdateUserInfo -> {
                updateUserInfo(
                    action.user,
                    action.imageUri
                )
            }
            is InformationAction.OnNameChange -> {
                _state.update { it.copy(editedName = action.name) }
            }
            is InformationAction.OnLocationChange -> {
                _state.update { it.copy(editedLocation = action.location) }
            }
            is InformationAction.OnImageSelected -> {
                _state.update { it.copy(selectedImageUri = action.uri) }
            }
            InformationAction.OpenAddressDialog -> {
                _state.update { it.copy(isAddressDialogVisible = true) }
            }
            InformationAction.CloseAddressDialog -> {
                _state.update { it.copy(isAddressDialogVisible = false) }
            }
        }
    }

    private fun getUserData() {
        viewModelScope.launch {
            val user = getUserInfoUseCase.execute()
            if (user != null) {
                _state.update { 
                    it.copy(
                        user = user.toUiState(),
                        editedName = user.name,
                        editedLocation = user.location
                    ) 
                }
            }
        }
    }

    private fun updateUserInfo(
        user: User,
        imageUri: Uri?
    ) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = updateUserInfoUseCase.execute(user, imageUri)) {
                is Result.Success -> {
                    val updatedUser = result.data
                    _state.update { 
                        it.copy(
                            user = updatedUser.toUiState(),
                            isLoading = false,
                            editedName = updatedUser.name,
                            editedLocation = updatedUser.location,
                            selectedImageUri = null
                        ) 
                    }
                    _event.emit(InformationEvent.NavigateBack)
                }
                is Result.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    _event.emit(InformationEvent.ShowSnackbar(result.error))
                }
            }
        }
    }
    
    private fun User.toUiState(): InformationUiState {
        return InformationUiState(
            userId = this.userId,
            name = this.name,
            email = this.email,
            phone = this.phone,
            location = this.location,
            image = this.image,
            token = this.token
        )
    }
}
