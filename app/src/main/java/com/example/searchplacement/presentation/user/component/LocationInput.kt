package com.example.searchplacement.presentation.user.component

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.searchplacement.core.di.AppModule
import com.example.searchplacement.presentation.theme.AppTextStyle
import com.example.searchplacement.presentation.theme.Black
import com.example.searchplacement.presentation.theme.CardBorderTransparentColor
import com.example.searchplacement.presentation.theme.ChipBorderColor
import com.example.searchplacement.presentation.theme.Dimens
import com.example.searchplacement.presentation.theme.IconColor
import com.example.searchplacement.presentation.theme.registerColor
import com.example.searchplacement.presentation.utils.AddressWebViewDialog

@Composable
fun LocationInput(
    location: String,
    onLocationChanged: (String) -> Unit,
    showWebView: Boolean,
    onShowWebViewChanged: (Boolean) -> Unit
) {
    var localAddressMain by remember { mutableStateOf(if (location.isNotBlank()) location else "") }
    var localAddressDetail by remember { mutableStateOf("") }
    
    fun updateLocation() {
        onLocationChanged("${localAddressMain.trim()} ${localAddressDetail.trim()}".trim())
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
            value = if (localAddressMain.isNotBlank()) "$localAddressMain\n상세주소: $localAddressDetail" else "",
            onValueChange = {},
            placeholder = {
                Column {
                    Text("서울시 강남구 테헤란로 123", color = IconColor, fontSize = 14.sp)
                    Text("상세주소: 1층 101호", color = IconColor, fontSize = 12.sp)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onShowWebViewChanged(true) },
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

        if (localAddressMain.isNotBlank()) {
            Spacer(modifier = Modifier.height(Dimens.Default))
            OutlinedTextField(
                value = localAddressDetail,
                onValueChange = {
                    localAddressDetail = it
                    updateLocation()
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
            showDialog = showWebView,
            onDismiss = { onShowWebViewChanged(false) },
            onAddressSelected = { selected ->
                localAddressMain = selected
                updateLocation()
            },
            url = AppModule.BASE_URL
        )
    }
}