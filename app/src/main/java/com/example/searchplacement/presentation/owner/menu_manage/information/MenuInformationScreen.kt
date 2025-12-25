package com.example.searchplacement.presentation.owner.menu_manage.information

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.searchplacement.data.menu.MenuRequest
import com.example.searchplacement.data.menu.MenuResponse
import com.example.searchplacement.data.section.MenuSectionRequest
import com.example.searchplacement.presentation.theme.Dimens
import com.example.searchplacement.presentation.theme.White
import com.example.searchplacement.presentation.utils.rememberImageLoaderWithToken
import com.example.searchplacement.presentation.owner.menu_manage.MenuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuInformationScreen(storeId: Long) {
    val menuViewModel: MenuViewModel = hiltViewModel()
    val menus by menuViewModel.menus.collectAsState()
    val sections by menuViewModel.sections.collectAsState()

    var search by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }
    var editMenu by remember { mutableStateOf<MenuResponse?>(null) }

    val imageLoader = rememberImageLoaderWithToken()

    var showAddSectionDialog by remember { mutableStateOf(false) }
    var newSectionName by remember { mutableStateOf("") }

    LaunchedEffect(storeId) {
        menuViewModel.fetch(storeId)
    }

    val sortedSections = sections.sortedBy { it.priority }

    val sectionedMenus = sortedSections.associateWith { section ->
        menus.filter { it.section?.sectionPK == section.sectionPK }
    }


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
                                .padding(Dimens.Default)
                        )
                    }

                    items(menusInSection) { menu ->
                        MenuItem(
                            menu = menu,
                            imageLoader = imageLoader,
                            onEditClick = { editMenu = menu },
                            onDeleteClick = {
                                menuViewModel.deleteMenu(menu.menuPK) {
                                    menuViewModel.fetch(storeId)
                                }
                            }
                        )
                    }
                }
            }
        }
    }

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
                        storePK = storeId,
                        name = name,
                        section = section.name,
                        priority = section.priority,
                        price = price,
                        description = desc,
                        available = true
                    ),
                    imageFile
                ) {
                    menuViewModel.fetch(storeId)
                    showAddDialog = false
                }
            }
        )
    }

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
                        storePK = storeId,
                        name = name,
                        section = section.name,
                        priority = section.priority,
                        price = price,
                        description = desc,
                        available = menu.available
                    ),
                    imageFile
                ) {
                    menuViewModel.fetch(storeId)
                    editMenu = null
                }
            }
        )
    }

    if (showAddSectionDialog) {
        AlertDialog(
            containerColor = White,
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
                            storePK = storeId,
                            name = newSectionName,
                            priority = sections.size + 1
                        )
                        menuViewModel.addSection(storeId, request) { success ->
                            if (success) {
                                menuViewModel.fetchSections(storeId)
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

