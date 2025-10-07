package com.example.searchplacement.ui.user.store.table

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.searchplacement.data.placement.TableData
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.viewmodel.PlacementViewModel


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun TableViewScreen(
    storeId: Long,
) {
    val placementViewModel: PlacementViewModel = hiltViewModel()
    val placement = placementViewModel.placement.collectAsState().value
    val alreadyFetched = remember { mutableStateOf(false) }
    val tables = remember { mutableStateListOf<TableData>() }

    LaunchedEffect(storeId) {
        if (!alreadyFetched.value) {
            placementViewModel.getPlacementByStore(storeId)
            alreadyFetched.value = true
        }
    }

    LaunchedEffect(placement) {
        placement?.data?.layout?.let { layout ->
            val newTables = layout.map { (key, value) ->
                TableData(
                    id = key,
                    x = mutableStateOf(value.x.toFloat()),
                    y = mutableStateOf(value.y.toFloat()),
                    table = value.table,
                    min = mutableStateOf(value.min),
                    status = mutableStateOf(value.status)
                )
            }
            tables.clear()
            tables.addAll(newTables)
        }
    }

    if (placement?.data == null) {
        EmptyTableSection()
        return
    }

    val layoutSize = placement.data.layoutSize
    val boxHeight = when (layoutSize) {
        1 -> 200.dp
        2 -> 300.dp
        else -> 500.dp
    }

    val groupedSeats = tables.groupBy { it.table }
        .mapValues { it.value.size }

    Column(
        Modifier
            .fillMaxSize()
            .padding(Dimens.Medium)
    ) {
        TableStatusSection()
        Spacer(Modifier.height(Dimens.Medium))
        Text(
            text = "좌석 배치도",
            style = AppTextStyle.BodyLarge.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(Modifier.height(Dimens.Large))
        TableLayoutBox(tables,boxHeight)
        Spacer(Modifier.height(Dimens.Large))
        TableSummarySection(groupedSeats)

    }
}
