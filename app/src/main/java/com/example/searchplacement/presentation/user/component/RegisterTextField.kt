package com.example.searchplacement.presentation.user.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.searchplacement.presentation.theme.AppTextStyle
import com.example.searchplacement.presentation.theme.CardBorderTransparentColor
import com.example.searchplacement.presentation.theme.Dimens
import com.example.searchplacement.presentation.theme.registerColor

@Composable
fun RegisterTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    leadingIcon: ImageVector,
    imeAction: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType = KeyboardType.Text,
    onImeAction: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    
    Column(modifier = Modifier.fillMaxWidth()) {
        Row {
            Text(
                text = label,
                style = AppTextStyle.Body.copy(fontWeight = FontWeight.Bold),
            )
            Text(" *", color = Red)
        }
        Spacer(modifier = Modifier.height(Dimens.Small))
        AuthTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onNext = { 
                    if (onImeAction == {}) focusManager.moveFocus(FocusDirection.Down) else onImeAction() 
                },
                onDone = {
                    if (onImeAction == {}) focusManager.clearFocus() else onImeAction()
                }
            ),
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

