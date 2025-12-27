package com.example.searchplacement.presentation.user.information

import android.net.Uri

import com.example.searchplacement.domain.model.User

sealed interface InformationAction {
    data class UpdateUserInfo(
        val user: User,
        val imageUri: Uri?
    ) : InformationAction
    data class OnNameChange(val name: String) : InformationAction
    data class OnLocationChange(val location: String) : InformationAction
    data class OnImageSelected(val uri: Uri) : InformationAction
    data object OpenAddressDialog : InformationAction
    data object CloseAddressDialog : InformationAction
}
