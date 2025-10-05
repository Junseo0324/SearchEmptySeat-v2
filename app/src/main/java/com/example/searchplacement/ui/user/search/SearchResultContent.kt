package com.example.searchplacement.ui.user.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.searchplacement.data.store.StoreResponse
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.IconColor
import com.example.searchplacement.ui.theme.ViewCountColor

@Composable
fun SearchResultContent(
    searchQuery: String,
    searchResults: List<StoreResponse>?,
    onStoreClick: (Long) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if (searchResults != null) {
            Text(
                text = "'$searchQuery' 검색 결과 ${searchResults.size}개",
                style = AppTextStyle.Body.copy(fontSize = 14.sp),
                modifier = Modifier.padding(Dimens.Medium)
            )
        }

        if (searchResults == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (searchResults.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Dimens.XLarge),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.SearchOff,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = ViewCountColor
                )
                Spacer(modifier = Modifier.height(Dimens.Medium))
                Text(
                    text = "검색 결과가 없습니다",
                    style = AppTextStyle.Body.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(Dimens.Small))
                Text(
                    text = "다른 검색어로 시도해보세요",
                    style = AppTextStyle.Body.copy(fontSize = 14.sp, color = IconColor)
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = Dimens.Medium, vertical = Dimens.Small),
                verticalArrangement = Arrangement.spacedBy(Dimens.Default)
            ) {
                items(searchResults) { store ->
                    SearchResultCard(
                        store = store,
                        onClick = { onStoreClick(store.storePK) }
                    )
                }
            }
        }
    }
}