package com.example.searchplacement.presentation.user.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp
import com.example.searchplacement.presentation.theme.AppTextStyle
import com.example.searchplacement.presentation.theme.CardBorderTransparentColor
import com.example.searchplacement.presentation.theme.Dimens
import com.example.searchplacement.presentation.theme.registerColor

@Composable
fun RegisterPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    imeAction: ImeAction = ImeAction.Next,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier.fillMaxWidth()) {
        Row {
            Text(
                text = label,
                style = AppTextStyle.Body.copy(fontWeight = FontWeight.Bold)
            )
            Text(" *", color = Red)
        }
        Spacer(modifier = Modifier.height(Dimens.Small))
        AuthPasswordField(
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholder,
            keyboardActions = KeyboardActions(onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }),
            imeAction = imeAction,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (isError) Red else registerColor,
                unfocusedBorderColor = if (isError) Red else CardBorderTransparentColor,
                focusedContainerColor = Color(0xFFFAFAFA),
                unfocusedContainerColor = Color(0xFFFAFAFA),
                cursorColor = if (isError) Red else registerColor
            )
        )
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                style = AppTextStyle.Body.copy(fontSize = 12.sp, color = Red),
                modifier = Modifier.padding(start = Dimens.Tiny, top = Dimens.Tiny)
            )
        }
        Spacer(modifier = Modifier.height(Dimens.Large))
    }
}
