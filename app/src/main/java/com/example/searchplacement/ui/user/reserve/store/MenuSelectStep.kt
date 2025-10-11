package com.example.searchplacement.ui.user.reserve.store

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.searchplacement.data.menu.MenuItemData
import com.example.searchplacement.data.menu.MenuResponse
import com.example.searchplacement.data.reserve.ReservationData
import com.example.searchplacement.data.section.MenuSectionResponse
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.IconTextColor
import com.example.searchplacement.ui.theme.White

@Composable
fun MenuSelectStep(
    reservationData: ReservationData,
    menus: List<MenuResponse>,
    sections: List<MenuSectionResponse>,
    onUpdateMenus: (MutableMap<String, MenuItemData>) -> Unit
) {
    val sortSections = sections.sortedBy { it.priority }
    val sectionedMenus = sortSections.associateWith { section ->
        menus.filter { it.section?.sectionPK == section.sectionPK }
    }

    LaunchedEffect(reservationData.selectedMenus) {
        reservationData.totalPrice = reservationData.selectedMenus.values
            .sumOf { it.price * it.quantity }
        onUpdateMenus(reservationData.selectedMenus)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(Dimens.Large)
        ) {
            sectionedMenus.forEach { (section, menuList) ->
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(Dimens.Default)) {
                        Text(
                            text = section.name,
                            style = AppTextStyle.Body.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = IconTextColor)
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            menuList.forEach { menu ->
                                if (menu.available) {
                                    MenuItemCard(
                                        menu = menu,
                                        quantity = reservationData.selectedMenus[menu.menuPK.toString()]?.quantity
                                            ?: 0,
                                        onQuantityChanged = { newQuantity ->
                                            val newSelectedMenus = reservationData.selectedMenus.toMutableMap()

                                            if (newQuantity > 0) {
                                                newSelectedMenus[menu.menuPK.toString()] = MenuItemData(
                                                    menuId = menu.menuPK.toString(),
                                                    name = menu.name,
                                                    price = menu.price,
                                                    quantity = newQuantity
                                                )
                                            } else {
                                                newSelectedMenus.remove(menu.menuPK.toString())
                                            }

                                            onUpdateMenus(newSelectedMenus)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 총 주문 금액
        if (reservationData.totalPrice > 0) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                elevation = CardDefaults.cardElevation(defaultElevation = Dimens.Small)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "총 주문 금액",
                        style = AppTextStyle.Body.copy(fontWeight = FontWeight.Bold, color = IconTextColor)
                    )
                    Text(
                        text = "${String.format("%,d", reservationData.totalPrice)}원",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1565C0)
                    )
                }
            }
        }
    }
}