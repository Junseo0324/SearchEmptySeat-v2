package com.example.searchplacement.presentation.user.home

import com.example.searchplacement.data.local.UserEntity
import com.example.searchplacement.domain.model.MapPinDetailModel

data class HomeState(
    val user: UserEntity? = null,
    val mapPins: List<MapPinUi> = emptyList(),
    val selectedPinDetail: MapPinDetailModel? = null,
    val isLoading: Boolean = false
)

data class MapPinUi(
    val storePK: Long,
    val lat: Double,
    val lng: Double
)
