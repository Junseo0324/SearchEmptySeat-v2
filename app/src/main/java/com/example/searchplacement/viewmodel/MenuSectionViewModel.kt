package com.example.searchplacement.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchplacement.data.section.MenuSectionBulkUpdateRequest
import com.example.searchplacement.data.section.MenuSectionRequest
import com.example.searchplacement.data.section.MenuSectionResponse
import com.example.searchplacement.repository.MenuSectionRepository
import com.example.searchplacement.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuSectionViewModel @Inject constructor(
    private val menuSectionRepository: MenuSectionRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _sections = MutableStateFlow<List<MenuSectionResponse>>(emptyList())
    val sections: StateFlow<List<MenuSectionResponse>> = _sections.asStateFlow()

    private val _updateResult = MutableStateFlow<String?>(null)
    val updateResult: StateFlow<String?> = _updateResult.asStateFlow()

    /** 메뉴 섹션 전체 조회 */
    fun fetchSections(storePK: Long) {
        viewModelScope.launch {
            val token = userRepository.getUser()?.token ?: ""
            val res = menuSectionRepository.getSections(token, storePK)
            if (res.isSuccessful) {
                _sections.value = res.body()?.data ?: emptyList()
            } else {
                _updateResult.value = res.body()?.message ?: "섹션 조회 실패"
            }
        }
    }

    fun addSection(storePK: Long, request: MenuSectionRequest, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val token = userRepository.getUser()?.token ?: ""
            val res = menuSectionRepository.addSection(token, storePK, request)
            _updateResult.value = res.body()?.message ?: "섹션 추가 실패"
            onComplete(res.isSuccessful)
        }
    }

    /** 메뉴 섹션 개별 업데이트 */
    fun updateSection(sectionPK: Long, request: MenuSectionRequest) {
        viewModelScope.launch {
            val token = userRepository.getUser()?.token ?: ""
            val res = menuSectionRepository.updateSection(token, sectionPK, request)
            _updateResult.value = res.body()?.message ?: "섹션 수정 실패"
        }
    }

    /** 메뉴 섹션 삭제 */
    fun deleteSection(sectionPK: Long, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val token = userRepository.getUser()?.token ?: ""
            val res = menuSectionRepository.deleteSection(token, sectionPK)
            _updateResult.value = res.body()?.message ?: "섹션 삭제 실패"
            onComplete(res.isSuccessful)
        }
    }

    /** 메뉴 섹션 일괄 업데이트 */
    fun bulkUpdateSections(storePK: Long, requests: List<MenuSectionBulkUpdateRequest>) {
        viewModelScope.launch {
            val token = userRepository.getUser()?.token ?: ""
            val res = menuSectionRepository.bulkUpdateSections(token, storePK, requests)
            _updateResult.value = res.body()?.message ?: "섹션 일괄 수정 실패"
        }
    }

    fun clearUpdateResult() {
        _updateResult.value = null
    }



}
