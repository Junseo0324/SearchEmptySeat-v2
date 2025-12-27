package com.example.searchplacement.presentation.user.setting

sealed interface SettingEvent {
    data object NavigateToLogin : SettingEvent
}
