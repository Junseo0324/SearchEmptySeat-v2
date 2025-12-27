package com.example.searchplacement.presentation.user.auth.register

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun RegisterScreenRoot(
    onNavigateToLogin: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(viewModel.event) {
        viewModel.event.collect { event ->
            when (event) {
                is RegisterEvent.RegisterSuccess -> {
                    Toast.makeText(context, "회원가입 성공", Toast.LENGTH_SHORT).show()
                    onNavigateToLogin()
                }
                is RegisterEvent.ShowSnackbar -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    RegisterScreen(
        state = state,
        onAction = { action ->
            when (action) {
                RegisterAction.OnBackClick -> onNavigateBack()
                else -> viewModel.onAction(action)
            }
        }
    )
}
