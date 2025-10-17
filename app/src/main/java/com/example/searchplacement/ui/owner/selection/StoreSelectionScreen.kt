package com.example.searchplacement.ui.owner.selection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.searchplacement.navigation.OwnerBottomNavItem
import com.example.searchplacement.ui.theme.CardBorderTransparentColor
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.viewmodel.StoreSelectViewModel

@Composable
fun StoreSelectScreen(
    navController: NavHostController
) {
    val viewModel: StoreSelectViewModel = hiltViewModel()
    val stores = viewModel.myStores.collectAsState().value


    LaunchedEffect(Unit) {
        viewModel.fetchMyStores()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CardBorderTransparentColor)
    ) {
        HeaderSection()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.Medium),
            verticalArrangement = Arrangement.spacedBy(Dimens.Medium)
        ) {
            items(stores) { store ->
                StoreCard(
                    store = store,
                    onClick = {
                        navController.navigate(OwnerBottomNavItem.Home.screenRoute)
                    }
                )
            }

            item {
                AddStoreCard(
                    onClick = {
                        navController.navigate("storeRegister")
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(Dimens.Medium))
            }
        }
    }
}
