package com.example.searchplacement.ui.owner.section

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.searchplacement.data.section.MenuSectionBulkUpdateRequest
import com.example.searchplacement.data.section.MenuSectionRequest
import com.example.searchplacement.data.section.MenuSectionResponse
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.IconColor
import com.example.searchplacement.ui.theme.RedPoint
import com.example.searchplacement.ui.theme.White
import com.example.searchplacement.viewmodel.owner.MenuSectionViewModel
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

fun <T> MutableList<T>.move(fromIndex: Int, toIndex: Int) {
    if (fromIndex == toIndex) return
    val item = this[fromIndex]
    removeAt(fromIndex)
    add(toIndex, item)
}


@Composable
fun EditSectionScreen(
    navController: NavHostController,
    storeId: Long
) {
    val menuSectionViewModel: MenuSectionViewModel = hiltViewModel()
    val context = LocalContext.current
    val sections by menuSectionViewModel.sections.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var newSectionName by remember { mutableStateOf("") }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var sectionToDeletePK by remember { mutableStateOf<Long?>(null) }

    val list = remember { mutableStateListOf<MenuSectionResponse>() }

    LaunchedEffect(Unit) {
        menuSectionViewModel.fetchSections(storeId)
    }

    LaunchedEffect(sections) {
        list.clear()
        list.addAll(sections.sortedBy { it.priority })
    }

    val reorderableState = rememberReorderableLazyListState(
        onMove = { from, to ->
            list.move(from.index, to.index)
        }
    )

    val updateResult by menuSectionViewModel.updateResult.collectAsState()
    LaunchedEffect(updateResult) {
        updateResult?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            menuSectionViewModel.clearUpdateResult()
            if (msg.contains("성공")) {
                navController.popBackStack()
                menuSectionViewModel.clearUpdateResult()
            }
        }
    }

    Column(Modifier.fillMaxSize()) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(Dimens.Small),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("섹션 관리", style = AppTextStyle.BodyLarge)
            IconButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "섹션 추가")
            }
        }

        if (showDialog) {
            AlertDialog(
                containerColor = White,
                onDismissRequest = { showDialog = false },
                title = { Text("섹션 추가") },
                text = {
                    OutlinedTextField(
                        value = newSectionName,
                        onValueChange = { newSectionName = it },
                        label = { Text("섹션 이름") }
                    )
                },
                confirmButton = {
                    Button(onClick = {
                        if (newSectionName.isNotBlank()) {
                            val request = MenuSectionRequest(
                                storePK = storeId,
                                name = newSectionName,
                                priority = list.size+1
                            )
                            menuSectionViewModel.addSection(storeId, request) { success ->
                                if(success) {
                                    menuSectionViewModel.fetchSections(storeId)
                                }
                            }
                        }
                        newSectionName = ""
                        showDialog = false
                    }) { Text("저장") }
                }
            )
        }

        if (showDeleteDialog && sectionToDeletePK != null) {
            AlertDialog(
                containerColor = White,
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("삭제 확인") },
                text = { Text("정말 삭제하시겠습니까?") },
                confirmButton = {
                    Button(onClick = {
                        menuSectionViewModel.deleteSection(sectionToDeletePK!!) {success ->
                            if(success) {
                                menuSectionViewModel.fetchSections(storeId)
                            }
                        }
                        showDeleteDialog = false
                    }) {
                        Text("삭제")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDeleteDialog = false }) {
                        Text("취소")
                    }
                }
            )
        }
        if (list.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("섹션이 없습니다. \n 추가 버튼을 눌러 섹션을 등록해주세요.", style = AppTextStyle.Body.copy(color = IconColor))
            }
        } else {
            LazyColumn(
                state = reorderableState.listState,
                modifier = Modifier
                    .weight(1f)
                    .reorderable(reorderableState)
                    .detectReorderAfterLongPress(reorderableState)
            ) {
                itemsIndexed(list, key = { _, item -> item.sectionPK }) { index, section ->
                    ReorderableItem(reorderableState, key = section.sectionPK) { isDragging ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(Dimens.Small)
                                .border(1.dp, if (isDragging) RedPoint else IconColor, RoundedCornerShape(Dimens.Small))
                                .background(White)
                                .padding(Dimens.Default),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("${index + 1}. ${section.name}", style = AppTextStyle.Body)
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(onClick = {
                                    sectionToDeletePK = section.sectionPK
                                    showDeleteDialog = true
                                }) {
                                    Icon(
                                        imageVector = Icons.Outlined.Delete,
                                        contentDescription = "삭제",
                                        tint = RedPoint
                                    )
                                }

                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = null,
                                    tint = IconColor,
                                    modifier = Modifier.padding(start = Dimens.Small)
                                )
                            }
                        }
                    }
                }
            }

            Button(
                onClick = {
                    val requests = list.mapIndexed { index, item ->
                        MenuSectionBulkUpdateRequest(
                            sectionPK = item.sectionPK,
                            name = item.name,
                            priority = index+1
                        )
                    }
                    menuSectionViewModel.bulkUpdateSections(storeId, requests)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.Medium)
            ) {
                Text("저장")
            }
        }
    }
}