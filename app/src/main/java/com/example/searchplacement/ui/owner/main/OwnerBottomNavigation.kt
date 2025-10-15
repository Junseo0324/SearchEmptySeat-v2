package com.example.searchplacement.ui.owner.main

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
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.searchplacement.navigation.OwnerBottomNavItem
import com.example.searchplacement.ui.theme.Black
import com.example.searchplacement.ui.theme.Gray

@Composable
fun OwnerBottomNavigation(
    containerColor: Color,
    contentColor: Color,
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val items = listOf(
        OwnerBottomNavItem.Home,
        OwnerBottomNavItem.Store,
        OwnerBottomNavItem.Reservation,
    )

    NavigationBar(
        modifier = Modifier,
        containerColor = containerColor,
        contentColor = contentColor
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.screenRoute,
                label = {
                    Text(text = item.title, style = MaterialTheme.typography.bodySmall)
                },
                icon = {
                    Icon(
                        imageVector = if (currentRoute == item.screenRoute) item.filledIcon else item.outlinedIcon,
                        contentDescription = item.title
                    )
                },
                onClick = {
                    navController.navigate(item.screenRoute) {
                        navController.graph.startDestinationRoute?.let {
                            popUpTo(it) { saveState = true }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Black,
                    unselectedIconColor = Gray,
                    selectedTextColor = Black,
                    unselectedTextColor = Gray,
                    indicatorColor = Color.Transparent
                )

            )
        }

    }
}