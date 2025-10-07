package com.example.searchplacement.ui.user.category

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Black
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.White
import com.example.searchplacement.viewmodel.CategoryViewModel


@Composable
fun CategoryScreen(
    navController: NavHostController
) {
    val categoryViewModel: CategoryViewModel = hiltViewModel()
    var showSortBottomSheet by remember { mutableStateOf(false) }

    val sortList = remember {
        listOf(
            "기본순" to "default",
            "거리순" to "distance",
            "예약순" to "reservation",
            "찜순" to "favorite",
            "리뷰순" to "review"
        )
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

    var sortCategory by remember { mutableStateOf(sortList[0].second) }
    var selectedCategory by remember { mutableStateOf(categoryList[0].first) }
    var selectedSortName by remember { mutableStateOf(sortList[0].first) }


    val storeResponse = if (selectedCategory.contains("전체")) {
        categoryViewModel.allStores.collectAsState()
    } else {
        categoryViewModel.categoryStores.collectAsState()
    }



    LaunchedEffect(selectedCategory, sortCategory) {
        if (selectedCategory.contains("전체")) {
            categoryViewModel.getAllStores(sortCategory)
        } else {
            val categoryToEnum = categoryList.find { it.first == selectedCategory }?.second ?: "ALL"
            categoryViewModel.getStoresByCategory(categoryToEnum, sortCategory)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = White,
            shadowElevation = Dimens.Nano
        ) {
            Column(
                modifier = Modifier.padding(Dimens.Medium)
            ) {
                Text(
                    text = "매장 찾기",
                    style = AppTextStyle.BodyLarge
                )

                Spacer(modifier = Modifier.height(Dimens.Default))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(Dimens.Small)
                ) {
                    items(categoryList) { (display, _) ->
                        CategoryChip(
                            text = display,
                            isSelected = selectedCategory == display,
                            onClick = { selectedCategory = display }
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.Medium, vertical = Dimens.Default),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "총 ${storeResponse.value?.size ?: 0}개 매장",
                style = AppTextStyle.Body.copy(fontSize = 14.sp)
            )

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(Dimens.Small))
                    .clickable { showSortBottomSheet = true }
                    .padding(horizontal = Dimens.Default, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(Dimens.Tiny),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Sort,
                    contentDescription = null,
                    tint = Black,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = selectedSortName,
                    style = AppTextStyle.Body.copy(fontSize = 14.sp)
                )
                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = Black,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(horizontal = Dimens.Medium, vertical = Dimens.Small),
            verticalArrangement = Arrangement.spacedBy(Dimens.Medium)
        ) {
            storeResponse.value?.let { storeList ->
                items(storeList) { store ->
                    CategoryList(
                        store = store,
                        onClick = {
                            navController.navigate("store/${store.storePK}")
                        }
                    )
                }
            } ?: run {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Dimens.XLarge),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }

    if (showSortBottomSheet) {
        SortBottomSheet(
            currentSort = sortCategory,
            sortList = sortList,
            onDismiss = { showSortBottomSheet = false },
            onSelectSort = { displayName, value ->
                sortCategory = value
                selectedSortName = displayName
                showSortBottomSheet = false
            }
        )
    }
}

