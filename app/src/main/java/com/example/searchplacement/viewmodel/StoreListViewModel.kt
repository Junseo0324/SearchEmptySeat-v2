package com.example.searchplacement.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.store.StoreRequest
import com.example.searchplacement.data.store.StoreResponse
import com.example.searchplacement.repository.OwnerStoreRepository
import com.example.searchplacement.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class StoreListViewModel @Inject constructor(
    private val ownerStoreRepository: OwnerStoreRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    var token = ""
    private val _myStores = MutableStateFlow<List<StoreResponse>>(emptyList())
    val myStores = _myStores.asStateFlow()

    private val _selectedStore = MutableStateFlow<StoreResponse?>(null)
    val selectedStore = _selectedStore.asStateFlow()

    private val _updateResult = MutableStateFlow<ApiResponse<Map<String, Any>>?>(null)
    val updateResult = _updateResult.asStateFlow()

    fun fetchMyStores() {
        viewModelScope.launch {
            token = userRepository.getUser()?.token ?: ""
            val response = ownerStoreRepository.getMyStores(token)
            Log.d("StoreListViewModel", "fetchMyStores: $response")
            if (response.isSuccessful) {
                _myStores.value = response.body()?.data ?: emptyList()
                Log.d("StoreListViewModel", "fetchMyStores: ${response.message()}")
                Log.d("StoreListViewModel", "fetchMyStores: ${response.code()}")

                _selectedStore.value = response.body()?.data?.firstOrNull()
            }
        }
    }

    fun selectStore(store: StoreResponse) {
        _selectedStore.value = store
    }

    fun updateStore(storeId: Long, request: StoreRequest, images: List<File>?) {
        viewModelScope.launch {
            val token = userRepository.getUser()?.token ?: ""
            val response = ownerStoreRepository.updateStore(token, storeId, request, images)
            _updateResult.value = response.body()
        }
    }
}