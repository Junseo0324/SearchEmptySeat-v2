package com.example.searchplacement.ui.owner.main

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
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.CategoryBgColor
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.IconColor
import com.example.searchplacement.ui.theme.IconTextColor
import com.example.searchplacement.ui.theme.White
import com.example.searchplacement.ui.theme.isOpenColor

@Composable
fun OwnerStoreScreen(navController: NavHostController,storeId: Long) {
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
                        imageVector = Icons.Default.RestaurantMenu,
                        contentDescription = null,
                        tint = isOpenColor,
                        modifier = Modifier.size(Dimens.Large)
                    )
                    Text(
                        text = "메뉴 관리",
                        style = AppTextStyle.Body.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = IconTextColor)
                    )
                }
                Text(
                    text = "메뉴와 카테고리를 관리하세요",
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
                    icon = Icons.Default.Close,
                    iconBgColor = Color(0xFFFFEBEE),
                    iconTint = Color(0xFFF44336),
                    title = "품절 처리",
                    description = "메뉴별 품절 상태 관리",
                    onClick = {
                        navController.navigate("soldOut")
                    }
                )
            }

            item {
                ManagementMenuCard(
                    icon = Icons.Default.RestaurantMenu,
                    iconBgColor = Color(0xFFE3F2FD),
                    iconTint = Color(0xFF2196F3),
                    title = "메뉴 관리",
                    description = "메뉴 추가/수정/삭제",
                    onClick = {
                        navController.navigate("editMenu")
                    }
                )
            }

            item {
                ManagementMenuCard(
                    icon = Icons.Default.Category,
                    iconBgColor = Color(0xFFF3E5F5),
                    iconTint = Color(0xFF9C27B0),
                    title = "색션 관리",
                    description = "메뉴 카테고리 및 색션 설정",
                    onClick = {
                        navController.navigate("editSection")
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(Dimens.Medium))
            }
        }
    }
}


