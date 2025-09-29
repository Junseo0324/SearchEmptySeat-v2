package com.example.searchplacement.ui.owner.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.searchplacement.viewmodel.StoreListViewModel

@Composable
fun OwnerHomeScreen(storeListViewModel: StoreListViewModel? = null){
    val stores = storeListViewModel?.selectedStore?.collectAsState()?.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text =stores?.storeName ?: "xxx",modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
            fontSize = 25.sp, fontWeight = FontWeight.Bold)

        // 오늘의 매출 현황
        SalesSummaryCard()

        Spacer(modifier = Modifier.height(20.dp))

    }
}

@Composable
fun SalesSummaryCard() {
    // 카드 형태로 한 번에 요약
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(16.dp))
            .padding(20.dp)
    ) {
        Text("오늘의 매출 현황", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        HorizontalDivider(Modifier.padding(vertical = 5.dp))
        Spacer(modifier = Modifier.height(10.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            SummaryItem(title = "총 매출", value = "₩2,500,000")
            SummaryItem(title = "예약 건수", value = "13건")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            SummaryItem(title = "평균 예약 금액", value = "₩192,308")
            SummaryItem(title = "최고 예약 금액", value = "₩450,000")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            SummaryItem(title = "평균 대기 시간", value = "12분")
            SummaryItem(title = "최대 대기 시간", value = "25분")
        }
    }
}

@Composable
fun SummaryItem(title: String, value: String) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.width(150.dp).padding(vertical = 5.dp)
    ) {
        Text(title, fontSize = 13.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 5.dp))
        Text(value, fontSize = 17.sp, fontWeight = FontWeight.Bold)
    }
}


@Preview(showBackground = true)
@Composable
fun OwnerHomeScreenPreview() {
    OwnerHomeScreen()
}