package com.example.searchplacement.data.mapper

import com.example.searchplacement.data.map.MapPinDetailResponse
import com.example.searchplacement.data.map.MapPinResponse
import com.example.searchplacement.domain.model.MapPinDetailModel
import com.example.searchplacement.domain.model.MapPinModel

fun MapPinResponse.toModel(): MapPinModel {
    return MapPinModel(
        storePK = storePK,
        location = location
    )
}

fun MapPinDetailResponse.toModel(): MapPinDetailModel {
    return MapPinDetailModel(
        storePK = storePK,
        storeName = storeName,
        availableSeats = availableSeats
    )
}
