package com.example.searchplacement.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.searchplacement.navigation.AppNavigation
import com.example.searchplacement.navigation.MainBottomNavItem
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.SearchPlacementTheme
import com.example.searchplacement.ui.theme.UserPrimaryColor
import com.example.searchplacement.ui.theme.White
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchPlacementTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                val bottomNavRoutes = MainBottomNavItem.items.map { it.screenRoute }
                val showBottomBar = bottomNavRoutes.contains(currentRoute)
                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
                            NavigationBar(
                                containerColor = White
                            ) {
                                MainBottomNavItem.items.forEach { item ->
                                    val selected = currentRoute?.startsWith(item.screenRoute) == true
                                    NavigationBarItem(
                                        icon = { Icon(item.icon, contentDescription = item.title) },
                                        label = { Text(item.title, style = AppTextStyle.BodySmall) },
                                        selected = selected,
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
                                            indicatorColor = Color.Transparent,
                                            selectedIconColor = UserPrimaryColor,
                                            unselectedIconColor = Color.Black,
                                            selectedTextColor = UserPrimaryColor,
                                            unselectedTextColor = Color.Black
                                        ),
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    AppNavigation(navController = navController,modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}


