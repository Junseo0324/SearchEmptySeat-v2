package com.example.searchplacement.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.store.StoreRequest
import com.example.searchplacement.repository.OwnerStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


@HiltViewModel
class OwnerStoreViewModel @Inject constructor(
    private val ownerStoreRepository: OwnerStoreRepository
) : ViewModel() {

    private val _registerResult = MutableStateFlow<ApiResponse<Map<String, Any>>?>(null)
    val registerResult = _registerResult.asStateFlow()

    fun registerStore(request: StoreRequest, images: List<File>?) {
        viewModelScope.launch {
            val response = ownerStoreRepository.registerStore(request, images)
            _registerResult.value = response.body()
        }
    }
}
