package com.example.searchplacement.ui.user.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.sp
import com.example.searchplacement.data.member.SignUpRequest
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.CardBorderTransparentColor
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.IconColor
import com.example.searchplacement.ui.theme.registerColor


@Composable
fun PasswordConfirmInput(signUpData: MutableState<SignUpRequest>) {
    val focusManager = LocalFocusManager.current
    var passwordConfirm by remember { mutableStateOf(signUpData.value.passwordConfirm) }
    val isMismatch = signUpData.value.password.isNotBlank() &&
            passwordConfirm.isNotBlank() &&
            passwordConfirm != signUpData.value.password

    Column(modifier = Modifier.fillMaxWidth()) {
        Row {
            Text(
                "비밀번호 확인",
                style = AppTextStyle.Body.copy(fontWeight = FontWeight.Bold)
            )
            Text(" *", color = Red)
        }
        Spacer(modifier = Modifier.height(Dimens.Small))
        OutlinedTextField(
            value = passwordConfirm,
            onValueChange = {
                passwordConfirm = it
                signUpData.value = signUpData.value.copy(passwordConfirm = it)
            },
            placeholder = { Text("비밀번호 재입력", color = IconColor) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(Dimens.Default),
            visualTransformation = PasswordVisualTransformation(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = IconColor
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (isMismatch) Red else registerColor,
                unfocusedBorderColor = if (isMismatch) Red else CardBorderTransparentColor,
                focusedContainerColor = Color(0xFFFAFAFA),
                unfocusedContainerColor = Color(0xFFFAFAFA),
                cursorColor = if (isMismatch) Red else registerColor
            )
        )
        if (isMismatch) {
            Text(
                text = "비밀번호가 일치하지 않습니다",
                style = AppTextStyle.Body.copy(fontSize = 12.sp, color = Red),
                modifier = Modifier.padding(start = Dimens.Tiny, top = Dimens.Tiny)
            )
        }
        Spacer(modifier = Modifier.height(Dimens.Large))
    }
}