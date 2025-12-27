package com.example.searchplacement.presentation.user.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import com.example.searchplacement.core.navigation.MainBottomNavItem
import com.example.searchplacement.presentation.theme.UserPrimaryColor
import com.example.searchplacement.presentation.theme.White


@Composable
fun MainBottomBar(
    containerColor: Color,
    contentColor: Color,
    currentRoute: String?,
    onItemClick: (String) -> Unit
) {

    val items = listOf(
        MainBottomNavItem.Home,
        MainBottomNavItem.Category,
        MainBottomNavItem.Reserve,
        MainBottomNavItem.Favorite,
        MainBottomNavItem.Setting
    )

    AnimatedVisibility(
        visible = items.map { it.screenRoute }.contains(currentRoute)
    ) {
        NavigationBar(
            modifier = Modifier,
            containerColor = White,
            contentColor = contentColor,
        ) {
            items.forEach { item ->
                NavigationBarItem(
                    selected = currentRoute == item.screenRoute,
                    label = {
                        Text(text = item.title, style = MaterialTheme.typography.bodySmall)
                    },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent,
                        selectedIconColor = UserPrimaryColor,
                        unselectedIconColor = Color.Black,
                        selectedTextColor = UserPrimaryColor,
                        unselectedTextColor = Color.Black
                    ),
                    onClick = {
                        onItemClick(item.screenRoute)
                    }
                )
            }
        }
    }
}
