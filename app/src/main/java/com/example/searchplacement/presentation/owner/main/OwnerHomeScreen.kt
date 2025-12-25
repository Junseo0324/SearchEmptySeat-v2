package com.example.searchplacement.presentation.owner.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.searchplacement.presentation.theme.AppTextStyle
import com.example.searchplacement.presentation.theme.CategoryBgColor
import com.example.searchplacement.presentation.theme.Dimens
import com.example.searchplacement.presentation.theme.IconColor
import com.example.searchplacement.presentation.theme.IconTextColor
import com.example.searchplacement.presentation.theme.RatingColor
import com.example.searchplacement.presentation.theme.White
import com.example.searchplacement.presentation.theme.isOpenColor
import com.example.searchplacement.presentation.owner.main.OwnerHomeViewModel

@Composable
fun OwnerHomeScreen(navController: NavHostController,storeId: Long){
    val ownerHomeViewModel: OwnerHomeViewModel = hiltViewModel()
    val stores = ownerHomeViewModel.store.collectAsState().value

    LaunchedEffect(Unit) {
        ownerHomeViewModel.getStoreData(storeId)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CategoryBgColor)
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
                        imageVector = Icons.Default.Store,
                        contentDescription = null,
                        tint = isOpenColor,
                        modifier = Modifier.size(Dimens.Large)
                    )
                    Text(
                        text = stores?.storeName ?: "매장 관리",
                        style = AppTextStyle.Body.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = IconTextColor)
                    )
                }
                Text(
                    text = "매장 정보와 좌석을 관리하세요",
                    style = AppTextStyle.Body.copy(fontSize = 14.sp, color = IconColor),
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = Dimens.Small)
                )
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.Medium),
            verticalArrangement = Arrangement.spacedBy(Dimens.Medium)
        ) {
            item {
                ManagementMenuCard(
                    icon = Icons.Default.GridView,
                    iconBgColor = Color(0xFFE3F2FD),
                    iconTint = Color(0xFF2196F3),
                    title = "자리 배치",
                    description = "테이블 위치 및 배치 설정",
                    onClick = {
                        navController.navigate("store_size")
                    }
                )
            }

            item {
                ManagementMenuCard(
                    icon = Icons.Default.Edit,
                    iconBgColor = Color(0xFFE8F5E9),
                    iconTint = isOpenColor,
                    title = "자리 수정",
                    description = "테이블 상태 관리",
                    onClick = {
                        navController.navigate("place_edit")
                    }
                )
            }

            item {
                ManagementMenuCard(
                    icon = Icons.Default.Store,
                    iconBgColor = Color(0xFFF3E5F5),
                    iconTint = Color(0xFF9C27B0),
                    title = "매장 정보",
                    description = "매장명, 주소, 카테고리 등",
                    onClick = {
                        navController.navigate("storeInfo")
                    }
                )
            }

            item {
                ManagementMenuCard(
                    icon = Icons.Default.Schedule,
                    iconBgColor = Color(0xFFFFF3E0),
                    iconTint = Color(0xFFFF9800),
                    title = "영업 시간 및 휴무",
                    description = "운영 시간 및 정기/임시 휴무 설정",
                    onClick = {
                        navController.navigate("businessHour")
                    }
                )
            }

            item {
                ManagementMenuCard(
                    icon = Icons.Default.Star,
                    iconBgColor = Color(0xFFFFFDE7),
                    iconTint = RatingColor,
                    title = "매장 리뷰",
                    description = "고객 리뷰 확인 및 답변",
                    onClick = {
                        navController.navigate("storeReview")
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(Dimens.Medium))
            }
        }
    }
}