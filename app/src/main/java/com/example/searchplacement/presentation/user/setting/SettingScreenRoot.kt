package com.example.searchplacement.presentation.user.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.searchplacement.presentation.theme.AppTextStyle
import com.example.searchplacement.presentation.theme.Gray
import com.example.searchplacement.presentation.theme.White

@Composable
fun SettingScreenRoot(
    viewModel: SettingViewModel = hiltViewModel(),
    onNavigateToInformation: () -> Unit,
    onNavigateToCheckPassword: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(viewModel.event) {
        viewModel.event.collect { event ->
            when (event) {
                is SettingEvent.NavigateToLogin -> {
                    onNavigateToLogin()
                }
            }
        }
    }

    SettingScreen(
        state = state,
        onAction = viewModel::onAction,
        onNavigateToInformation = onNavigateToInformation,
        onNavigateToCheckPassword = onNavigateToCheckPassword
    )

    if (state.isLogoutDialogVisible) {
        AlertDialog(
            onDismissRequest = { viewModel.onAction(SettingAction.CloseLogoutDialog) },
            containerColor = White,
            title = {
                Text(
                    text = "로그아웃",
                    fontWeight = FontWeight.SemiBold
                )
            },
            text = {
                Text("정말 로그아웃 하시겠습니까?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.onAction(SettingAction.CloseLogoutDialog)
                        viewModel.onAction(SettingAction.Logout)
                    }
                ) {
                    Text("로그아웃", color = Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.onAction(SettingAction.CloseLogoutDialog) }
                ) {
                    Text("취소", color = Gray)
                }
            }
        )
    }

    if (state.isDeleteDialogVisible) {
        AlertDialog(
            onDismissRequest = { viewModel.onAction(SettingAction.CloseDeleteDialog) },
            containerColor = White,
            title = {
                Text(
                    text = "회원탈퇴",
                    fontWeight = FontWeight.SemiBold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("정말 회원탈퇴 하시겠습니까?")
                    Text(
                        text = "모든 데이터가 삭제되며 복구할 수 없습니다.",
                        style = AppTextStyle.BodySmall
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.onAction(SettingAction.CloseDeleteDialog)
                        viewModel.onAction(SettingAction.DeleteAccount)
                    }
                ) {
                    Text("탈퇴", color = Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.onAction(SettingAction.CloseDeleteDialog) }
                ) {
                    Text("취소", color = Gray)
                }
            }
        )
    }
}
