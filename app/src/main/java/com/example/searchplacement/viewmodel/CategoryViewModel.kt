package com.example.searchplacement.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchplacement.data.store.StoreResponse
import com.example.searchplacement.repository.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel@Inject constructor(
    private val storeRepository: StoreRepository
) : ViewModel() {

    private val _allStores = MutableStateFlow<List<StoreResponse>?>(null)
    val allStores: StateFlow<List<StoreResponse>?> = _allStores.asStateFlow()

    private val _categoryStores = MutableStateFlow<List<StoreResponse>?>(null)
    val categoryStores: StateFlow<List<StoreResponse>?> = _categoryStores.asStateFlow()


    fun getAllStores(sortBy: String = "default") {
        viewModelScope.launch {
            try {
                val response = storeRepository.getAllStores(sortBy)
                if (response.isSuccessful && response.body()?.data != null) {
                    _allStores.value = response.body()?.data
                } else {
                    _allStores.value = emptyList()
                }
            } catch (e: Exception) {
                _allStores.value = emptyList()
            }
        }
    }


    fun getStoresByCategory(category: String, sortBy: String = "default") {
        viewModelScope.launch {
            try {
                val response = storeRepository.getStoresByCategory(category, sortBy)
                if (response.isSuccessful && response.body()?.data != null) {
                    _categoryStores.value = response.body()?.data
                } else {
                    _categoryStores.value = emptyList()
                }
            } catch (e: Exception) {
                _categoryStores.value = emptyList()
            }
        }
    }
}