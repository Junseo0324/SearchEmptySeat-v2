package com.example.searchplacement.ui.owner.info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp


val categoryMap = mapOf(
    "CHICKEN" to "치킨",
    "CAFE" to "카페",
    "PIZZA" to "피자",
    "FASTFOOD" to "패스트푸드",
    "CHINESEFOOD" to "중식",
    "KOREANFOOD" to "한식",
    "SNACK" to "분식",
    "JAPANESEFOOD" to "일식",
    "WESTERNFOOD" to "양식",
    "ASIANFOOD" to "아시안",
    "MEAT" to "고기"
)

@Composable
fun CategorySelectDialog(
    selected: Set<String>,
    onSelectedChanged: (Set<String>) -> Unit,
    onDismiss: () -> Unit
) {
    var tempSelected by remember { mutableStateOf(selected.toMutableSet()) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("카테고리 선택") },
        text = {
            Column {
                categoryMap.forEach { (key, label) ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = tempSelected.contains(key),
                            onCheckedChange = { checked ->
                                tempSelected =
                                    when {
                                        checked && tempSelected.size < 3 -> tempSelected + key
                                        !checked -> tempSelected - key
                                        else -> tempSelected // 3개 초과 방지
                                    }.toMutableSet()
                            }
                        )
                        Text(label)
                    }
                }
                if (tempSelected.size >= 3) {
                    Text(text = "최대 3개까지 선택 가능합니다.", color = Color.Red, fontSize = 13.sp)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onSelectedChanged(tempSelected)
                onDismiss()
            }) { Text("확인") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("취소") }
        },
        containerColor = Color.White
    )
}
