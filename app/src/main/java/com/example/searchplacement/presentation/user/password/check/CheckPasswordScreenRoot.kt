package com.example.searchplacement.presentation.user.password.check

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CheckPasswordScreenRoot(
    onNavigateToUpdatePassword: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: CheckPasswordViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val passwordFocusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        passwordFocusRequester.requestFocus()
        keyboardController?.show()
    }

    LaunchedEffect(true) {
        viewModel.event.collectLatest { event ->
            when (event) {
                CheckPasswordEvent.PasswordCorrect -> onNavigateToUpdatePassword()
                is CheckPasswordEvent.ShowError -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    CheckPassword(
        state = state,
        passwordFocusRequester = passwordFocusRequester,
        onAction = viewModel::onAction,
        onNavigateBack = onNavigateBack
    )
}
