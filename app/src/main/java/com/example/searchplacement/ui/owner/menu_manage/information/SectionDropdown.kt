package com.example.searchplacement.ui.owner.menu_manage.information

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.searchplacement.data.section.MenuSectionResponse
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.White

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
            containerColor = White,
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth().padding(Dimens.Medium)
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
                modifier = Modifier.background(White),
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Add, contentDescription = "섹션 추가")
                        Spacer(Modifier.width(Dimens.Small))
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