package com.example.searchplacement.presentation.user.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SearchScreenRoot(
    viewModel: SearchViewModel = hiltViewModel(),
    onNavigateToStore: (Long) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SearchScreen(
        state = state,
        onAction = { action ->
            when(action) {
                is SearchAction.OnStoreClick -> onNavigateToStore(action.storeId)
                else -> viewModel.onAction(action)
            }
        }
    )
}
