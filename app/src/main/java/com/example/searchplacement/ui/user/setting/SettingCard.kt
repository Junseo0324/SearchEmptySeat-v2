package com.example.searchplacement.ui.user.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Black
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.White

@Composable
fun SettingCard(navController: NavHostController,title: String,route: String) {
    Card(
        Modifier
            .padding(start = Dimens.Small, end = Dimens.Small, bottom = Dimens.Small)
            .fillMaxWidth(),
        colors = CardColors(
            contentColor = Black, containerColor = White,
            disabledContentColor = Black, disabledContainerColor = White
        ),
        elevation = CardDefaults.elevatedCardElevation(1.dp)
    )
    {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(Dimens.Small)
        ) {
            Text(text = title,
                style = AppTextStyle.BodyLarge,
                modifier = Modifier.clickable {
                    navController.navigate(route)
                }
                    .padding(Dimens.Small)
            )


        }
    }
}