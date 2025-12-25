package com.example.searchplacement.presentation.owner.menu_manage

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.searchplacement.presentation.theme.AppTextStyle
import com.example.searchplacement.presentation.theme.ButtonMainColor
import com.example.searchplacement.presentation.theme.Dimens
import com.example.searchplacement.presentation.owner.menu_manage.EditMenuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMenuScreen(navController: NavHostController,storeId: Long) {
    val viewModel: EditMenuViewModel = hiltViewModel()
    val menus by viewModel.menus.collectAsState()
    val sections by viewModel.sections.collectAsState()
    val stockState by viewModel.stockState.collectAsState()
    val updateResult by viewModel.updateResult.collectAsState()

    var search by remember { mutableStateOf("") }

    LaunchedEffect(storeId) {
        viewModel.fetch(storeId)
    }

    if (updateResult != null) {
        Toast.makeText(LocalContext.current, updateResult, Toast.LENGTH_SHORT).show()
    }
    val sortedSections = sections.sortedBy { it.priority }

    val filteredMenus = menus.filter {
        it.name.contains(search, ignoreCase = true)
    }

    val sectionedMenus = sortedSections.associateWith { section ->
        filteredMenus.filter { it.section?.sectionPK == section.sectionPK }
    }

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
                    .padding(Dimens.Default),
                onClick = {
                    viewModel.updateStock()
                    navController.popBackStack()
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
                .padding(Dimens.Default)
        ) {
            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                label = { Text("메뉴 검색") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(Dimens.Medium))

            LazyColumn {
                sectionedMenus.forEach { (section, menuList) ->
                    item {
                        Text(
                            text = section.name,
                            style = AppTextStyle.Body.copy(fontWeight = FontWeight.Bold, fontSize = 18.sp),
                            modifier = Modifier.padding(vertical = Dimens.Small)
                        )
                    }
                    items(menuList) { menu ->
                        MenuStockItem(
                            menu = menu,
                            isAvailable = stockState[menu.menuPK] ?: true,
                            onToggle = { available -> viewModel.toggleMenu(menu.menuPK, available) }
                        )
                    }
                }
            }
        }
    }
}



