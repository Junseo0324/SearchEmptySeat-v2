package com.example.searchplacement.presentation.user.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchplacement.core.util.Result
import com.example.searchplacement.domain.usecase.GetStoresUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getStoresUseCase: GetStoresUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CategoryState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<CategoryEvent>()
    val event = _event.asSharedFlow()

    private val categoryMap = mapOf(
        "전체" to "ALL",
        "치킨" to "CHICKEN",
        "카페" to "CAFE",
        "피자" to "PIZZA",
        "패스트푸드" to "FASTFOOD",
        "중식" to "CHINESEFOOD",
        "한식" to "KOREANFOOD",
        "분식" to "SNACK",
        "일식" to "JAPANESEFOOD",
        "양식" to "WESTERNFOOD",
        "아시안" to "ASIANFOOD",
        "고기" to "MEAT"
    )

    init {
        fetchStores()
    }

    fun onAction(action: CategoryAction) {
        when (action) {
            is CategoryAction.OnCategorySelected -> {
                if (_state.value.selectedCategory != action.category) {
                    _state.update { it.copy(selectedCategory = action.category) }
                    fetchStores()
                }
            }
            is CategoryAction.OnSortSelected -> {
                if (_state.value.sortCategory != action.value) {
                    _state.update { 
                        it.copy(
                            selectedSortName = action.displayName,
                            sortCategory = action.value,
                            showSortBottomSheet = false
                        ) 
                    }
                    fetchStores()
                }
            }
            is CategoryAction.OnStoreClick -> {
                viewModelScope.launch {
                    _event.emit(CategoryEvent.NavigateToStoreDetail(action.storeId))
                }
            }
            is CategoryAction.OnSortButtonClick -> {
                _state.update { it.copy(showSortBottomSheet = true) }
            }
            is CategoryAction.OnSortDismiss -> {
                _state.update { it.copy(showSortBottomSheet = false) }
            }
        }
    }

    private fun fetchStores() {
        val categoryDisplay = _state.value.selectedCategory
        val categoryEnum = categoryMap[categoryDisplay] ?: "ALL"
        val sortBy = _state.value.sortCategory

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            when (val result = getStoresUseCase.execute(categoryEnum, sortBy)) {
                is Result.Success -> {
                    _state.update { it.copy(stores = result.data, isLoading = false) }
                }
                is Result.Error -> {
                    _state.update { it.copy(stores = emptyList(), isLoading = false) }
                    _event.emit(CategoryEvent.ShowSnackbar(result.error))
                }
            }
        }
    }
}