package com.example.searchplacement.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.store.StoreRequest
import com.example.searchplacement.repository.OwnerStoreRepository
import com.example.searchplacement.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


@HiltViewModel
class OwnerStoreViewModel @Inject constructor(private val ownerStoreRepository: OwnerStoreRepository,private val userRepository: UserRepository) : ViewModel() {

    private val _registerResult = MutableStateFlow<ApiResponse<Map<String, Any>>?>(null)
    val registerResult = _registerResult.asStateFlow()

    fun registerStore(request: StoreRequest, images: List<File>?) {
        viewModelScope.launch {
            val token = userRepository.getUser()?.token ?: ""
            Log.d("OwnerStoreVM", "registerStore() called, token=$token, request=$request, images=${images?.size}")
            val response = ownerStoreRepository.registerStore(token, request, images)
            Log.d("OwnerStoreVM", "registerStore() response: success=${response.isSuccessful}, errorCode = ${response.code()} body=${response.body()}")
            _registerResult.value = response.body()
        }
    }
}
