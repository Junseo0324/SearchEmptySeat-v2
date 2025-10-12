package com.example.searchplacement.ui.user.home

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.searchplacement.ui.theme.AppButtonStyle
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.IconColor
import com.example.searchplacement.ui.theme.White


@Composable
fun SearchBar(navController: NavHostController) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.Small)
            .clickable { navController.navigate("search") }
            .border(1.dp, IconColor, shape = RoundedCornerShape(Dimens.Default)),
        shape = AppButtonStyle.RoundedShape,
        color = White
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = Dimens.Medium, vertical = Dimens.Default),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "검색",
                tint = IconColor
            )
            Spacer(modifier = Modifier.width(Dimens.Large))
            Text(
                text = "매장명, 지역, 카테고리 검색...",
                color = IconColor,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

