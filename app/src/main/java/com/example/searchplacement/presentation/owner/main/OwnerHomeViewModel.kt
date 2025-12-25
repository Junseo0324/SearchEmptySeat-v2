package com.example.searchplacement.presentation.owner.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchplacement.data.store.StoreResponse
import com.example.searchplacement.domain.repository.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OwnerHomeViewModel @Inject constructor(
    private val storeRepository: StoreRepository
): ViewModel() {
    private val _store = MutableStateFlow<StoreResponse?>(null)
    val store: StateFlow<StoreResponse?> = _store.asStateFlow()

    fun getStoreData(storeId: Long) {
        viewModelScope.launch {
            val response = storeRepository.getStoreData(storeId)
            if (response.status == "success" && response?.data != null) {
                _store.value = response?.data
            }  else {
                Log.d("OwnerHomeViewModel", "getStoreData: 실패")
            }
        }
    }
}