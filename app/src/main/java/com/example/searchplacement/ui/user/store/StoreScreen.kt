package com.example.searchplacement.ui.user.store

import MenuDisplayScreen
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Black
import com.example.searchplacement.ui.theme.Gray
import com.example.searchplacement.ui.theme.White
import com.example.searchplacement.ui.user.placement.TableViewScreen
import com.example.searchplacement.viewmodel.FavoriteViewModel
import com.example.searchplacement.viewmodel.MainViewModel
import com.example.searchplacement.viewmodel.MenuSectionViewModel
import com.example.searchplacement.viewmodel.MenuViewModel
import com.example.searchplacement.viewmodel.PlacementViewModel
import com.example.searchplacement.viewmodel.StoreViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreScreen(
    navController: NavHostController,
    storeId: Long,
    storeViewModel: StoreViewModel,
    mainViewModel: MainViewModel,
    favoriteViewModel: FavoriteViewModel,
    menuViewModel: MenuViewModel,
    menuSectionViewModel: MenuSectionViewModel,
    placementViewModel: PlacementViewModel
) {
    var list = remember {
        listOf(
            "예약 현황", "리뷰", "자리 현황", "메뉴"
        )
    }
    val user by mainViewModel.user.collectAsState()
    val token = user?.token ?: ""
    var selectedTabIndex by remember { mutableStateOf(0) }

    val storeData = storeViewModel.storeData.collectAsState()

    LaunchedEffect(storeId) {
        storeViewModel.getStoreData(storeId)
    }

    LaunchedEffect(Unit) {
        favoriteViewModel.getFavoriteList()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("${storeData.value?.data?.storeName}",style = AppTextStyle.Section)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "뒤로 가기"
                        )
                    }
                },
            )
        },
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
        ) {
            storeData.value?.data?.let { storeResponse ->
                StoreInformation(navController, storeResponse, token, favoriteViewModel)
            }

            TabRow(
                selectedTabIndex = selectedTabIndex,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = Black
                    )
                },
                containerColor = White,
                contentColor = Black
            ) {
                list.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTabIndex == index) Black else Gray,
                                fontSize = 16.sp
                            )
                        }
                    )
                }
            }
            when (selectedTabIndex) {
                0 -> ReservationScreen()
                1 -> StoreReview(storeId,token)
                2 -> TableViewScreen(storeId, placementViewModel)
                3 -> MenuDisplayScreen(
                    storePk = storeId,
                    menuViewModel = menuViewModel,
                    menuSectionViewModel = menuSectionViewModel,
                    token = token
                )
            }

        }
    }
}



