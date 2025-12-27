package com.example.searchplacement.presentation.user.component

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.searchplacement.presentation.theme.AppTextStyle
import com.example.searchplacement.presentation.theme.Dimens

@Composable
fun UserTypeInput(
    userType: String,
    onUserTypeChanged: (String) -> Unit
) {
    val effectiveUserType = userType.ifBlank { "USER" }

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
                isSelected = effectiveUserType == "USER",
                modifier = Modifier.weight(1f)
            ) {
                onUserTypeChanged("USER")
            }
            UserTypeButton(
                text = "점주",
                icon = Icons.Default.Store,
                isSelected = effectiveUserType == "OWNER",
                modifier = Modifier.weight(1f)
            ) {
                onUserTypeChanged("OWNER")
            }
        }
        Spacer(modifier = Modifier.height(Dimens.Large))
    }
}