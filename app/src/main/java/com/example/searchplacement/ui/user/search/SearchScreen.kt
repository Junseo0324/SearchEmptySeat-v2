package com.example.searchplacement.ui.user.search

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.ChipBorderColor
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.IconColor
import com.example.searchplacement.ui.theme.UserPrimaryColor
import com.example.searchplacement.ui.theme.ViewCountColor
import com.example.searchplacement.ui.theme.White
import com.example.searchplacement.viewmodel.SearchViewModel
import kotlinx.coroutines.delay

@Composable
fun SearchScreen(navController: NavHostController) {
    val searchViewModel: SearchViewModel = hiltViewModel()
    var searchQuery by remember { mutableStateOf("") }
    val searchResults by searchViewModel.searchResults.collectAsState()

    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotEmpty()) {
            delay(300)
            searchViewModel.searchStoresByName(searchQuery)
        }
    }

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
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
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
                        if (searchQuery.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    searchQuery = ""
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

        if (searchQuery.isEmpty()) {
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
                searchQuery = searchQuery,
                searchResults = searchResults,
                onStoreClick = { storePK ->
                    navController.navigate("store/$storePK")
                }
            )
        }
    }
}

