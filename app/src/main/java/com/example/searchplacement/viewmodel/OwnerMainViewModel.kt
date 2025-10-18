package com.example.searchplacement.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class OwnerMainViewModel @Inject constructor() : ViewModel() {

    private val _selectedStoreId = MutableStateFlow<Long?>(null)
    val selectedStoreId: StateFlow<Long?> = _selectedStoreId.asStateFlow()

    fun selectStore(storeId: Long) {
        _selectedStoreId.value = storeId
    }

    fun clearSelectedStore() {
        _selectedStoreId.value = null
    }
}