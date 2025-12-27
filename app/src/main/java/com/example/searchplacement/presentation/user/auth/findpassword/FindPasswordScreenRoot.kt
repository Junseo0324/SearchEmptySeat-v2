package com.example.searchplacement.presentation.user.auth.findpassword

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
fun FindPasswordScreenRoot(
    onNavigateBack: () -> Unit,
    viewModel: FindPasswordViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.event) {
        viewModel.event.collect { event ->
            when (event) {
                is FindPasswordEvent.FindPasswordSuccess -> {
                    snackbarHostState.showSnackbar("작성한 이메일로 임시 비밀번호를 전송하였습니다.")
                    onNavigateBack()
                }
                is FindPasswordEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        FindPasswordScreen(
            state = state,
            onAction = { action ->
                when (action) {
                    FindPasswordAction.OnBackClick -> onNavigateBack()
                    else -> viewModel.onAction(action)
                }
            },
            modifier = Modifier.padding(paddingValues)
        )
    }
}
