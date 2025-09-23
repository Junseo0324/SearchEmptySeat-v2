package com.example.searchplacement.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.store.StoreResponse
import com.example.searchplacement.repository.StoreRepository
import com.example.searchplacement.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val storeRepository: StoreRepository
) : ViewModel() {

    private val _allStores = MutableStateFlow<ApiResponse<List<StoreResponse>>?>(null)
    val allStores: StateFlow<ApiResponse<List<StoreResponse>>?> = _allStores.asStateFlow()


    private val _storeData = MutableStateFlow<ApiResponse<StoreResponse>?>(null)
    val storeData: StateFlow<ApiResponse<StoreResponse>?> = _storeData.asStateFlow()

    private val _categoryStores = MutableStateFlow<ApiResponse<List<StoreResponse>>?>(null)
    val categoryStores: StateFlow<ApiResponse<List<StoreResponse>>?> = _categoryStores.asStateFlow()

    private val _searchResults = MutableStateFlow<ApiResponse<List<StoreResponse>>?>(null)
    val searchResults: StateFlow<ApiResponse<List<StoreResponse>>?> = _searchResults.asStateFlow()


    private val _userId = MutableStateFlow<String?>(null)
    val userId: StateFlow<String?> = _userId.asStateFlow()

    fun getUserId() {
        viewModelScope.launch {
            _userId.value = userRepository.getUser()?.userId
        }
    }


    //모든 가게 정보
    fun getAllStores(sortBy: String = "default") {
        viewModelScope.launch {
            try {
                val response = storeRepository.getAllStores(sortBy)
                if (response.isSuccessful && response.body() != null) {
                    _allStores.value = response.body()
                } else {
                    _allStores.value = ApiResponse(
                        status = "fail",
                        message = response.errorBody()?.string() ?: "가게 목록 불러오기 실패",
                        data = emptyList()
                    )
                }
            } catch (e: Exception) {
                _allStores.value = ApiResponse(
                    status = "fail",
                    message = "네트워크 오류: ${e.message}",
                    data = emptyList()
                )
            }
        }
    }

    fun getStoreData(storeId: Long) {
        viewModelScope.launch {
            val response = storeRepository.getStoreData(storeId)
            if (response.isSuccessful && response.body() !=null) {
                _storeData.value = response.body()
            } else {
                Log.d("TAG", "getStoreData: 실패")
            }
        }
    }

    //카테고리 필터링
    fun getStoresByCategory(category: String, sortBy: String = "default") {
        viewModelScope.launch {
            try {
                val response = storeRepository.getStoresByCategory(category, sortBy)
                if (response.isSuccessful && response.body() != null) {
                    _categoryStores.value = response.body()
                } else {
                    _categoryStores.value = ApiResponse(
                        status = "fail",
                        message = response.errorBody()?.string() ?: "카테고리 가게 목록 불러오기 실패",
                        data = emptyList()
                    )
                }
            } catch (e: Exception) {
                _categoryStores.value = ApiResponse(
                    status = "fail",
                    message = "네트워크 오류: ${e.message}",
                    data = emptyList()
                )
            }
        }
    }

    //검색
    fun searchStoresByName(storeName: String) {
        viewModelScope.launch {
            try {

                // 서버에 요청
                val response = storeRepository.searchStoresByName(storeName)

                if (response.isSuccessful) {
                    // 응답이 성공적이라면 응답 본문을 사용
                    if (response.body() != null) {
                        _searchResults.value = response.body()
                    } else {
                        _searchResults.value = ApiResponse(
                            status = "fail",
                            message = "빈 데이터 응답",
                            data = emptyList()
                        )
                        Log.d("SearchScreen", "No content: Response body is empty.")
                    }
                } else {
                    // 실패한 경우, errorBody()로 응답을 확인
                    _searchResults.value = ApiResponse(
                        status = "fail",
                        message = response.errorBody()?.string() ?: "검색 실패",
                        data = emptyList()
                    )
                    Log.d("SearchScreen", "fail: ${response.code()} ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                // 예외 발생 시 처리
                _searchResults.value = ApiResponse(
                    status = "fail",
                    message = "네트워크 오류: ${e.message}",
                    data = emptyList()
                )
                Log.e("SearchScreen", "Error during API call", e)
            }
        }
    }


}