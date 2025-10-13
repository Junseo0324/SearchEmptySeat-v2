package com.example.searchplacement.ui.user.home

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
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.searchplacement.navigation.MainBottomNavItem
import com.example.searchplacement.ui.theme.UserPrimaryColor


@Composable
fun MainBottomBar(
    containerColor: Color,
    contentColor: Color,
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
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
            containerColor = containerColor,
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
                        navController.navigate(item.screenRoute) {
                            navController.graph.startDestinationRoute?.let {
                                popUpTo(it) { saveState = true }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}
