package com.example.searchplacement.ui.user.login

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.searchplacement.ui.theme.AppButtonStyle
import com.example.searchplacement.ui.theme.Black
import com.example.searchplacement.ui.theme.ButtonMainColor
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.White


@Composable
fun SegmentedButton(text: String, selected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) ButtonMainColor else Color.LightGray,
            contentColor = if (selected) White else Black
        ),
        shape = AppButtonStyle.RoundedShape,
        modifier = Modifier.padding(horizontal = Dimens.Small)
    ) {
        Text(text)
    }
}