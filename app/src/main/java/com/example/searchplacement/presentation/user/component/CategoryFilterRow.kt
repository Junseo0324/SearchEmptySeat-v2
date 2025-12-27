package com.example.searchplacement.presentation.user.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.example.searchplacement.presentation.theme.Dimens

@Composable
fun CategoryFilterRow(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    val categoryList = listOf("전체", "치킨", "카페", "피자", "패스트푸드", "중식", "한식", "분식", "일식", "양식", "아시안", "고기")

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(Dimens.Small)
    ) {
        items(categoryList) { category ->
            CategoryChip(
                text = category,
                isSelected = selectedCategory == category,
                onClick = { onCategorySelected(category) }
            )
        }
    }
}