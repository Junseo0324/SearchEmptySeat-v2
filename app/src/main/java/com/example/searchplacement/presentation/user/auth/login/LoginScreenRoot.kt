package com.example.searchplacement.presentation.user.auth.login

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun LoginScreenRoot(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToRegister: () -> Unit = {},
    onNavigateToFindPassword: () -> Unit= {},
    onNavigateToHome: (userType: String) -> Unit= {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is LoginEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    LaunchedEffect(state.loginUser) {
        state.loginUser?.let { user ->
            onNavigateToHome(user.userType)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        LoginScreen(
            state = state,
            onAction = { action ->
                when (action) {
                    LoginAction.OnRegisterClick ->
                        onNavigateToRegister()

                    LoginAction.OnFindPasswordClick ->
                        onNavigateToFindPassword()

                    else ->
                        viewModel.onAction(action)
                }
            }
        )
    }

}

@Preview
@Composable
private fun LoginScreenRootPreview() {
    LoginScreenRoot()
}