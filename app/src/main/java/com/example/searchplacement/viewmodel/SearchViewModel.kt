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
class SearchViewModel @Inject constructor(
    private val storeRepository: StoreRepository
) : ViewModel() {

    private val _searchResults = MutableStateFlow<List<StoreResponse>?>(null)
    val searchResults: StateFlow<List<StoreResponse>?> = _searchResults.asStateFlow()

    fun searchStoresByName(query: String) {
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }

        viewModelScope.launch {

            try {
                val response = storeRepository.searchStoresByName(query)
                if (response.isSuccessful && response.body()?.data != null) {
                    _searchResults.value = response.body()?.data
                } else {
                    _searchResults.value = emptyList()
                }
            } catch (e: Exception) {
                _searchResults.value = emptyList()
            } finally {
            }
        }
    }
}
