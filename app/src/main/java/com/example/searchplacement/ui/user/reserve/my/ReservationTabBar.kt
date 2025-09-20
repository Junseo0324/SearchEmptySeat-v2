package com.example.searchplacement.ui.user.reserve.my

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.searchplacement.ui.theme.Black
import com.example.searchplacement.ui.theme.Gray
import com.example.searchplacement.ui.theme.White

@Composable
fun ReservationTabBar(
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    stateList: List<String>
) {
    TabRow(
        selectedTabIndex = selectedIndex,
        containerColor = White,
        contentColor = Black,
        indicator = { tabPositions: List<TabPosition> ->
            TabRowDefaults.SecondaryIndicator(
                Modifier
                    .tabIndicatorOffset(tabPositions[selectedIndex])
                    .height(2.dp),
                color = Black
            )
        }
    ) {
        stateList.forEachIndexed { index, title ->
            Tab(
                selected = selectedIndex == index,
                onClick = { onTabSelected(index) },
                selectedContentColor = Black,
                unselectedContentColor = Gray,
                text = {
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        fontWeight = if (selectedIndex == index) FontWeight.Bold else FontWeight.Normal
                    )
                }
            )
        }
    }
}
