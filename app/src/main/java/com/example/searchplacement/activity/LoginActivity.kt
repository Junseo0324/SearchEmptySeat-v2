package com.example.searchplacement.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.example.searchplacement.navigation.LoginNavigation
import com.example.searchplacement.ui.theme.SearchPlacementTheme
import com.example.searchplacement.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchPlacementTheme {
                val navController = rememberNavController()
                LoginNavigation(navController, loginViewModel)
            }
        }
    }
}