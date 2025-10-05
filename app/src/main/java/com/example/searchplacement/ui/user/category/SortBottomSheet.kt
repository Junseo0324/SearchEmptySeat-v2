package com.example.searchplacement.ui.user.category

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.Gray
import com.example.searchplacement.ui.theme.UserPrimaryColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortBottomSheet(
    currentSort: String,
    sortList: List<Pair<String, String>>,
    onDismiss: () -> Unit,
    onSelectSort: (String, String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Dimens.Medium)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = Dimens.Small),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "정렬 기준",
                    style = AppTextStyle.Body.copy(fontWeight = FontWeight.Bold)
                )
                TextButton(onClick = onDismiss) {
                    Text("완료", style = AppTextStyle.Body.copy(color = UserPrimaryColor))
                }
            }

            Spacer(modifier = Modifier.height(Dimens.Small))

            sortList.forEach { (displayName, value) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectSort(displayName, value) }
                        .padding(horizontal = 20.dp, vertical = Dimens.Medium),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = displayName,
                        style = AppTextStyle.Body.copy(
                            color = if (currentSort == value) UserPrimaryColor else Gray,
                            fontWeight = if (currentSort == value) FontWeight.Bold else FontWeight.Normal
                        )
                    )
                    if (currentSort == value) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = UserPrimaryColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(Dimens.Medium))
        }
    }
}