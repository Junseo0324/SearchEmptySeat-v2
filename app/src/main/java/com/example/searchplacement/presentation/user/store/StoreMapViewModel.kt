package com.example.searchplacement.presentation.user.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchplacement.core.util.Result
import com.example.searchplacement.domain.model.StoreModel
import com.example.searchplacement.domain.usecase.GetStoreDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreMapViewModel @Inject constructor(
    private val getStoreDetailUseCase: GetStoreDetailUseCase
) : ViewModel() {

    private val _store = MutableStateFlow<StoreModel?>(null)
    val store = _store.asStateFlow()

    fun fetchStore(storeId: Long) {
        viewModelScope.launch {
            when (val result = getStoreDetailUseCase.execute(storeId)) {
                is Result.Success -> {
                    _store.value = result.data
                }
                is Result.Error -> {
                    // Handle error (e.g., show snackbar) - currently just logs or ignored
                }
            }
        }
    }
}
