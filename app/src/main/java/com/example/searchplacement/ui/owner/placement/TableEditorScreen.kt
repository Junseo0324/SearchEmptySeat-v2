package com.example.searchplacement.ui.owner.placement

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.searchplacement.R
import com.example.searchplacement.data.placement.PlacementRequest
import com.example.searchplacement.data.placement.PlacementUpdateRequest
import com.example.searchplacement.data.placement.TableData
import com.example.searchplacement.data.placement.TableLayoutData
import com.example.searchplacement.ui.theme.ButtonMainColor
import com.example.searchplacement.viewmodel.PlacementViewModel
import com.example.searchplacement.viewmodel.StoreListViewModel

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun TableEditorScreen(
    layoutSize: Int,
    placementViewModel: PlacementViewModel,
    storeListViewModel: StoreListViewModel
) {
    val tables = remember { mutableStateListOf<TableData>() }
    var selectedTableType by remember { mutableStateOf(1) }
    var minPeople by remember { mutableStateOf("1") }
    var showTableDialog by remember { mutableStateOf(false) }
    val tableMaxPeople = mapOf(1 to 1, 2 to 2, 4 to 4, 6 to 6, 8 to 8)
    var maxPeople by remember { mutableStateOf(tableMaxPeople[selectedTableType].toString()) }
    val draggingTableId = remember { mutableStateOf<String?>(null) }
    val selectedTableId = remember { mutableStateOf<String?>(null) }

    val selectedTable = tables.find { it.id == selectedTableId.value }


    val boxHeight = when (layoutSize) { 1 -> 200.dp; 2 -> 300.dp; else -> 500.dp }

    val selectedStore = storeListViewModel.selectedStore.collectAsState().value
    val context = LocalContext.current
    val placement by placementViewModel.placement.collectAsState()
    val alreadyInitialized = remember { mutableStateOf(false) }

    LaunchedEffect(placement) {
        if (placement != null && !alreadyInitialized.value) {
            val newTables = placement!!.data?.layout?.map { (key, value) ->
                val tableData = value
                TableData(
                    id = key,
                    x = mutableStateOf(tableData.x.toFloat()),
                    y = mutableStateOf(tableData.y.toFloat()),
                    table = tableData.table,
                    min = mutableStateOf(tableData.min),
                    status = mutableStateOf(tableData.status)
                )
            }
            tables.clear()
            newTables?.let { tables.addAll(it) }
            alreadyInitialized.value = true
        }
    }

    LaunchedEffect(selectedTableId.value) {
        selectedTable?.let {
            maxPeople = tableMaxPeople[it.table].toString()
        }
    }



    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("자리 배치 수정", fontSize = 25.sp, fontWeight = FontWeight.Bold)

        BoxWithConstraints(
            Modifier
                .fillMaxWidth().padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            val density = LocalDensity.current
            val boxWidthPx = with(density) { maxWidth.toPx() }
            val boxHeightPx = with(density) { boxHeight.toPx() }

            Box(
                Modifier
                    .width(maxWidth)
                    .height(boxHeight)
                    .background(Color.White)
                    .border(2.dp, Color.Black)
            ) {
                tables.forEachIndexed { index, table ->
                    val (width, height) = getTableSize(table.table)
                    val widthPx = with(density) { width.toPx() }
                    val heightPx = with(density) { height.toPx() }
                    DraggableTable(
                        tableData = table,
                        onPositionChange = { newX, newY ->
                            val limitedX = newX.coerceIn(0f, boxWidthPx - widthPx)
                            val limitedY = newY.coerceIn(0f, boxHeightPx - heightPx)
                            table.x.value = limitedX
                            table.y.value = limitedY
                        },
                        tableResId = getTableDrawableRes(table.table,table.status.value),
                        width = width,
                        height = height,
                        isDragging = draggingTableId.value == table.id || selectedTableId.value == table.id,
                        onDragStart = {
                            draggingTableId.value = table.id
                            selectedTableId.value = table.id
                                      },
                        onDragEnd = { draggingTableId.value = null },
                        onClick = { selectedTableId.value = table.id}
                    )
                }
            }
        }

        selectedTable?.let {
            Text("선택된 테이블: ${it.id}", modifier = Modifier.padding(4.dp))
        }


        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Box(
                Modifier
                    .size(100.dp)
                    .border(2.dp, Color.Gray, RoundedCornerShape(12.dp))
                    .clickable { showTableDialog = true },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(getTableDrawableRes(selectedTableType,0)),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp)
                )
            }

            // 테이블 선택 다이얼로그
            if (showTableDialog) {
                AlertDialog(
                    onDismissRequest = { showTableDialog = false },
                    title = { Text("테이블 타입 선택") },
                    text = {
                        Column {
                            LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                items(listOf(1, 2, 4, 6, 8)) { type ->
                                    Box(
                                        modifier = Modifier
                                            .size(60.dp)
                                            .padding(4.dp)
                                            .border(
                                                width = if (selectedTableType == type) 2.dp else 0.dp,
                                                color = if (selectedTableType == type) Color(
                                                    0xffFED6D6
                                                ) else ButtonMainColor,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .clickable {
                                                selectedTableType = type
                                                maxPeople = tableMaxPeople[type].toString()
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${type}인",
                                            color = Color.Black,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                            Spacer(Modifier.height(8.dp))
                            Text("${maxPeople}인", modifier = Modifier.align(Alignment.CenterHorizontally))
                        }

                    },
                    containerColor = Color.White,
                    confirmButton = {
                        Button(
                            onClick = { showTableDialog = false },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ButtonMainColor,
                                contentColor = Color.White
                            ),
                        ) {
                            Text("확인")
                        }
                    }
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        tables.add(
                            TableData(
                                id = (tables.size + 1).toString(),
                                x = mutableStateOf(0f),
                                y = mutableStateOf(0f),
                                table = selectedTableType,
                                min = mutableStateOf(minPeople.toIntOrNull() ?: 1),
                                status = mutableStateOf(0)
                            )
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonMainColor,
                        contentColor = Color.White
                    )
                ) { Text("테이블 추가") }
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = {
                        selectedTableId.value?.let { idToRemove ->
                            tables.removeAll { it.id == idToRemove }
                            selectedTableId.value = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonMainColor,
                        contentColor = Color.White
                    )
                ) { Text("테이블 삭제")}
            }

        }

        Row(Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedTable?.min?.value?.toString() ?: "",
                onValueChange = {
                    val newMin = it.toIntOrNull()
                    if (newMin != null) {
                        selectedTable?.min?.value = newMin
                    }
                },
                label = { Text("최소 인원수") },
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
            )

            OutlinedTextField(
                value = maxPeople,
                onValueChange = {},
                readOnly = true,
                label = { Text("최대 인원수") },
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
            )
        }

        Button(
            onClick = {
                if (selectedStore == null) {
                    Toast.makeText(context, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                val layoutMap: Map<String, TableLayoutData> = tables.associate { table ->
                    table.id to TableLayoutData(
                        x = table.x.value.toInt(),
                        y = table.y.value.toInt(),
                        table = table.table,
                        min = table.min.value,
                        status = table.status.value.toInt()
                    )
                }
                Log.d("PlacementViewModel", "TableEditorScreen: layoutMap : ${layoutMap}")

                val pk = placementViewModel.placementPK.value

                if (pk != null) {
                    val updateRequest = PlacementUpdateRequest(layout = layoutMap)
                    Log.d("PlacementViewModel",  "updatePlacement 호출 전 : ${updateRequest}")
                    placementViewModel.updatePlacement(pk, updateRequest)
                } else {
                    val request = PlacementRequest(
                        storePK = selectedStore.storePK,
                        layout = layoutMap,
                        layoutSize = layoutSize
                    )
                    placementViewModel.createPlacement(request)
                }

            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ButtonMainColor,
                contentColor = Color.White
            )
        ) { Text("저장") }
    }
}

@Composable
fun DraggableTable(
    tableData: TableData,
    onPositionChange: (Float, Float) -> Unit,
    tableResId: Int,
    width: Dp,
    height: Dp,
    isDragging: Boolean,
    onDragStart: () -> Unit,
    onDragEnd: () -> Unit,
    onClick: () -> Unit
) {
    Box(
        Modifier
            .offset { IntOffset(tableData.x.value.toInt(), tableData.y.value.toInt()) }
            .pointerInput(tableData.id) {
                detectDragGestures(
                    onDragStart = { onDragStart() },
                    onDragEnd = { onDragEnd() }
                ) { _, dragAmount ->
                    val newX = tableData.x.value + dragAmount.x
                    val newY = tableData.y.value + dragAmount.y
                    onPositionChange(newX, newY)
                }
            }
            .size(width, height)
            .clickable { onClick() }
            .border(
                width = 3.dp,
                color = if (isDragging) Color.Red else Color.Black
            )
    ) {
        Image(
            painter = painterResource(id = tableResId),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}



fun getTableDrawableRes(tableType: Int, status: Int): Int = when (tableType) {
    1 -> if (status == 1) R.drawable.table_one_used else R.drawable.table_one
    2 -> if (status == 1) R.drawable.table_two_used else R.drawable.table_two
    4 -> if (status == 1) R.drawable.table_four_used else R.drawable.table_four
    6 -> if (status == 1) R.drawable.table_six_used else R.drawable.table_six
    8 -> if (status == 1) R.drawable.table_eight_used else R.drawable.table_eight
    else -> R.drawable.table_one
}


fun getTableSize(tableType: Int): Pair<Dp, Dp> = when (tableType) {
    1 -> 60.dp to 40.dp
    2 -> 90.dp to 60.dp
    4 -> 90.dp to 75.dp
    6 -> 135.dp to 105.dp
    8 -> 159.dp to 117.dp
    else -> 90.dp to 60.dp
}




