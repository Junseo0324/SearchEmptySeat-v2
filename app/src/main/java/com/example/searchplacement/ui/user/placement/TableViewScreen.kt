package com.example.searchplacement.ui.user.placement

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.searchplacement.data.placement.TableData
import com.example.searchplacement.ui.owner.placement.getTableDrawableRes
import com.example.searchplacement.ui.owner.placement.getTableSize
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Black
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.Gray
import com.example.searchplacement.ui.theme.White
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

    // 자리 배치 불러오기
    LaunchedEffect(storeId) {
        if (!alreadyFetched.value) {
            placementViewModel.getPlacementByStore(storeId)
            alreadyFetched.value = true
        }
    }

    // 테이블 세팅
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.XLarge),
            contentAlignment = Alignment.Center
        ) {
            Text("정보를 제공하지 않습니다.", color = Gray)
        }
        return
    }

    // 레이아웃 크기 계산
    val layoutSize = placement?.data?.layoutSize ?: 3
    val boxHeight = when (layoutSize) {
        1 -> 200.dp
        2 -> 300.dp
        else -> 500.dp
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(Dimens.Medium)
    ) {
        Text("자리 현황", style = AppTextStyle.BodyLarge)
        Spacer(Modifier.height(Dimens.XXLarge))
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(boxHeight)
                .background(White)
                .border(2.dp, Black),
            contentAlignment = Alignment.TopStart
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
                        painter = painterResource(id = getTableDrawableRes(table.table,table.status.value)),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
