package com.example.searchplacement.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchplacement.data.menu.MenuRequest
import com.example.searchplacement.data.menu.MenuResponse
import com.example.searchplacement.data.menu.OutOfStockRequest
import com.example.searchplacement.repository.MenuRepository
import com.example.searchplacement.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val menuRepository: MenuRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _menus = MutableStateFlow<List<MenuResponse>>(emptyList())
    val menus: StateFlow<List<MenuResponse>> = _menus.asStateFlow()

    private val _updateResult = MutableStateFlow<String?>(null)
    val updateResult: StateFlow<String?> = _updateResult.asStateFlow()

    /** 전체 메뉴 조회 */
    fun fetchMenus(storePK: Long) {
        viewModelScope.launch {
            val token = userRepository.getUser()?.token ?: ""
            val res = menuRepository.getMenus(token,storePK)
            if (res.isSuccessful) {
                _menus.value = res.body()?.data ?: emptyList()
            } else {
                _updateResult.value = res.body()?.message ?: "메뉴 조회 실패"
            }
        }
    }

    /** 메뉴 추가 */
    fun addMenu(menuRequest: MenuRequest, imageFile: File?, onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            val token = userRepository.getUser()?.token ?: ""

            val res = menuRepository.addMenu(token,menuRequest, imageFile)
            _updateResult.value = res.body()?.message ?: "메뉴 추가 실패"
            onComplete?.invoke()
            // 추가 후 메뉴 목록 갱신 필요 시 fetchMenus 호출 가능
        }
    }

    /** 메뉴 수정 */
    fun updateMenu(menuId: Long, menuRequest: MenuRequest, imageFile: File?, onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            val token = userRepository.getUser()?.token ?: ""

            val res = menuRepository.updateMenu(token,menuId, menuRequest, imageFile)
            _updateResult.value = res.body()?.message ?: "메뉴 수정 실패"
            onComplete?.invoke()
        }
    }

    /** 메뉴 삭제 */
    fun deleteMenu(menuId: Long, onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            val token = userRepository.getUser()?.token ?: ""

            val res = menuRepository.deleteMenu(token,menuId)
            _updateResult.value = res.body()?.message ?: "메뉴 삭제 실패"
            onComplete?.invoke()
        }
    }

    /** 품절 처리 */
    fun updateMenusStock(request: OutOfStockRequest, onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            val token = userRepository.getUser()?.token ?: ""
            Log.d("updateMenusStock", "Sending updateMenusStock request: $request")
            val res = menuRepository.updateMenusStock(token,request)
            _updateResult.value = res.body()?.message ?: "품절 처리 실패"
            Log.d("updateMenusStock", "Sending updateMenusStock request: ${res.code()}")
            Log.d("updateMenusStock", "Sending updateMenusStock request: ${res.body()}")
            Log.d("updateMenusStock", "Sending updateMenusStock request: ${res.errorBody()}")
            Log.d("updateMenusStock", "Sending updateMenusStock request: ${res.body()}")

            onComplete?.invoke()
        }
    }
}