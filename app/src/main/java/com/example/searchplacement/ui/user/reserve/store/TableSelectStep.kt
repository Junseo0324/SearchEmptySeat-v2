package com.example.searchplacement.ui.user.reserve.store

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventSeat
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.searchplacement.data.reserve.ReservationData
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.IconColor
import com.example.searchplacement.ui.theme.IconTextColor
import com.example.searchplacement.ui.theme.StoreTabBackgroundColor
import com.example.searchplacement.ui.theme.reservationCountColor

// 4단계: 좌석 선택
@Composable
fun TableSelectStep(
    reservationData: ReservationData,
    storeId: Long,
    onSelectTable: (String) -> Unit
) {
    val availableTables = remember {
        listOf(
            TableOption("중앙 4인석", "4인석 · 최대 4명", true, "table_1"),
            TableOption("대형 8인석", "8인석 · 최대 8명", true, "table_2"),
            TableOption("입구 4인석", "4인석 · 최대 4명", true, "table_3"),
            TableOption("창가 2인석", "2인석 · 최대 2명", false, "table_4")
        ).filter {
            val cap = it.capacity.filter { c -> c.isDigit() }.toIntOrNull() ?: 0
            cap >= reservationData.numberOfPeople
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(StoreTabBackgroundColor)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.Small)
        ) {
            Icon(
                imageVector = Icons.Default.EventSeat,
                contentDescription = null,
                tint = reservationCountColor,
                modifier = Modifier.size(Dimens.Large)
            )
            Text(
                text = "좌석을 선택해주세요",
                style = AppTextStyle.Body.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = IconTextColor)
            )
        }

        Text(
            text = "${reservationData.numberOfPeople}명이 앉을 수 있는 테이블을 추천합니다",
            style = AppTextStyle.Body.copy(fontSize = 14.sp,color = IconColor)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(Dimens.Default)
        ) {
            items(availableTables) { table ->
                TableOptionCard(
                    table = table,
                    isSelected = reservationData.selectedTable == table.id,
                    onClick = { onSelectTable(table.id) }
                )
            }
        }
    }
}