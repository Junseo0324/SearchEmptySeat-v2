package com.example.searchplacement.ui.user.store.table

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.searchplacement.data.placement.TableLayoutData
import com.example.searchplacement.ui.theme.AppTextStyle

@Composable
fun BoxScope.TableItemAccurate(
    tableData: TableLayoutData,
    boxWidthPx: Float,
    boxHeightPx: Float,
    density: Density
) {
    val (statusColor, _) = when (tableData.status) {
        0 -> Color(0xFF27AE60) to Color(0xFF27AE60)
        1 -> Color(0xFFE74C3C) to Color(0xFFE74C3C)
        2 -> Color(0xFF95A5A6) to Color(0xFF95A5A6)
        else -> Color(0xFF95A5A6) to Color(0xFF95A5A6)
    }

    val tableWidth = 40.dp
    val tableHeight = 40.dp
    val tableWidthPx = with(density) { tableWidth.toPx() }
    val tableHeightPx = with(density) { tableHeight.toPx() }

    Box(
        modifier = Modifier
            .offset {
                IntOffset(
                    tableData.x.toInt().coerceIn(0, (boxWidthPx - tableWidthPx).toInt()),
                    tableData.y.toInt().coerceIn(0, (boxHeightPx - tableHeightPx).toInt())
                )
            }
            .size(tableWidth, tableHeight)
            .background(statusColor.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
            .border(2.dp, statusColor, RoundedCornerShape(6.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "${tableData.table}",
            style = AppTextStyle.Body.copy(fontSize = 12.sp, fontWeight = FontWeight.Bold, color = statusColor),
        )
    }
}