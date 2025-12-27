package com.example.searchplacement.presentation.user.password

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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.searchplacement.presentation.theme.AppButtonStyle
import com.example.searchplacement.presentation.theme.AppTextStyle
import com.example.searchplacement.presentation.theme.Black
import com.example.searchplacement.presentation.theme.ButtonMainColor
import com.example.searchplacement.presentation.theme.Dimens
import com.example.searchplacement.presentation.theme.Gray
import com.example.searchplacement.presentation.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckPassword(
    state: CheckPasswordState,
    passwordFocusRequester: FocusRequester,
    onAction: (CheckPasswordAction) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("현재 비밀번호") },
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
                .padding(Dimens.Small)
        ) {
            Text(
                text = "현재 비밀번호를 입력하세요.",
                style = AppTextStyle.Body,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            OutlinedTextField(
                value = state.password,
                onValueChange = { onAction(CheckPasswordAction.OnPasswordChanged(it)) },
                label = { Text("Password") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = if (state.showPassword) KeyboardType.Text else KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onAction(CheckPasswordAction.OnConfirmClick)
                    }
                ),
                visualTransformation = if (state.showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    Box(
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        TextButton(onClick = { onAction(CheckPasswordAction.OnTogglePasswordVisibility) }) {
                            Text(
                                if (state.showPassword) "Hide" else "Show",
                                color = Black
                            )
                        }
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Black,
                    unfocusedBorderColor = Black,
                    focusedLabelColor = Black,
                    cursorColor = Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.Small)
                    .focusRequester(passwordFocusRequester)
            )

            Button(
                onClick = { onAction(CheckPasswordAction.OnConfirmClick) },
                enabled = !state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(Dimens.Small),
                shape = AppButtonStyle.RoundedShape,
                colors = ButtonColors(
                    containerColor = ButtonMainColor, contentColor = Black,
                    disabledContainerColor = Gray, disabledContentColor = Black
                )
            ) {
                Text(if (state.isLoading) "확인 중..." else "확인", color = White)
            }
        }
    }
}