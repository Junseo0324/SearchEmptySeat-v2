package com.example.searchplacement.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchplacement.data.menu.MenuRequest
import com.example.searchplacement.data.menu.MenuResponse
import com.example.searchplacement.data.menu.OutOfStockRequest
import com.example.searchplacement.data.section.MenuSectionRequest
import com.example.searchplacement.data.section.MenuSectionResponse
import com.example.searchplacement.repository.MenuRepository
import com.example.searchplacement.repository.MenuSectionRepository
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
    private val menuSectionRepository: MenuSectionRepository
) : ViewModel() {

    private val _menus = MutableStateFlow<List<MenuResponse>>(emptyList())
    val menus: StateFlow<List<MenuResponse>> = _menus.asStateFlow()


    private val _sections = MutableStateFlow<List<MenuSectionResponse>>(emptyList())
    val sections: StateFlow<List<MenuSectionResponse>> = _sections.asStateFlow()


    private val _updateResult = MutableStateFlow<String?>(null)
    val updateResult: StateFlow<String?> = _updateResult.asStateFlow()

    fun fetch(storeId: Long) {
        viewModelScope.launch {
            val menuRes = menuRepository.getMenus(storeId)
            val sectionRes = menuSectionRepository.getSections(storeId)
            if (menuRes.isSuccessful && sectionRes.isSuccessful) {
                _menus.value = menuRes.body()?.data ?: emptyList()
                _sections.value = sectionRes.body()?.data ?: emptyList()
            }
        }
    }

    /** 전체 메뉴 조회 */
    fun fetchMenus(storePK: Long) {
        viewModelScope.launch {
            val res = menuRepository.getMenus(storePK)
            if (res.isSuccessful) {
                _menus.value = res.body()?.data ?: emptyList()
            } else {
                _updateResult.value = res.body()?.message ?: "메뉴 조회 실패"
            }
        }
    }

    fun fetchSections(storePK: Long) {
        viewModelScope.launch {
            val res = menuSectionRepository.getSections(storePK)
            if (res.isSuccessful) {
                _sections.value = res.body()?.data ?: emptyList()
            } else {
                _updateResult.value = res.body()?.message ?: "섹션 조회 실패"
            }
        }
    }

    fun addSection(storePK: Long, request: MenuSectionRequest, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val res = menuSectionRepository.addSection(storePK, request)
            _updateResult.value = res.body()?.message ?: "섹션 추가 실패"
            onComplete(res.isSuccessful)
        }
    }

    /** 메뉴 추가 */
    fun addMenu(menuRequest: MenuRequest, imageFile: File?, onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {

            val res = menuRepository.addMenu(menuRequest, imageFile)
            _updateResult.value = res.body()?.message ?: "메뉴 추가 실패"
            onComplete?.invoke()
            // 추가 후 메뉴 목록 갱신 필요 시 fetchMenus 호출 가능
        }
    }

    /** 메뉴 수정 */
    fun updateMenu(menuId: Long, menuRequest: MenuRequest, imageFile: File?, onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {

            val res = menuRepository.updateMenu(menuId, menuRequest, imageFile)
            _updateResult.value = res.body()?.message ?: "메뉴 수정 실패"
            onComplete?.invoke()
        }
    }

    /** 메뉴 삭제 */
    fun deleteMenu(menuId: Long, onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {

            val res = menuRepository.deleteMenu(menuId)
            _updateResult.value = res.body()?.message ?: "메뉴 삭제 실패"
            onComplete?.invoke()
        }
    }

    /** 품절 처리 */
    fun updateMenusStock(request: OutOfStockRequest, onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            val res = menuRepository.updateMenusStock(request)
            _updateResult.value = res.body()?.message ?: "품절 처리 실패"

            onComplete?.invoke()
        }
    }
}