package com.example.searchplacement.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.store.StoreRequest
import com.example.searchplacement.data.store.StoreResponse
import com.example.searchplacement.repository.OwnerStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class StoreListViewModel @Inject constructor(
    private val ownerStoreRepository: OwnerStoreRepository
) : ViewModel() {

    private val _myStores = MutableStateFlow<List<StoreResponse>>(emptyList())
    val myStores = _myStores.asStateFlow()

    private val _selectedStore = MutableStateFlow<StoreResponse?>(null)
    val selectedStore = _selectedStore.asStateFlow()

    private val _updateResult = MutableStateFlow<ApiResponse<Map<String, Any>>?>(null)
    val updateResult = _updateResult.asStateFlow()

    fun fetchMyStores() {
        viewModelScope.launch {
            val response = ownerStoreRepository.getMyStores()
            if (response.isSuccessful) {
                _myStores.value = response.body()?.data ?: emptyList()
                Log.d("StroeSelectionScreen", "StoreSelectScreen: ${response.body()}")

                _selectedStore.value = response.body()?.data?.firstOrNull()
            }
        }
    }

    fun selectStore(store: StoreResponse) {
        _selectedStore.value = store
    }

    fun updateStore(storeId: Long, request: StoreRequest, images: List<File>?) {
        viewModelScope.launch {
            val response = ownerStoreRepository.updateStore(storeId, request, images)
            _updateResult.value = response.body()
        }
    }
}