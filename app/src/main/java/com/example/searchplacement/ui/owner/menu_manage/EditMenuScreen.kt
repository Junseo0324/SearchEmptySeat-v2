package com.example.searchplacement.ui.owner.menu_manage

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import com.example.searchplacement.data.menu.MenuResponse
import com.example.searchplacement.data.menu.MenuStockDto
import com.example.searchplacement.data.menu.OutOfStockRequest
import com.example.searchplacement.di.AppModule
import com.example.searchplacement.ui.theme.ButtonMainColor
import com.example.searchplacement.ui.theme.UserPrimaryColor
import com.example.searchplacement.ui.utils.rememberImageLoaderWithToken
import com.example.searchplacement.viewmodel.MenuSectionViewModel
import com.example.searchplacement.viewmodel.MenuViewModel
import com.example.searchplacement.viewmodel.StoreListViewModel


//메뉴 품절 처리
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMenuScreen() {
    val storeListViewModel: StoreListViewModel = hiltViewModel()
    val menuViewModel: MenuViewModel = hiltViewModel()
    val menuSectionViewModel: MenuSectionViewModel = hiltViewModel()
    val store by storeListViewModel.selectedStore.collectAsState()
    val storePk = store?.storePK ?: return

    val imageLoader = rememberImageLoaderWithToken()
    val menus by menuViewModel.menus.collectAsState()
    val sections by menuSectionViewModel.sections.collectAsState()

    var search by remember { mutableStateOf("") }

    LaunchedEffect(storePk) {
        menuSectionViewModel.fetchSections(storePk)
        menuViewModel.fetchMenus(storePk)
    }

    val stockStateMap = remember { mutableStateMapOf<Long, Boolean>() }

    LaunchedEffect(menus) {
        menuViewModel.fetchMenus(storePk)
        stockStateMap.clear()
        stockStateMap.putAll(menus.associate { it.menuPK to it.available })
    }


    val sortedSections = sections.sortedBy { it.priority }

    val filteredMenus = menus.filter {
        it.name.contains(search, ignoreCase = true)
    }

    val sectionedMenus = sortedSections.associateWith { section ->
        filteredMenus.filter { it.section?.sectionPK == section.sectionPK }
    }

    val context = LocalContext.current
    val noSectionMenus = filteredMenus.filter { it.section == null }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("메뉴 품절 처리", fontSize = 22.sp, fontWeight = FontWeight.Bold) }
            )
        },
        bottomBar = {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                onClick = {
                    val dtoList = stockStateMap.map {
                        MenuStockDto(menuPK = it.key, available = it.value)
                    }
                    val request = OutOfStockRequest(dtoList)

                    Log.d("updateMenusStock", "$request")
                    menuViewModel.updateMenusStock(request) {
                        Toast.makeText(context, "업데이트가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        menuViewModel.fetchMenus(storePk)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = ButtonMainColor)

            ) {
                Text(text = "저장")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(12.dp)
        ) {
            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                label = { Text("메뉴 검색") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                sectionedMenus.forEach { (section, menuList) ->
                    item {
                        Text(
                            text = section.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    items(menuList) { menu ->
                        MenuStockItem(menu = menu, stockStateMap = stockStateMap,imageLoader)
                    }
                }

                if (noSectionMenus.isNotEmpty()) {
                    item {
                        Text(
                            text = "미분류",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    items(noSectionMenus) { menu ->
                        MenuStockItem(menu = menu, stockStateMap = stockStateMap,imageLoader)
                    }
                }
            }
        }
    }
}

@Composable
fun MenuStockItem(menu: MenuResponse, stockStateMap: MutableMap<Long, Boolean>,imageLoader: ImageLoader) {

    val imageUrl = menu.image?.firstOrNull()
    val url = AppModule.BASE_URL + "/api/files/"+imageUrl
    val isAvailable = stockStateMap[menu.menuPK] ?: true

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {

        if (!imageUrl.isNullOrEmpty()) {
            AsyncImage(
                model = "${AppModule.BASE_URL}/api/files/$imageUrl",
                imageLoader = imageLoader,
                contentDescription = menu.name,
                modifier = Modifier
                    .size(100.dp)
//                    .clip(RoundedCornerShape(12.dp))
            )
        } else {
            Box(
                Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text("No Img", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        Column(Modifier
            .weight(1f)
            .padding(10.dp)) {
            Text(menu.name, fontWeight = FontWeight.SemiBold)
            Text("${menu.price}원", color = MaterialTheme.colorScheme.primary, fontSize = 15.sp)
            Text(menu.description ?: "", fontSize = 13.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
        }

        MultiToggleSwitch(
            labels = listOf("판매", "품절"),
            selectedIndex = if (isAvailable) 0 else 1,
            onToggle = { index ->
                stockStateMap[menu.menuPK] = (index == 0)
                Log.d("updateMenusStock", "${stockStateMap} ")
            },
            modifier = Modifier.width(100.dp)
        )

    }
}


@Composable
fun MultiToggleSwitch(
    labels: List<String>,
    selectedIndex: Int,
    onToggle: (Int) -> Unit,
    modifier: Modifier = Modifier,
    activeBgColor: Color = UserPrimaryColor,
    inactiveBgColor: Color = Color.LightGray,
    activeTextColor: Color = Color.White,
    inactiveTextColor: Color = Color.Black
) {

    val switchWidth = 50.dp
    val switchHeight = 36.dp
    val totalWidth = switchWidth * labels.size

    val animatedOffset by animateDpAsState(
        targetValue = switchWidth * selectedIndex,
        label = "ToggleSlide"
    )

    Box(
        modifier = modifier
            .width(totalWidth)
            .height(switchHeight)
            .background(inactiveBgColor)
    ) {

        Box(
            modifier = Modifier
                .offset(x = animatedOffset)
                .width(switchWidth)
                .fillMaxHeight()
                .background(activeBgColor)
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
        ) {
            labels.forEachIndexed { index, label ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable { onToggle(index) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        color = if (selectedIndex == index) activeTextColor else inactiveTextColor,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

