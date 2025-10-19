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


    fun fetchSections(storePK: Long) {
        viewModelScope.launch {
            val res = menuSectionRepository.getSections(storePK)
            if (res.isSuccessful) {
                _sections.value = res.body()?.data ?: emptyList()
            }
        }
    }

    fun addSection(storePK: Long, request: MenuSectionRequest, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val res = menuSectionRepository.addSection(storePK, request)
            onComplete(res.isSuccessful)
        }
    }

    fun deleteSection(sectionPK: Long, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val res = menuSectionRepository.deleteSection(sectionPK)
            onComplete(res.isSuccessful)
        }
    }

    fun bulkUpdateSections(storePK: Long, requests: List<MenuSectionBulkUpdateRequest>) {
        viewModelScope.launch {
            menuSectionRepository.bulkUpdateSections(storePK, requests)
        }
    }



}