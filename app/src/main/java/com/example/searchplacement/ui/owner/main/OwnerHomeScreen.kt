package com.example.searchplacement.ui.owner.main

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.rotate
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

        // 예약 현황 (그래프)
        ReservationGraphCard()
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

@Composable
fun ReservationGraphCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(16.dp))
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("요일별 예약 인원(평균)", fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.weight(1f))
            RotatingRefreshIcon {
                //서버와 통신해 새로고침하는 로직.
            }

        }
        Spacer(modifier = Modifier.height(10.dp))

        // 여기에 나중에 그래프 추가(더미 영역)
        Box(
            Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color(0xFFF8F8F8), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("여기에 그래프 표시 예정", color = Color.Gray)
        }
    }
}

@Composable
fun RotatingRefreshIcon(
    onClick: () -> Unit = {}
) {
    var isRotating by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (isRotating) 360f else 0f,
        animationSpec = tween(durationMillis = 300, easing = LinearEasing),
        label = "refresh"
    )

    Icon(
        imageVector = Icons.Filled.Refresh,
        contentDescription = "새로고침",
        modifier = Modifier
            .size(28.dp)
            .rotate(rotation)
            .clickable {
                if (!isRotating) {
                    isRotating = true
                    onClick()
                }
            }
    )

    if (isRotating && rotation == 360f) {
        LaunchedEffect(Unit) {
            isRotating = false
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OwnerHomeScreenPreview() {
    OwnerHomeScreen()
}