package com.example.searchplacement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.example.searchplacement.core.util.TokenManager
import com.example.searchplacement.domain.repository.UserRepository
import com.example.searchplacement.presentation.user.main.MyApp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 앱 실행 시 토큰 복구
        lifecycleScope.launch {
            val user = userRepository.getUser()
            if (user != null) {
                TokenManager.setToken(user.token)
            }
        }

        setContent {
            MyApp()
        }
    }
}