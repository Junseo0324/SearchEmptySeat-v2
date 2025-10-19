package com.example.searchplacement.viewmodel.owner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchplacement.data.section.MenuSectionBulkUpdateRequest
import com.example.searchplacement.data.section.MenuSectionRequest
import com.example.searchplacement.data.section.MenuSectionResponse
import com.example.searchplacement.repository.MenuSectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuSectionViewModel @Inject constructor(
    private val menuSectionRepository: MenuSectionRepository
) : ViewModel() {

    private val _sections = MutableStateFlow<List<MenuSectionResponse>>(emptyList())
    val sections: StateFlow<List<MenuSectionResponse>> = _sections.asStateFlow()

    private val _updateResult = MutableStateFlow<String?>(null)
    val updateResult: StateFlow<String?> = _updateResult.asStateFlow()

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

    fun deleteSection(sectionPK: Long, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val res = menuSectionRepository.deleteSection(sectionPK)
            _updateResult.value = res.body()?.message ?: "섹션 삭제 실패"
            onComplete(res.isSuccessful)
        }
    }

    fun bulkUpdateSections(storePK: Long, requests: List<MenuSectionBulkUpdateRequest>) {
        viewModelScope.launch {
            val res = menuSectionRepository.bulkUpdateSections(storePK, requests)
            _updateResult.value = res.body()?.message ?: "섹션 일괄 수정 실패"
        }
    }

    fun clearUpdateResult() {
        _updateResult.value = null
    }



}