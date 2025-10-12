package com.example.searchplacement.ui.user.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.searchplacement.data.member.SignUpRequest
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Dimens


@Composable
fun UserTypeInput(signUpData: MutableState<SignUpRequest>) {
    var userType by remember { mutableStateOf(signUpData.value.userType.ifBlank { "USER" }) }
    LaunchedEffect(userType) {
        signUpData.value = signUpData.value.copy(userType = userType)
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "사용자 유형",
            style = AppTextStyle.Body.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(Dimens.Default))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Dimens.Default)
        ) {
            UserTypeButton(
                text = "일반 사용자",
                icon = Icons.Default.Person,
                isSelected = userType == "USER",
                modifier = Modifier.weight(1f)
            ) {
                userType = "USER"
            }
            UserTypeButton(
                text = "점주",
                icon = Icons.Default.Store,
                isSelected = userType == "OWNER",
                modifier = Modifier.weight(1f)
            ) {
                userType = "OWNER"
            }
        }
        Spacer(modifier = Modifier.height(Dimens.Large))
    }
}