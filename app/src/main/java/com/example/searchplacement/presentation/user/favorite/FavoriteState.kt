package com.example.searchplacement.presentation.user.favorite

import androidx.compose.runtime.Stable
import com.example.searchplacement.domain.model.FavoriteModel

@Stable
data class FavoriteState(
    val favorites: List<FavoriteModel>? = null,
    val isLoading: Boolean = false
)
