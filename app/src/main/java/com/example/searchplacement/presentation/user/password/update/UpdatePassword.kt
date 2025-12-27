package com.example.searchplacement.presentation.user.password.update

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.searchplacement.presentation.theme.AppTextStyle
import com.example.searchplacement.presentation.theme.ButtonMainColor
import com.example.searchplacement.presentation.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdatePassword(
    state: UpdatePasswordState,
    passwordFocusRequester: FocusRequester,
    checkFocusRequester: FocusRequester,
    onAction: (UpdatePasswordAction) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("새 비밀번호") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "뒤로 가기"
                        )
                    }
                },
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(Dimens.Default)
        ) {
            Text(
                text = "새로운 비밀번호를 입력해주세요.",
                style = AppTextStyle.Body,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.Small)
            )

            OutlinedTextField(
                value = state.newPassword,
                onValueChange = {
                    onAction(UpdatePasswordAction.OnNewPasswordChanged(it))
                },
                label = { Text("New") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = if (state.showNewPassword) KeyboardType.Text else KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        checkFocusRequester.requestFocus()
                    }
                ),
                visualTransformation = if (state.showNewPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    Box(modifier = Modifier.padding(end = Dimens.Small)) {
                        TextButton(onClick = { onAction(UpdatePasswordAction.OnToggleNewPasswordVisibility) }) {
                            Text(if (state.showNewPassword) "Hide" else "Show", color = Color.Black)
                        }
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (state.passwordMismatch) Color.Red else Color.Black,
                    unfocusedBorderColor = if (state.passwordMismatch) Color.Red else Color.Black,
                    focusedLabelColor = Color.Black,
                    cursorColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.Small)
                    .focusRequester(passwordFocusRequester)
            )

            Text(
                text = "비밀번호를 다시 한번 입력해주세요.",
                style = AppTextStyle.Body,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.Small)
            )

            OutlinedTextField(
                value = state.confirmPassword,
                onValueChange = {
                    onAction(UpdatePasswordAction.OnConfirmPasswordChanged(it))
                },
                label = { Text("Check") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = if (state.showConfirmPassword) KeyboardType.Text else KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onAction(UpdatePasswordAction.OnUpdateClick)
                    }
                ),
                visualTransformation = if (state.showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    Box(modifier = Modifier.padding(end = Dimens.Small)) {
                        TextButton(onClick = {
                            onAction(UpdatePasswordAction.OnToggleConfirmPasswordVisibility)
                        }) {
                            Text(
                                if (state.showConfirmPassword) "Hide" else "Show",
                                color = Color.Black
                            )
                        }
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (state.passwordMismatch) Color.Red else Color.Black,
                    unfocusedBorderColor = if (state.passwordMismatch) Color.Red else Color.Black,
                    focusedLabelColor = Color.Black,
                    cursorColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.Small)
                    .focusRequester(checkFocusRequester)
            )

            if (state.passwordMismatch) {
                Text(
                    text = state.error ?: "비밀번호가 일치하지 않습니다.",
                    style = AppTextStyle.redPoint,
                    modifier = Modifier.padding(start = Dimens.Medium, bottom = Dimens.Small)
                )
            }

            Button(
                onClick = {
                    onAction(UpdatePasswordAction.OnUpdateClick)
                },
                enabled = !state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(Dimens.Small),
                shape = MaterialTheme.shapes.large,
                colors = ButtonColors(
                    containerColor = ButtonMainColor,
                    contentColor = Color.Black,
                    disabledContainerColor = Color.DarkGray,
                    disabledContentColor = Color.Black
                )
            ) {
                Text(if (state.isLoading) "변경 중..." else "변경", color = Color.White)
            }
        }
    }
}