package com.example.searchplacement.ui.user.store.table

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.searchplacement.data.placement.PlacementResponse
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.StoreTabBackgroundColor

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun SeatLayoutWithStats(placement: PlacementResponse) {
    val layoutSize = placement.layoutSize
    val boxHeight = when (layoutSize) {
        1 -> 200.dp
        2 -> 300.dp
        else -> 500.dp
    }

    val tables = placement.layout.map { it.value }
    val tablesByCapacity = placement.layout.values.groupBy { it.table }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.Medium),
        verticalArrangement = Arrangement.spacedBy(Dimens.Medium)
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(boxHeight)
                .background(StoreTabBackgroundColor, RoundedCornerShape(Dimens.Default))
                .border(1.dp, LightGray)
                .padding(Dimens.Small)
        ) {
            val density = LocalDensity.current
            val boxWidthPx = with(density) { maxWidth.toPx() }
            val boxHeightPx = with(density) { boxHeight.toPx() }

            tables.forEach { table ->
                TableItemAccurate(
                    tableData = table,
                    boxWidthPx = boxWidthPx,
                    boxHeightPx = boxHeightPx,
                    density = density
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Dimens.Default)
        ) {
            tablesByCapacity.forEach { (capacity, tables) ->
                SeatTypeCard(
                    capacity = capacity,
                    totalSeats = tables.sumOf { it.table },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
