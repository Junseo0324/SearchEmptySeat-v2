package com.example.searchplacement.ui.user.favorite

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.viewmodel.FavoriteViewModel

@Composable
fun FavoriteScreen(navController: NavHostController, favoriteViewModel: FavoriteViewModel) {

    val favoriteListState by favoriteViewModel.favoriteList.collectAsState()

    LaunchedEffect(favoriteListState) {
        favoriteViewModel.getFavoriteList()
    }

    Column(
        Modifier
            .padding(Dimens.Small)
            .fillMaxSize()
    ) {
        Text(text = "내 찜 목록", style = AppTextStyle.Section)
        Spacer(modifier = Modifier.height(30.dp))
        LazyColumn {
            favoriteListState?.data?.let { favoriteList ->
                items(favoriteList) { favorite ->
                    FavoriteList(favorite.store,navController,favoriteViewModel)
                }
            }
        }
    }
}