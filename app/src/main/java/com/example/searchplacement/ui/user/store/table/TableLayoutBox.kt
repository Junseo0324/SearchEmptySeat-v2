package com.example.searchplacement.ui.user.store.table

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.searchplacement.data.placement.TableData
import com.example.searchplacement.ui.owner.placement.getTableDrawableRes
import com.example.searchplacement.ui.owner.placement.getTableSize
import com.example.searchplacement.ui.theme.Gray
import com.example.searchplacement.ui.theme.White

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun TableLayoutBox(tables: List<TableData>,boxHeight: Dp) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(boxHeight)
            .background(White)
            .border(2.dp, Gray, RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        val density = LocalDensity.current
        val boxWidthPx = with(density) { maxWidth.toPx() }
        val boxHeightPx = with(density) { boxHeight.toPx() }

        tables.forEach { table ->
            val (width, height) = getTableSize(table.table)
            val widthPx = with(density) { width.toPx() }
            val heightPx = with(density) { height.toPx() }

            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            table.x.value.toInt().coerceIn(0, (boxWidthPx - widthPx).toInt()),
                            table.y.value.toInt().coerceIn(0, (boxHeightPx - heightPx).toInt())
                        )
                    }
                    .size(width, height)
                    .border(2.dp, Gray)
            ) {
                Image(
                    painter = painterResource(
                        id = getTableDrawableRes(table.table, table.status.value)
                    ),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
