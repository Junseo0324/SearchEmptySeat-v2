package com.example.searchplacement.ui.user.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import com.example.searchplacement.data.member.SignUpRequest
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.CardBorderTransparentColor
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.IconColor
import com.example.searchplacement.ui.theme.registerColor


@Composable
fun PhoneInput(signUpData: MutableState<SignUpRequest>) {
    val focusManager = LocalFocusManager.current
    var phone by remember { mutableStateOf(signUpData.value.phone) }
    Column(modifier = Modifier.fillMaxWidth()) {
        Row {
            Text(
                "전화번호",
                style = AppTextStyle.Body.copy(fontWeight = FontWeight.Bold)
            )
            Text(" *", color = Red)
        }
        Spacer(modifier = Modifier.height(Dimens.Small))
        OutlinedTextField(
            value = phone,
            onValueChange = {
                phone = it
                signUpData.value = signUpData.value.copy(phone = it)
            },
            placeholder = { Text("010-1234-5678", color = IconColor) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(Dimens.Default),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = null,
                    tint = IconColor
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
            }),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = registerColor,
                unfocusedBorderColor = CardBorderTransparentColor,
                focusedContainerColor = Color(0xFFFAFAFA),
                unfocusedContainerColor = Color(0xFFFAFAFA),
                cursorColor = registerColor
            )
        )
        Spacer(modifier = Modifier.height(Dimens.Large))
    }
}