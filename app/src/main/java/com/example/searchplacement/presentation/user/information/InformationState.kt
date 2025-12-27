package com.example.searchplacement.presentation.user.information

import android.net.Uri

data class InformationState(
    val user: InformationUiState? = null,
    val isLoading: Boolean = false,
    val editedName: String = "",
    val editedLocation: String = "",
    val selectedImageUri: Uri? = null,
    val isAddressDialogVisible: Boolean = false
)

data class InformationUiState(
    val userId: String,
    val name: String,
    val email: String,
    val phone: String,
    val location: String,
    val image: String,
    val token: String
)
