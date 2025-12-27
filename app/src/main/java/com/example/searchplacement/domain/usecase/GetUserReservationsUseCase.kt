package com.example.searchplacement.domain.usecase

import com.example.searchplacement.core.util.Result
import com.example.searchplacement.data.mapper.ReservationMapper
import com.example.searchplacement.data.mapper.toModel
import com.example.searchplacement.domain.model.ReservationWithStoreModel
import com.example.searchplacement.domain.repository.ReservationRepository
import com.example.searchplacement.domain.repository.StoreRepository
import javax.inject.Inject

class GetUserReservationsUseCase @Inject constructor(
    private val reservationRepository: ReservationRepository,
    private val storeRepository: StoreRepository
) {
    suspend fun execute(): Result<List<ReservationWithStoreModel>, String> {
        return try {
            val res = reservationRepository.getUserReservations()
            if (res.status == "success") {
                val reservations = res.data ?: emptyList()
                val combinedList = reservations.map { reservation ->
                    val storeData = try {
                        val storeRes = storeRepository.getStoreData(reservation.storePK)
                        if (storeRes.status == "success") storeRes.data else null
                    } catch (e: Exception) {
                        null
                    }
                    val reservationModel = ReservationMapper.toDomain(reservation)
                    val storeModel = storeData?.toModel()
                    ReservationWithStoreModel(reservationModel, storeModel)
                }
                Result.Success(combinedList)
            } else {
                Result.Error(res.message ?: "예약 목록을 불러오는데 실패했습니다.")
            }
        } catch (e: Exception) {
            Result.Error(e.localizedMessage ?: "네트워크 오류 발생")
        }
    }
}
