package com.example.searchplacement.presentation.user.category

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.searchplacement.presentation.user.component.SortBottomSheet

@Composable
fun CategoryScreenRoot(
    onNavigateToStoreDetail: (Long) -> Unit,
    viewModel: CategoryViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.event) {
        viewModel.event.collect { event ->
            when (event) {
                is CategoryEvent.NavigateToStoreDetail -> {
                    onNavigateToStoreDetail(event.storeId)
                }
                is CategoryEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    val sortList = remember {
        listOf(
            "기본순" to "default",
            "거리순" to "distance",
            "예약순" to "reservation",
            "찜순" to "favorite",
            "리뷰순" to "review"
        )
    }



    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        CategoryScreen(
            state = state,
            onAction = viewModel::onAction,
            modifier = Modifier.padding(paddingValues)
        )
        
        if (state.showSortBottomSheet) {
            SortBottomSheet(
                currentSort = state.sortCategory,
                sortList = sortList,
                onDismiss = { viewModel.onAction(CategoryAction.OnSortDismiss) },
                onSelectSort = { displayName, value ->
                    viewModel.onAction(CategoryAction.OnSortSelected(displayName, value))
                }
            )
        }
    }
}
