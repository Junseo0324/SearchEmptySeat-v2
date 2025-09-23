package com.example.searchplacement.ui.user.reserve.store

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.AsyncImage
import com.example.searchplacement.data.menu.MenuResponse
import com.example.searchplacement.data.placement.TableData
import com.example.searchplacement.data.reserve.ReservationDraft
import com.example.searchplacement.data.reserve.ReservationRequest
import com.example.searchplacement.data.section.MenuSectionResponse
import com.example.searchplacement.di.AppModule
import com.example.searchplacement.ui.owner.placement.getTableSize
import com.example.searchplacement.ui.theme.LoginTextColor
import com.example.searchplacement.ui.utils.rememberImageLoaderWithToken
import com.example.searchplacement.viewmodel.MenuSectionViewModel
import com.example.searchplacement.viewmodel.MenuViewModel
import com.example.searchplacement.viewmodel.PlacementViewModel
import com.example.searchplacement.viewmodel.ReservationViewModel

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun SeatMenuSelectionScreen(
    navController: NavHostController,
    placementViewModel: PlacementViewModel,
    menuViewModel: MenuViewModel,
    sectionViewModel: MenuSectionViewModel,
    reservationDraft: ReservationDraft
) {
    val placement = placementViewModel.placement.collectAsState().value
    val menus = menuViewModel.menus.collectAsState().value
    val context = LocalContext.current
    val tables = remember { mutableStateListOf<TableData>() }
    val selectedTableId = remember { mutableStateOf<String?>(null) }
    val statusMessage = remember { mutableStateOf("") }
    val initialized = remember { mutableStateOf(false) }
    val reservationViewModel: ReservationViewModel = hiltViewModel()

    val selectedMenus = remember { mutableStateMapOf<Long, Int>() }

    val layoutSize = placement?.data?.layoutSize ?: 1
    val boxHeight = when (layoutSize) {
        1 -> 200.dp; 2 -> 300.dp; else -> 500.dp
    }

    LaunchedEffect(Unit) {
        placementViewModel.getPlacementByStore(reservationDraft.storePK)
        menuViewModel.fetchMenus(reservationDraft.storePK)
        sectionViewModel.fetchSections(reservationDraft.storePK)
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
            } ?: emptyList()
            tables.clear()
            tables.addAll(newTables)
            initialized.value = true
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text("좌석 선택", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(maxWidth)
                        .height(boxHeight)
                        .background(Color.White)
                        .border(2.dp, Color.Gray)
                ) {
                    tables.forEach { table ->
                        val (width, height) = getTableSize(table.table)
                        val isSelected = selectedTableId.value == table.id
                        val tableStatus = table.status.value

                        val backgroundColor = when (tableStatus) {
                            0 -> Color.White
                            1 -> LoginTextColor
                            2 -> Color(0xFFAFD4EF)
                            else -> Color.LightGray
                        }

                        val borderColor = when {
                            isSelected -> Color.Red
                            else -> Color.Black
                        }

                        Box(
                            modifier = Modifier
                                .offset { IntOffset(table.x.value.toInt(), table.y.value.toInt()) }
                                .size(width, height)
                                .border(2.dp, borderColor)
                                .background(backgroundColor)
                                .clickable {
                                    if (tableStatus != 0) {
                                        statusMessage.value = "해당 좌석은 예약 중이거나 사용 중입니다."
                                        return@clickable
                                    }

                                    val partySize = reservationDraft.partySize
                                    val minSize = table.min.value
                                    val maxSize = table.table

                                    if (partySize < minSize) {
                                        statusMessage.value = "선택한 좌석은 최소 ${minSize}명부터 예약 가능합니다."
                                    } else if (partySize > maxSize) {
                                        statusMessage.value = "선택한 좌석은 최대 ${maxSize}명까지 수용 가능합니다."
                                    } else {
                                        selectedTableId.value = table.id
                                        statusMessage.value = ""
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${table.table}인",
                                color = if (tableStatus == 0) Color.Black else Color.White,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "현재 선택한 좌석 번호: ${selectedTableId.value ?: "-"}",
                style = MaterialTheme.typography.bodyLarge
            )

            if (statusMessage.value.isNotEmpty()) {
                Text(
                    text = statusMessage.value,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("메뉴 선택", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            MenuSelectionSection(
                menuViewModel = menuViewModel,
                sectionViewModel = sectionViewModel,
                selectedMenus = selectedMenus
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text("선택한 메뉴", style = MaterialTheme.typography.titleMedium)

            if (selectedMenus.isEmpty()) {
                Text("선택된 메뉴가 없습니다.")
            } else {
                selectedMenus.forEach { (menuPK, count) ->
                    val menu = menus.find { it.menuPK == menuPK }
                    if (menu != null) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(modifier = Modifier.weight(1f), text = menu.name)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Button(
                                    onClick = {
                                    if (count <= 1) selectedMenus.remove(menu.menuPK)
                                    else selectedMenus[menu.menuPK] = count - 1
                                    },
                                    colors = ButtonColors(
                                        Color.Transparent, Color.Black, Color.Transparent, Color.Black
                                    ),
                                    border = BorderStroke(1.dp, Color.Transparent),
                                    shape = RoundedCornerShape(10.dp),
                                ) { Text("-") }

                                Text("$count", modifier = Modifier.padding(horizontal = 8.dp))

                                Button(
                                    onClick = {
                                    selectedMenus[menu.menuPK] = count + 1
                                    },
                                    colors = ButtonColors(
                                        Color.Transparent, Color.Black, Color.Transparent, Color.Black
                                    ),
                                    border = BorderStroke(1.dp, Color.Transparent),
                                    shape = RoundedCornerShape(10.dp),
                                ) {
                                    Text("+")
                                }
                            }
                        }
                    }
                }
            }
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))

            Text("최종 확인")
            HorizontalDivider()

            Spacer(modifier = Modifier.height(8.dp))

            Text("방문 인원: ${reservationDraft.partySize}명")
            Text("예약 시간: ${reservationDraft.reservationTime.replace("T", " ")}")
            Text("선택한 좌석 번호: ${selectedTableId.value ?: "-"}")

            Spacer(modifier = Modifier.height(8.dp))

            Text("메뉴")

            var totalPrice = 0

            selectedMenus.forEach { (menuPK, count) ->
                val menu = menus.find { it.menuPK == menuPK }
                if (menu != null) {
                    val menuTotal = menu.price * count
                    totalPrice += menuTotal

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "${menu.name}", modifier = Modifier.weight(5f))
                        Text("× $count", modifier = Modifier.weight(2f))
                        Text("${menuTotal}원", modifier = Modifier.weight(3f), textAlign = TextAlign.End)
                    }
                }
            }

            HorizontalDivider()
            Text(
                text = "$totalPrice",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (selectedTableId.value == null) {
                        Toast.makeText(context, "좌석을 선택해 주세요", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val tableLayout = placement?.data?.layout?.get(selectedTableId.value)
                    val tableNumber = tableLayout?.table ?: return@Button

                    // 메뉴 변환
                    val menuMap = mutableMapOf<String, Any>()
                    selectedMenus.forEach { (menuPK, count) ->
                        val menu = menus.find { it.menuPK == menuPK }
                        if (menu != null) {
                            menuMap[menuPK.toString()] = mapOf(
                                "name" to menu.name,
                                "price" to menu.price,
                                "quantity" to count
                            )
                        }
                    }

                    val request = ReservationRequest(
                        userId = reservationDraft.userId,
                        storePK = reservationDraft.storePK,
                        reservationTime = reservationDraft.reservationTime,
                        tableNumber = tableNumber,
                        menu = menuMap,
                        partySize = reservationDraft.partySize,
                        paymentMethod = "offline",
                        status = "pending"
                    )

                    reservationViewModel.createReservation(request) { success ->
                        if (success) {
                            Toast.makeText(context, "예약이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                            navController.navigate("store/${reservationDraft.storePK}") {
                                popUpTo("seat_menu_selection") { inclusive = true }
                            }
                        } else {
                            Toast.makeText(context, "예약에 실패했습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                colors = ButtonColors(
                    Color.Transparent, Color.Black, Color.Transparent, Color.Black
                ),
                border = BorderStroke(1.dp, Color.Black),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("예약하기")
            }

        }

    }
}

@Composable
fun MenuSelectionSection(
    menuViewModel: MenuViewModel,
    sectionViewModel: MenuSectionViewModel,
    selectedMenus: MutableMap<Long, Int>,
) {
    val menus = menuViewModel.menus.collectAsState().value
    val sections = sectionViewModel.sections.collectAsState().value
    val imageLoader = rememberImageLoaderWithToken()
    SectionedMenuList(
        menus = menus,
        sections = sections,
        selectedMenus = selectedMenus,
        onAdd = { menu ->
            selectedMenus[menu.menuPK] = (selectedMenus[menu.menuPK] ?: 0) + 1
        },
        imageLoader = imageLoader
    )
}

@Composable
fun SectionedMenuList(
    menus: List<MenuResponse>,
    sections: List<MenuSectionResponse>,
    selectedMenus: MutableMap<Long, Int>,
    onAdd: (MenuResponse) -> Unit,
    imageLoader: ImageLoader
) {
    val sortedSections = sections.sortedBy { it.priority }
    val sectionedMenus = sortedSections.associateWith { section ->
        menus.filter { it.section?.sectionPK == section.sectionPK }
    }
    val noSectionMenus = menus.filter { it.section == null }

    Column(modifier = Modifier.fillMaxWidth()) {
        sectionedMenus.forEach { (section, menuList) ->
            if (menuList.isNotEmpty()) {
                Text(
                    text = section.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                menuList.forEach { menu ->
                    MenuItemRow(menu = menu,imageLoader) {
                        onAdd(menu)
                    }
                }
            }
        }

        if (noSectionMenus.isNotEmpty()) {
            Text(
                text = "미분류",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            noSectionMenus.forEach { menu ->
                MenuItemRow(menu = menu,imageLoader) {
                    onAdd(menu)
                }
            }
        }
    }
}

@Composable
fun MenuItemRow(menu: MenuResponse,imageLoader: ImageLoader, onAdd: () -> Unit) {
    val imageUrl = menu.image?.firstOrNull()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!imageUrl.isNullOrEmpty()) {
            AsyncImage(
                model = "${AppModule.BASE_URL}/api/files/$imageUrl",
                contentDescription = menu.name,
                imageLoader = imageLoader,
                modifier = Modifier
                    .size(70.dp)
                    .padding(end = 8.dp)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text("No Img", fontSize = 12.sp)
            }
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(menu.name, fontWeight = FontWeight.SemiBold)
            Text("${menu.price}원", color = MaterialTheme.colorScheme.primary)
            Text(menu.description ?: "", fontSize = 12.sp, maxLines = 1)
        }

        Button(
            onClick = onAdd,
            colors = ButtonColors(
                Color.Transparent, Color.Black, Color.Transparent, Color.Black
            ),
            border = BorderStroke(1.dp, Color.Black),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("추가")
        }
    }
}
