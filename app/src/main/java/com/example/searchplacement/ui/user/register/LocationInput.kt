package com.example.searchplacement.ui.user.register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.searchplacement.data.member.SignUpRequest
import com.example.searchplacement.di.AppModule
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Black
import com.example.searchplacement.ui.theme.CardBorderTransparentColor
import com.example.searchplacement.ui.theme.ChipBorderColor
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.IconColor
import com.example.searchplacement.ui.theme.registerColor
import com.example.searchplacement.ui.utils.AddressWebViewDialog


@Composable
fun LocationInput(signUpData: MutableState<SignUpRequest>, showWebView: MutableState<Boolean>) {
    var addressMain by remember { mutableStateOf(signUpData.value.location) }
    var addressDetail by remember { mutableStateOf("") }

    LaunchedEffect(addressMain, addressDetail) {
        if (addressMain.isNotBlank()) {
            signUpData.value = signUpData.value.copy(location = "$addressMain $addressDetail")
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row {
            Text(
                "주소",
                style = AppTextStyle.Body.copy(fontWeight = FontWeight.Bold)
            )
            Text(" *", color = Red)
        }
        Spacer(modifier = Modifier.height(Dimens.Small))

        OutlinedTextField(
            value = if (addressMain.isNotBlank()) "$addressMain\n상세주소: $addressDetail" else "",
            onValueChange = {},
            placeholder = {
                Column {
                    Text("서울시 강남구 테헤란로 123", color = IconColor, fontSize = 14.sp)
                    Text("상세주소: 1층 101호", color = IconColor, fontSize = 12.sp)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showWebView.value = true },
            enabled = false,
            shape = RoundedCornerShape(Dimens.Default),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = IconColor
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                disabledBorderColor = ChipBorderColor,
                disabledContainerColor = Color(0xFFFAFAFA),
                disabledTextColor = Black
            ),
            minLines = 2
        )

        if (addressMain.isNotBlank()) {
            Spacer(modifier = Modifier.height(Dimens.Default))
            OutlinedTextField(
                value = addressDetail,
                onValueChange = {
                    addressDetail = it
                    signUpData.value = signUpData.value.copy(location = "$addressMain $addressDetail")
                },
                placeholder = { Text("상세주소: 1층 101호", color = IconColor) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(Dimens.Default),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = registerColor,
                    unfocusedBorderColor = CardBorderTransparentColor,
                    focusedContainerColor = Color(0xFFFAFAFA),
                    unfocusedContainerColor = Color(0xFFFAFAFA),
                    cursorColor = registerColor
                )
            )
        }

        Spacer(modifier = Modifier.height(Dimens.Large))

        AddressWebViewDialog(
            showDialog = showWebView.value,
            onDismiss = { showWebView.value = false },
            onAddressSelected = { selected ->
                addressMain = selected
            },
            url = AppModule.BASE_URL
        )
    }
}