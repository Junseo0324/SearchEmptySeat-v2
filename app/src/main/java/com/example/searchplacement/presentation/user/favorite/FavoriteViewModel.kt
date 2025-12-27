package com.example.searchplacement.presentation.user.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.searchplacement.core.util.Result

import com.example.searchplacement.domain.usecase.GetFavoriteListUseCase
import com.example.searchplacement.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getFavoriteListUseCase: GetFavoriteListUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(FavoriteState())
    val state: StateFlow<FavoriteState> = _state.asStateFlow()

    private val _event = Channel<FavoriteEvent>()
    val event = _event.receiveAsFlow()

    init {
        loadFavorites()
    }

    fun onAction(action: FavoriteAction) {
        when (action) {

            is FavoriteAction.OnStoreClick -> {
                viewModelScope.launch {
                    _event.send(FavoriteEvent.NavigateToStoreDetail(action.storeId))
                }
            }
            is FavoriteAction.OnFavoriteToggle -> toggleFavorite(action.storeId)
        }
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = getFavoriteListUseCase.execute()) {
                is Result.Success -> {
                    _state.update { it.copy(isLoading = false, favorites = result.data) }
                }

                is Result.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    _event.send(FavoriteEvent.ShowSnackbar(result.error))
                }
            }
        }
    }

    private fun toggleFavorite(storeId: Long) {
        viewModelScope.launch {
             when (val result = toggleFavoriteUseCase.execute(storeId, isFavorite = true)) {
                 is Result.Success -> {
                     loadFavorites()
                 }
                 is Result.Error -> {
                     _event.send(FavoriteEvent.ShowSnackbar("변경 실패: ${result.error}"))
                 }
             }
        }
    }
}
