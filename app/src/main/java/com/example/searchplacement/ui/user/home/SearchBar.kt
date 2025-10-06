package com.example.searchplacement.ui.user.home

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.navigation.NavHostController
import com.example.searchplacement.ui.theme.AppButtonStyle
import com.example.searchplacement.ui.theme.Black
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.Gray
import com.example.searchplacement.ui.theme.White


@Composable
fun SearchBar(navController: NavHostController) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.Small)
            .clickable { navController.navigate("search") }
            .border(1.dp, Black, shape = RoundedCornerShape(Dimens.Default)),
        shape = AppButtonStyle.RoundedShape,
        color = White
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = Dimens.Medium, vertical = Dimens.Default),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "검색",
                color = Gray,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "검색",
                tint = Black
            )
        }
    }
}

