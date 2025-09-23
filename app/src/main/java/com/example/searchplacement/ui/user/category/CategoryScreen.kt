package com.example.searchplacement.ui.user.category

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.viewmodel.MainViewModel
import com.example.searchplacement.viewmodel.StoreViewModel


@Composable
fun CategoryScreen(
    navController: NavHostController,
    storeViewModel: StoreViewModel,
    mainViewModel: MainViewModel
) {
    val user by mainViewModel.user.collectAsState()

    val sortList = remember {
        listOf("default", "distance", "reservation", "favorite", "review")
    }



    val categoryList = remember {
        listOf(
            "전체" to "ALL",
            "치킨" to "CHICKEN",
            "카페" to "CAFE",
            "피자" to "PIZZA",
            "패스트푸드" to "FASTFOOD",
            "중식" to "CHINESEFOOD",
            "한식" to "KOREANFOOD",
            "분식" to "SNACK",
            "일식" to "JAPANESEFOOD",
            "양식" to "WESTERNFOOD",
            "아시안" to "ASIANFOOD",
            "고기" to "MEAT"
        )
    }

    var sortCategory by remember { mutableStateOf(sortList[0]) }
    var selectedCategory by remember { mutableStateOf(categoryList[0].first) }


    // 카테고리가 전체일 경우에는 기존 방식 (all) 아닐 경우 category 선택한 것 데이터 가져오게끔
    val storeResponse = if (selectedCategory == "전체") {
        storeViewModel.allStores.collectAsState()
    } else {
        storeViewModel.categoryStores.collectAsState()
    }


    // 데이터 기준 바뀔 때마다 데이터 변경 처리.
    LaunchedEffect(selectedCategory, sortCategory) {
        if (selectedCategory == "전체") {
            storeViewModel.getAllStores(sortCategory)
        } else {
            val categoryToEnum = categoryList.find { it.first == selectedCategory }?.second ?: "ALL"
            storeViewModel.getStoresByCategory(categoryToEnum, sortCategory)
        }
    }

    Column(
        Modifier
            .padding(Dimens.Small)
            .fillMaxSize()
    )
    {
        LazyRow {
            items(categoryList) { category ->
                CategoryList(category.first, selectedCategory == category.first) {
                    selectedCategory = category.first
                }

            }
        }
        Text(text = "정렬 기준", style = AppTextStyle.Body.copy(fontWeight = FontWeight.Bold), modifier = Modifier.padding(Dimens.Small))
        LazyRow {
            items(sortList) { item ->
                SortByStore(item, sortCategory == item) {
                    sortCategory = item
                }

            }
        }
        Spacer(modifier = Modifier.height(Dimens.Small))

        LazyColumn {
            storeResponse.value?.data?.let { storeList ->
                items(storeList) { store ->
                    StoreList(
                        storePk = store.storePK,
                        storeName = store.storeName,
                        storeAddress = store.location,
                        review = String.format("%.1f", store.averageRating ?: 0.0),
                        favoriteCount = store.favoriteCount.toString() ?: "0",
                        imageUrls = store.image,
                        navController = navController,
                    )
                }
            } ?: run {
                item {
                    Text(
                        text = storeResponse.value?.message ?: "로딩 중...",
                        modifier = Modifier.padding(16.dp),
                        style = AppTextStyle.Body
                    )
                }
            }
        }

    }
}