package com.example.searchplacement.ui.owner.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.searchplacement.ui.theme.Dimens

@Composable
fun OwnerStoreScreen(navController: NavHostController,storeId: Long) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(Dimens.Medium)
        .verticalScroll(rememberScrollState())
    ) {
        Text("매장 관리", fontSize = 25.sp)
        HorizontalDivider(Modifier.padding(top = 5.dp, bottom = Dimens.Medium))
        ServiceCard(
            navController,
            "매장 관리",
            listOf(
                "자리 배치" to "store_size",
                "자리 수정" to "place_edit",
                "매장 정보" to "storeInfo",
                "영업 시간 및 휴무" to "businessHour",
                "예약 내역" to "reservationHistory",
                "매장 리뷰" to "storeReview"
            )
        )
        Spacer(Modifier.height(32.dp))

        ServiceCard(
            navController = navController,
            title = "메뉴 관리",
            items = listOf(
                "품절 처리" to "soldOut",
                "메뉴 관리" to "editMenu",
                "섹션 관리" to "editSection"
            )
        )
        ServiceCard(
            navController = navController,
            title = "신규 가게 등록",
            items = listOf(
                "신규 가게 등록" to "storeRegister"
            )
        )
    }
}

@Composable
fun ServiceCard(
    navController: NavHostController,
    title: String,
    items: List<Pair<String, String>>
) {
    Card(
        Modifier
            .padding(bottom = 16.dp)
            .fillMaxWidth(),
        colors = CardColors(
            contentColor = Color.Black, containerColor = Color.White,
            disabledContentColor = Color.Black, disabledContainerColor = Color.White
        ),
        elevation = CardDefaults.elevatedCardElevation(1.dp)
    ) {
        Column(Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 25.sp)
            Spacer(Modifier.height(10.dp))
            items.forEach { (label, route) ->
                Text(
                    text = label,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            if (route == "store_size") {
                                    navController.navigate("checkPlacement")
                            } else {
                                navController.navigate(route)
                            }
                        }
                )
            }
        }
    }
}

