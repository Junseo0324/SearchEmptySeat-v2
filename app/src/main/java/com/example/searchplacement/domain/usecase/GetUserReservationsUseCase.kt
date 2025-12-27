package com.example.searchplacement.domain.usecase

import com.example.searchplacement.core.util.Result
import com.example.searchplacement.data.reserve.ReservationWithStore
import com.example.searchplacement.domain.repository.ReservationRepository
import com.example.searchplacement.domain.repository.StoreRepository
import javax.inject.Inject

class GetUserReservationsUseCase @Inject constructor(
    private val reservationRepository: ReservationRepository,
    private val storeRepository: StoreRepository
) {
    suspend fun execute(): Result<List<ReservationWithStore>, String> {
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
                    ReservationWithStore(reservation, storeData)
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
