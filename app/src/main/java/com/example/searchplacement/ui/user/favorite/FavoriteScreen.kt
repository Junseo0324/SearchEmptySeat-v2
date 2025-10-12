package com.example.searchplacement.ui.user.favorite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.IconTextColor
import com.example.searchplacement.ui.theme.RedPoint
import com.example.searchplacement.viewmodel.FavoriteViewModel

@Composable
fun FavoriteScreen(navController: NavHostController) {
    val favoriteViewModel: FavoriteViewModel = hiltViewModel()

    val favoriteListState by favoriteViewModel.favoriteList.collectAsState()

    LaunchedEffect(favoriteListState) {
        favoriteViewModel.getFavoriteList()
    }
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = Dimens.Medium),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.Small)
        ) {
            Icon(
                imageVector = Icons.Outlined.FavoriteBorder,
                contentDescription = null,
                tint = RedPoint,
                modifier = Modifier.size(Dimens.Large)
            )
            Text(
                text = "찜 목록",
                style = AppTextStyle.Body.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = IconTextColor
                )
            )
        }
        Column(
            Modifier
                .fillMaxSize()
                .padding(Dimens.Small)
        ) {
            LazyColumn {
                favoriteListState?.data?.let { favoriteList ->
                    items(favoriteList) { favorite ->
                        FavoriteList(favorite.store, navController, favoriteViewModel)
                    }
                }
            }
        }
    }
}