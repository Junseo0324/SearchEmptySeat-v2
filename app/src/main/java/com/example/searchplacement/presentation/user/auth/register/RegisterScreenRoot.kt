package com.example.searchplacement.presentation.user.auth.register

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

@Composable
fun RegisterScreenRoot(
    onNavigateToLogin: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.event) {
        viewModel.event.collect { event ->
            when (event) {
                is RegisterEvent.RegisterSuccess -> {
                    snackbarHostState.showSnackbar("회원가입 성공")
                    onNavigateToLogin()
                }
                is RegisterEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        RegisterScreen(
            state = state,
            onAction = { action ->
                when (action) {
                    RegisterAction.OnBackClick -> onNavigateBack()
                    else -> viewModel.onAction(action)
                }
            },
            modifier = Modifier.padding(paddingValues)
        )
    }
}
