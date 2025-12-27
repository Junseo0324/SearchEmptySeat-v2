package com.example.searchplacement.presentation.user.setting

data class SettingState(
    val name: String? = null,
    val email: String? = null,
    val image: String? = null,
    val isLogoutDialogVisible: Boolean = false,
    val isDeleteDialogVisible: Boolean = false
)
