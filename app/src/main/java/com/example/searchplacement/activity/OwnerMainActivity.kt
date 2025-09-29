package com.example.searchplacement.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.searchplacement.navigation.OwnerBottomNavItem
import com.example.searchplacement.navigation.OwnerNavigation
import com.example.searchplacement.ui.theme.Black
import com.example.searchplacement.ui.theme.Gray
import com.example.searchplacement.ui.theme.SearchPlacementTheme
import com.example.searchplacement.viewmodel.MainViewModel
import com.example.searchplacement.viewmodel.MenuSectionViewModel
import com.example.searchplacement.viewmodel.MenuViewModel
import com.example.searchplacement.viewmodel.OwnerStoreViewModel
import com.example.searchplacement.viewmodel.PlacementViewModel
import com.example.searchplacement.viewmodel.StoreListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OwnerMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SearchPlacementTheme {
                OwnerMainView()
            }
        }
    }
}

@Composable
fun OwnerMainView() {
    val navController = rememberNavController()
    val storeListViewModel: StoreListViewModel = hiltViewModel()
    val mainViewModel: MainViewModel = hiltViewModel()
    val ownerStoreViewModel: OwnerStoreViewModel = hiltViewModel()
    val placementViewModel: PlacementViewModel = hiltViewModel()
    val menuSectionViewModel: MenuSectionViewModel = hiltViewModel()
    val menuViewModel: MenuViewModel = hiltViewModel()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavRoutes = listOf(
        OwnerBottomNavItem.Home.screenRoute,
        OwnerBottomNavItem.Store.screenRoute,
        OwnerBottomNavItem.Reservation.screenRoute
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (currentRoute in bottomNavRoutes) {
                OwnerBottomNavigation(
                    Color.White, Color.Black, navController
                )
            }
        }
    ) { padding ->
        OwnerNavigation(navController, storeListViewModel,mainViewModel,ownerStoreViewModel,placementViewModel,menuSectionViewModel,menuViewModel)
    }
}

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