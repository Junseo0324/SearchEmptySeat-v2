package com.example.searchplacement.presentation.owner.menu_manage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchplacement.data.menu.MenuResponse
import com.example.searchplacement.data.menu.MenuStockDto
import com.example.searchplacement.data.menu.OutOfStockRequest
import com.example.searchplacement.data.section.MenuSectionResponse
import com.example.searchplacement.domain.repository.MenuRepository
import com.example.searchplacement.domain.repository.MenuSectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EditMenuViewModel @Inject constructor(
    private val menuRepository: MenuRepository,
    private val menuSectionRepository: MenuSectionRepository
) : ViewModel() {

    private val _menus = MutableStateFlow<List<MenuResponse>>(emptyList())
    val menus: StateFlow<List<MenuResponse>> = _menus.asStateFlow()

    private val _updateResult = MutableStateFlow<String?>(null)
    val updateResult: StateFlow<String?> = _updateResult.asStateFlow()

    private val _stockState = MutableStateFlow<Map<Long, Boolean>>(emptyMap())
    val stockState: StateFlow<Map<Long, Boolean>> = _stockState.asStateFlow()

    private val _sections = MutableStateFlow<List<MenuSectionResponse>>(emptyList())
    val sections: StateFlow<List<MenuSectionResponse>> = _sections.asStateFlow()

    fun fetch(storeId: Long) {
        viewModelScope.launch {
            val menusRes = menuRepository.getMenus(storeId)
            val sectionRes = menuSectionRepository.getSections(storeId)

            val menuList = menusRes?.data ?: emptyList()
            _menus.value = menuList
            _sections.value = sectionRes?.data ?: emptyList()

            _stockState.value = menuList.associate { it.menuPK to it.available }
        }
    }

    fun toggleMenu(menuId: Long, available: Boolean) {
        _stockState.value = _stockState.value.toMutableMap().apply {
            this[menuId] = available
        }
    }

    fun updateStock() {
        viewModelScope.launch {
            val dto = _stockState.value.map { MenuStockDto(it.key, it.value) }
            val request = OutOfStockRequest(dto)

            val res = menuRepository.updateMenusStock(request)
            _updateResult.value = res?.message ?: "업데이트 실패"
        }
    }

}