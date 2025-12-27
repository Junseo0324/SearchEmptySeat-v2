package com.example.searchplacement.data.mapper

import com.example.searchplacement.data.reserve.ReservationResponse
import com.example.searchplacement.domain.model.ReservationModel

object ReservationMapper {
    fun toDomain(dto: ReservationResponse): ReservationModel {
        return ReservationModel(
            reservationPK = dto.reservationPK,
            userId = dto.userId,
            storePK = dto.storePK,
            reservationNum = dto.reservationNum,
            reservationTime = dto.reservationTime,
            tableNumber = dto.tableNumber,
            menu = dto.menu,
            seats = dto.seats,
            partySize = dto.partySize,
            paymentMethod = dto.paymentMethod,
            status = dto.status,
            createdDate = dto.createdDate,
            endDate = dto.endDate
        )
    }
}
