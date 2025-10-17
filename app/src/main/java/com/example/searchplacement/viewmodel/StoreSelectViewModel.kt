package com.example.searchplacement.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchplacement.data.dto.StoreSelectDTO
import com.example.searchplacement.data.store.StoreResponse
import com.example.searchplacement.repository.OwnerStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreSelectViewModel @Inject constructor(
    private val ownerStoreRepository: OwnerStoreRepository
) : ViewModel() {
    private val _myStores = MutableStateFlow<List<StoreSelectDTO>>(emptyList())
    val myStores = _myStores.asStateFlow()

    fun fetchMyStores() {
        viewModelScope.launch {
            val response = ownerStoreRepository.getMyStores()
            if (response.isSuccessful) {
                val stores = response.body()?.data  ?: emptyList()

                _myStores.value = stores.map {
                    StoreSelectDTO(
                        storePK = it.storePK,
                        storeName = it.storeName,
                        location = it.location,
                        description = it.description,
                        businessRegistrationNumber = it.businessRegistrationNumber,
                        bank = it.bank,
                        accountNumber = it.accountNumber,
                        depositor = it.depositor,
                        businessHours = it.businessHours,
                        image = it.image,
                        category = it.category,
                        viewCount = it.viewCount,
                        averageRating = it.averageRating,
                        favoriteCount = it.favoriteCount,
                        regularHolidays = it.regularHolidays,
                        temporaryHolidays = it.temporaryHolidays,
                        isAvailable = isStoreOpen(it)
                    )
                }
            }
        }
    }

    private fun isStoreOpen(store: StoreResponse): Boolean {
        val today = java.time.LocalDate.now()
        val dayOfWeek = today.dayOfWeek.name

        val koreanDay = when (dayOfWeek) {
            "MONDAY" -> "월요일"
            "TUESDAY" -> "화요일"
            "WEDNESDAY" -> "수요일"
            "THURSDAY" -> "목요일"
            "FRIDAY" -> "금요일"
            "SATURDAY" -> "토요일"
            "SUNDAY" -> "일요일"
            else -> ""
        }

        if (store.temporaryHolidays!!.contains(today.toString())) {
            return false
        }

        val isHoliday = store.regularHolidays?.get(koreanDay) == 1
        if (isHoliday) return false

        val now = java.time.LocalTime.now()
        val hours = store.businessHours[koreanDay] ?: return false

        val (openStr, closeStr) = hours.split(" - ")
        val openTime = java.time.LocalTime.parse(openStr)
        val closeTime = java.time.LocalTime.parse(closeStr)

        return now.isAfter(openTime) && now.isBefore(closeTime)
    }
}
