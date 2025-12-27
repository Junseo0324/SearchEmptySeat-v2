package com.example.searchplacement.presentation.user.setting

sealed interface SettingAction {
    data object Logout : SettingAction
    data object DeleteAccount : SettingAction
    data object OpenLogoutDialog : SettingAction
    data object CloseLogoutDialog : SettingAction
    data object OpenDeleteDialog : SettingAction
    data object CloseDeleteDialog : SettingAction
}
