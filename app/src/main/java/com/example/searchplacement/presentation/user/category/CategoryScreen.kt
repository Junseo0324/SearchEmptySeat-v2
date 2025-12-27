package com.example.searchplacement.presentation.user.category

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.searchplacement.presentation.theme.AppTextStyle
import com.example.searchplacement.presentation.theme.Black
import com.example.searchplacement.presentation.theme.Dimens
import com.example.searchplacement.presentation.theme.IconTextColor
import com.example.searchplacement.presentation.theme.StoreTabBackgroundColor
import com.example.searchplacement.presentation.theme.White
import com.example.searchplacement.presentation.user.component.CategoryFilterRow
import com.example.searchplacement.presentation.user.component.CategoryList

@Composable
fun CategoryScreen(
    state: CategoryState,
    onAction: (CategoryAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(StoreTabBackgroundColor)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = White,
            shadowElevation = Dimens.Nano
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = Dimens.Medium),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimens.Small)
                ) {
                    Icon(
                        imageVector = Icons.Default.GridOn,
                        contentDescription = null,
                        tint = Black,
                        modifier = Modifier.size(Dimens.Large)
                    )
                    Text(
                        text = "매장 찾기",
                        style = AppTextStyle.Body.copy(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = IconTextColor
                        )
                    )
                }
                Column(
                    modifier = Modifier.padding(Dimens.Medium)
                ) {
                    CategoryFilterRow(
                        selectedCategory = state.selectedCategory,
                        onCategorySelected = { category ->
                            onAction(CategoryAction.OnCategorySelected(category))
                        }
                    )
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
                text = "총 ${state.stores?.size ?: 0}개 매장",
                style = AppTextStyle.Body.copy(fontSize = 14.sp)
            )

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(Dimens.Small))
                    .clickable { onAction(CategoryAction.OnSortButtonClick) }
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
                    text = state.selectedSortName,
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
            if (state.isLoading) {
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
            } else {
                state.stores?.let { storeList ->
                    items(storeList) { store ->
                        CategoryList(
                            store = store,
                            onClick = {
                                onAction(CategoryAction.OnStoreClick(store.storePK))
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
                            Text("매장이 없습니다.")
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun CategoryScreenPreview() {
    CategoryScreen(
        state = CategoryState(),
        onAction = {}
    )
}
