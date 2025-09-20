package com.example.searchplacement.ui.owner.menu_manage

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.searchplacement.data.menu.MenuRequest
import com.example.searchplacement.data.menu.MenuResponse
import com.example.searchplacement.data.section.MenuSectionRequest
import com.example.searchplacement.data.section.MenuSectionResponse
import com.example.searchplacement.di.AppModule
import com.example.searchplacement.ui.owner.info.uriToFile
import com.example.searchplacement.ui.utils.rememberImageLoaderWithToken
import com.example.searchplacement.viewmodel.MenuSectionViewModel
import com.example.searchplacement.viewmodel.MenuViewModel
import com.example.searchplacement.viewmodel.StoreListViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuInformationScreen(
    storeListViewModel: StoreListViewModel,
    menuViewModel: MenuViewModel,
    menuSectionViewModel: MenuSectionViewModel,
    navController: NavHostController
) {
    val store by storeListViewModel.selectedStore.collectAsState()
    val storePk = store?.storePK ?: return // 가게 선택 안 된 경우 리턴
    val menus by menuViewModel.menus.collectAsState()
    val sections by menuSectionViewModel.sections.collectAsState()



    var search by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }
    var editMenu by remember { mutableStateOf<MenuResponse?>(null) }

    var token = storeListViewModel.token
    val imageLoader = rememberImageLoaderWithToken(token)
    Log.d("MenuInformationScreen", "MenuInformationScreen: $token")


    var showAddSectionDialog by remember { mutableStateOf(false) }
    var newSectionName by remember { mutableStateOf("") }

    // 최초 진입 시 메뉴 목록 요청
    LaunchedEffect(storePk) {
        menuSectionViewModel.fetchSections(storePk)
        menuViewModel.fetchMenus(storePk)
    }

    // 섹션을 priority 순서로 정렬
    val sortedSections = sections.sortedBy { it.priority }

    // 메뉴를 섹션별로 분류
    val sectionedMenus = sortedSections.associateWith { section ->
        menus.filter { it.section?.sectionPK == section.sectionPK }
    }

    val noSectionMenus = menus.filter { it.section == null }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("메뉴 편집", fontSize = 22.sp, fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "메뉴 추가")
                    }
                }
            )
        }
    ) { padding ->
        Column(Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(10.dp)) {
            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                label = { Text("메뉴 검색") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(14.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                sectionedMenus.forEach { (section, menusInSection) ->
                    item {
                        Text(
                            text = section.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        )
                    }

                    items(menusInSection) { menu ->
                        MenuItem(
                            menu = menu,
                            token = token,
                            imageLoader = imageLoader,
                            onEditClick = { editMenu = menu },
                            onDeleteClick = {
                                menuViewModel.deleteMenu(menu.menuPK) {
                                    menuViewModel.fetchMenus(storePk)
                                }
                            }
                        )
                    }
                }
                if (noSectionMenus.isNotEmpty()) {
                    item {
                        Text(
                            text = "미분류",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        )
                    }
                    items(noSectionMenus) { menu ->
                        MenuItem(
                            menu = menu,
                            token = token,
                            imageLoader = imageLoader,
                            onEditClick = { editMenu = menu },
                            onDeleteClick = {
                                menuViewModel.deleteMenu(menu.menuPK) {
                                    menuViewModel.fetchMenus(storePk)
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    // 메뉴 추가 다이얼로그
    if (showAddDialog) {
        MenuDialog(
            title = "메뉴 추가",
            sections = sections,
            imageLoader = imageLoader,
            onAddSectionClick = { showAddSectionDialog = true },
            onDismiss = { showAddDialog = false },
            onConfirm = { name, price, section, desc, imageFile ->
                menuViewModel.addMenu(
                    MenuRequest(
                        storePK = storePk,
                        name = name,
                        section = section.name,
                        priority = section.priority,
                        price = price,
                        description = desc,
                        available = true
                    ),
                    imageFile
                ) {
                    menuViewModel.fetchMenus(storePk)
                    showAddDialog = false
                }
            }
        )
    }

    // 메뉴 수정 다이얼로그
    editMenu?.let { menu ->
        var selectedSection by remember { mutableStateOf(menu.section ?: sortedSections.firstOrNull()) }

        MenuDialog(
            title = "메뉴 수정",
            menu = menu,
            sections = sortedSections,
            imageLoader = imageLoader,
            selectedSection = selectedSection,
            onSectionSelected = { selectedSection = it },
            onAddSectionClick = {
                showAddSectionDialog = true
            },
            onDismiss = { editMenu = null },
            onConfirm = { name, price, section, desc, imageFile ->
                menuViewModel.updateMenu(
                    menu.menuPK,
                    MenuRequest(
                        storePK = storePk,
                        name = name,
                        section = section.name,
                        priority = section.priority,
                        price = price,
                        description = desc,
                        available = menu.available
                    ),
                    imageFile
                ) {
                    menuViewModel.fetchMenus(storePk)
                    editMenu = null
                }
            }
        )
    }

    if (showAddSectionDialog) {
        AlertDialog(
            onDismissRequest = { showAddSectionDialog = false },
            title = { Text("섹션 추가") },
            text = {
                OutlinedTextField(
                    value = newSectionName,
                    onValueChange = { newSectionName = it },
                    label = { Text("섹션 이름") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(onClick = {
                    if (newSectionName.isNotBlank()) {
                        val request = MenuSectionRequest(
                            storePK = storePk,
                            name = newSectionName,
                            priority = sections.size + 1
                        )
                        menuSectionViewModel.addSection(storePk, request) { success ->
                            if (success) {
                                menuSectionViewModel.fetchSections(storePk)
                            }
                        }
                    }
                    newSectionName = ""
                    showAddSectionDialog = false
                }) {
                    Text("추가")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showAddSectionDialog = false }) {
                    Text("취소")
                }
            }
        )
    }

}

@Composable
fun MenuItem(
    menu: MenuResponse,
    token: String,
    imageLoader: ImageLoader,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val imageUrl = menu.image?.firstOrNull()
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
        Column(Modifier.weight(1f)) {
            Text(menu.name, fontWeight = FontWeight.SemiBold)
            Text("${menu.price}원", color = MaterialTheme.colorScheme.primary, fontSize = 15.sp)
            Text(menu.description ?: "", fontSize = 13.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
        }
        IconButton(onClick = onEditClick) {
            Icon(Icons.Outlined.Edit, contentDescription = "수정")
        }
        IconButton(onClick = onDeleteClick) {
            Icon(Icons.Outlined.Delete, tint = MaterialTheme.colorScheme.error, contentDescription = "삭제")
        }
    }
}

@Composable
fun MenuDialog(
    title: String,
    menu: MenuResponse? = null,
    imageLoader: ImageLoader,
    sections: List<MenuSectionResponse>,
    selectedSection: MenuSectionResponse? = null,
    onSectionSelected: (MenuSectionResponse) -> Unit = {},
    onAddSectionClick: () -> Unit = {},
    onDismiss: () -> Unit,
    onConfirm: (name: String, price: Int, section: MenuSectionResponse, desc: String, imageFile: File?) -> Unit
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf(menu?.name ?: "") }
    var price by remember { mutableStateOf(menu?.price?.toString() ?: "") }
    var sectionState by remember { mutableStateOf(selectedSection ?: sections.firstOrNull()) }
    var desc by remember { mutableStateOf(menu?.description ?: "") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            imageUri = it
        }
    }


    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("메뉴 이름") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("가격") },
                    modifier = Modifier.fillMaxWidth()
                )

                // 섹션 선택 드롭다운
                SectionDropdown(
                    sections = sections,
                    selectedSection = sectionState,
                    onSectionSelected = {
                        sectionState = it
                        onSectionSelected(it)
                    },
                    onAddSectionClick = onAddSectionClick
                )

                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("설명") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))
                Text("이미지 (1개)", fontWeight = FontWeight.Bold)

                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray)
                        .clickable { launcher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        imageUri != null -> {
                            Image(
                                painter = rememberAsyncImagePainter(imageUri),
                                contentDescription = "선택된 이미지",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        menu?.image?.isNotEmpty() == true -> {
                            AsyncImage(
                                model = "${AppModule.BASE_URL}/api/files/${menu.image.first()}",
                                imageLoader = imageLoader,
                                contentDescription = "기존 이미지",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        else -> {
                            Icon(Icons.Default.Add, contentDescription = "이미지 추가", tint = Color.White, modifier = Modifier.size(40.dp))
                        }
                    }
                }

                if (imageUri != null) {
                    TextButton(
                        onClick = { imageUri = null },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("이미지 제거")
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val imageFile = imageUri?.let { uriToFile(context, it) }
                if (sectionState != null) {
                    onConfirm(
                        name,
                        price.toIntOrNull() ?: 0,
                        sectionState!!,
                        desc,
                        imageFile
                    )
                }
            }) {
                Text("확인")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}
@Composable
fun SectionDropdown(
    sections: List<MenuSectionResponse>,
    selectedSection: MenuSectionResponse?,
    onSectionSelected: (MenuSectionResponse) -> Unit,
    onAddSectionClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(selectedSection?.name ?: "섹션 선택")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            sections.forEach { section ->
                DropdownMenuItem(
                    text = { Text(section.name) },
                    onClick = {
                        onSectionSelected(section)
                        expanded = false
                    }
                )
            }
            DropdownMenuItem(
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Add, contentDescription = "섹션 추가")
                        Spacer(Modifier.width(8.dp))
                        Text("섹션 추가")
                    }
                },
                onClick = {
                    onAddSectionClick()
                    expanded = false
                }
            )
        }
    }
}