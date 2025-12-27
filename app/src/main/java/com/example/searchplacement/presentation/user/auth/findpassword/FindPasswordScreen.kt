package com.example.searchplacement.presentation.user.auth.findpassword

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.searchplacement.presentation.theme.AppButtonStyle
import com.example.searchplacement.presentation.theme.AppTextStyle
import com.example.searchplacement.presentation.theme.Black
import com.example.searchplacement.presentation.theme.ButtonMainColor
import com.example.searchplacement.presentation.theme.Dimens
import com.example.searchplacement.presentation.theme.Gray
import com.example.searchplacement.presentation.theme.White

@Composable
fun FindPasswordScreen(
    state: FindPasswordState,
    onAction: (FindPasswordAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(Dimens.Medium)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "비밀번호 찾기",
            modifier = Modifier
                .padding(Dimens.Small)
                .fillMaxWidth(),
            style = AppTextStyle.Title
        )

        HorizontalDivider()

        Text(
            text = "비밀번호를 찾으시려면 계정에 연결된 이메일을 입력해주세요.",
            modifier = Modifier
                .padding(Dimens.Small)
                .fillMaxWidth(),
            style = AppTextStyle.BodySmall
        )


        Spacer(modifier = Modifier.height(50.dp))

        TextField(
            value = state.email,
            onValueChange = { onAction(FindPasswordAction.OnEmailChanged(it)) },
            placeholder = { Text(text = "등록한 이메일을 입력해주세요", style = AppTextStyle.BodySmall) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.Small),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = White,
                focusedContainerColor = White,
                focusedTextColor = Black,
                focusedIndicatorColor = Black,
                unfocusedIndicatorColor = Black
            )
        )

        Spacer(modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth())

        Button(
            onClick = { onAction(FindPasswordAction.OnFindPasswordClick) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = AppButtonStyle.RoundedShape,
            colors = ButtonColors(
                containerColor = ButtonMainColor, contentColor = Black,
                disabledContainerColor = Gray, disabledContentColor = Black
            ),
            enabled = !state.isLoading
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(color = Black)
            } else {
                Text(
                    text = "완료", style = AppTextStyle.Button.copy(color = White)
                )
            }
        }
    }
}
