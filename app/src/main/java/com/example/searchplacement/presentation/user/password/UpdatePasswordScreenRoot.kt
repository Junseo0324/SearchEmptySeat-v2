package com.example.searchplacement.presentation.user.password

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
fun UpdatePasswordScreenRoot(
    onNavigateBack: () -> Unit,
    onSuccessUpdate: () -> Unit,
    viewModel: UpdatePasswordViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val passwordFocusRequester = remember { FocusRequester() }
    val checkFocusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        passwordFocusRequester.requestFocus()
        keyboardController?.show()
    }

    LaunchedEffect(true) {
        viewModel.event.collectLatest { event ->
            when (event) {
                UpdatePasswordEvent.PasswordUpdated -> {
                    Toast.makeText(context, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show()
                    onSuccessUpdate()
                }
                is UpdatePasswordEvent.ShowError -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    UpdatePassword(
        state = state,
        passwordFocusRequester = passwordFocusRequester,
        checkFocusRequester = checkFocusRequester,
        onAction = viewModel::onAction,
        onNavigateBack = onNavigateBack
    )
}
