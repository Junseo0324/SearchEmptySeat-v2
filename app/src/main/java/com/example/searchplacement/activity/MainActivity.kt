package com.example.searchplacement.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.searchplacement.ui.theme.SearchPlacementTheme
import com.example.searchplacement.ui.user.home.MainView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchPlacementTheme {
                MainView()
            }
        }
    }
}


