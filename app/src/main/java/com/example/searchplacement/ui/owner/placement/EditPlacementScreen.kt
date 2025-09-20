package com.example.searchplacement.ui.owner.placement

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.searchplacement.data.placement.PlacementUpdateRequest
import com.example.searchplacement.data.placement.TableData
import com.example.searchplacement.data.placement.TableLayoutData
import com.example.searchplacement.viewmodel.PlacementViewModel
import com.example.searchplacement.viewmodel.StoreListViewModel

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun EditPlacementScreen(
    navController: NavHostController,
    placementViewModel: PlacementViewModel,
    storeListViewModel: StoreListViewModel
) {
    val placement = placementViewModel.placement.collectAsState().value
    val placementPK = placementViewModel.placementPK.collectAsState().value

    val selectedStore = storeListViewModel.selectedStore.collectAsState().value
    val tables = remember { mutableStateListOf<TableData>() }
    val context = LocalContext.current
    val initialized = remember { mutableStateOf(false) }

    val layoutSize = placement?.data?.layoutSize ?: 1
    val boxHeight = when (layoutSize) {
        1 -> 200.dp; 2 -> 300.dp; else -> 500.dp
    }

    LaunchedEffect(Unit) {
        selectedStore?.let {
            placementViewModel.getPlacementByStore(it.storePK)
        }
    }

    LaunchedEffect(placement) {
        if (placement != null && !initialized.value) {
            val newTables = placement.data?.layout?.map { (id, data) ->
                TableData(
                    id = id,
                    x = mutableStateOf(data.x.toFloat()),
                    y = mutableStateOf(data.y.toFloat()),
                    table = data.table,
                    min = mutableStateOf(data.min),
                    status = mutableStateOf(data.status)
                )
            }
            tables.clear()
            newTables?.let { tables.addAll(it) }
            initialized.value = true
        }
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("자리 상태 수정", fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 10.dp))



        BoxWithConstraints(
            Modifier.fillMaxWidth().padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                Modifier
                    .width(maxWidth)
                    .height(boxHeight)
                    .background(Color.White)
                    .border(2.dp, Color.Black)
            ) {
                tables.forEach { table ->
                    val (width, height) = getTableSize(table.table)

                    Box(
                        modifier = Modifier
                            .offset { IntOffset(table.x.value.toInt(), table.y.value.toInt()) }
                            .size(width, height)
                            .clickable {
                                table.status.value = if (table.status.value == 1) 0 else 1
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(getTableDrawableRes(table.table, table.status.value)),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }

        Button(
            onClick = {
                if (placementPK == null) {
                    Toast.makeText(context, "자리 배치 정보가 없습니다.", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val layoutMap = tables.associate { table ->
                    table.id to TableLayoutData(
                        x = table.x.value.toInt(),
                        y = table.y.value.toInt(),
                        table = table.table,
                        min = table.min.value,
                        status = table.status.value.toInt()
                    )
                }

                val request = PlacementUpdateRequest(layout = layoutMap)
                placementViewModel.updatePlacement(placementPK, request)
                Toast.makeText(context, "저장 완료", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            Text("상태 저장")
        }
    }
}
