package com.example.searchplacement.presentation.user.auth.register

import android.net.Uri
import okhttp3.MultipartBody

sealed interface RegisterAction {
    data class OnEmailChanged(val email: String) : RegisterAction
    data class OnPasswordChanged(val password: String) : RegisterAction
    data class OnPasswordConfirmChanged(val passwordConfirm: String) : RegisterAction
    data class OnNameChanged(val name: String) : RegisterAction
    data class OnPhoneChanged(val phone: String) : RegisterAction
    data class OnLocationChanged(val location: String) : RegisterAction
    data class OnUserTypeChanged(val userType: String) : RegisterAction
    data class OnImageUriChanged(val uri: Uri?) : RegisterAction
    data class OnRegisterClick(val imageFile: MultipartBody.Part?) : RegisterAction
    data object OnBackClick : RegisterAction
}
