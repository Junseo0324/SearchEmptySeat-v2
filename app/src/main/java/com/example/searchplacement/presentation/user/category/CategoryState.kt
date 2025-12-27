package com.example.searchplacement.presentation.user.category

import androidx.compose.runtime.Stable
import com.example.searchplacement.domain.model.StoreModel

@Stable
data class CategoryState(
    val stores: List<StoreModel>? = null,
    val isLoading: Boolean = false,
    val selectedCategory: String = "전체",
    val sortCategory: String = "default",
    val selectedSortName: String = "기본순",
    val showSortBottomSheet: Boolean = false
)
