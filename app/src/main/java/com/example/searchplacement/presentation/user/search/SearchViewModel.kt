package com.example.searchplacement.presentation.user.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchplacement.core.util.Result
import com.example.searchplacement.domain.usecase.SearchStoresUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchStoresUseCase: SearchStoresUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state.asStateFlow()


    fun onAction(action: SearchAction) {
        when (action) {
            is SearchAction.OnQueryChanged -> {
                _state.update { it.copy(query = action.query) }
                searchStoresByName(action.query)
            }
            is SearchAction.OnSearch -> {
                searchStoresByName(_state.value.query)
            }
            is SearchAction.OnClearQuery -> {
                _state.update { it.copy(query = "", searchResults = emptyList()) }
            }
            else -> {}
        }
    }

    private fun searchStoresByName(query: String) {
        if (query.isBlank()) {
            _state.update { it.copy(searchResults = emptyList()) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            when (val result = searchStoresUseCase.execute(query)) {
                is Result.Success -> {
                    _state.update { it.copy(searchResults = result.data, isLoading = false) }
                }
                is Result.Error -> {
                    _state.update { it.copy(searchResults = emptyList(), isLoading = false) }
                }
            }
        }
    }
}
