package com.example.searchplacement.presentation.user.information

import androidx.compose.foundation.layout.Box
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
import com.example.searchplacement.core.di.AppModule
import com.example.searchplacement.presentation.utils.AddressWebViewDialog

@Composable
fun InformationScreenRoot(
    viewModel: InformationViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.event) {
        viewModel.event.collect { event ->
            when (event) {
                is InformationEvent.NavigateBack -> {
                    onNavigateBack()
                }
                is InformationEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            InformationScreen(
                state = state,
                onAction = viewModel::onAction,
                onNavigateBack = onNavigateBack
            )

            if (state.isAddressDialogVisible) {
                AddressWebViewDialog(
                    showDialog = true,
                    onDismiss = { viewModel.onAction(InformationAction.CloseAddressDialog) },
                    onAddressSelected = { selectedAddress ->
                        viewModel.onAction(InformationAction.OnLocationChange(selectedAddress))
                        viewModel.onAction(InformationAction.CloseAddressDialog)
                    },
                    url = AppModule.BASE_URL
                )
            }
        }
    }
}
