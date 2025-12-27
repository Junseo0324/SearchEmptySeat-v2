package com.example.searchplacement.presentation.user.information

sealed interface InformationEvent {
    data object NavigateBack : InformationEvent
    data class ShowSnackbar(val message: String) : InformationEvent
}
