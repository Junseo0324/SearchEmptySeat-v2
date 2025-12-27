package com.example.searchplacement.presentation.user.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.searchplacement.presentation.theme.AppTextStyle
import com.example.searchplacement.presentation.theme.ChipBorderColor
import com.example.searchplacement.presentation.theme.Dimens
import com.example.searchplacement.presentation.theme.IconColor
import com.example.searchplacement.presentation.theme.UserPrimaryColor
import com.example.searchplacement.presentation.theme.ViewCountColor
import com.example.searchplacement.presentation.theme.White

@Composable
fun SearchScreen(
    state: SearchState,
    onAction: (SearchAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
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
                    text = "매장 검색",
                    style = AppTextStyle.BodyLarge.copy(fontSize = 24.sp, fontWeight = FontWeight.Bold,)
                )

                Spacer(modifier = Modifier.height(Dimens.Default))

                OutlinedTextField(
                    value = state.query,
                    onValueChange = { onAction(SearchAction.OnQueryChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            "매장명, 지역, 음식 종류를 검색하세요",
                            style = AppTextStyle.Body.copy(fontSize = 14.sp, color = Color(0xFFBDC3C7))
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = IconColor
                        )
                    },
                    trailingIcon = {
                        if (state.query.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    onAction(SearchAction.OnClearQuery)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "지우기",
                                    tint = IconColor
                                )
                            }
                        }
                    },
                    shape = RoundedCornerShape(Dimens.Default),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = ChipBorderColor,
                        focusedBorderColor = UserPrimaryColor
                    ),
                    singleLine = true
                )
            }
        }

        if (state.query.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens.Default)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = ViewCountColor
                )
                Text(
                    text = "검색어를 입력해주세요",
                    style = AppTextStyle.BodyLarge.copy(fontSize = 18.sp, color = Color(0xFF2C3E50))
                )
                Text(
                    text = "매장명, 지역, 음식 종류로 검색할 수 있습니다",
                    style = AppTextStyle.Body.copy(
                    fontSize = 14.sp,
                    color = IconColor)
                )
            }
        } else {
            SearchResultContent(
                searchQuery = state.query,
                searchResults = state.searchResults,
                onStoreClick = { storePK ->
                    onAction(SearchAction.OnStoreClick(storePK))
                }
            )
        }
    }
}

