package com.example.searchplacement.domain.usecase

import com.example.searchplacement.core.util.Result
import com.example.searchplacement.domain.repository.ReservationRepository
import javax.inject.Inject

class CancelReservationUseCase @Inject constructor(
    private val reservationRepository: ReservationRepository
) {
    suspend fun execute(reservationId: Long): Result<Unit, String> {
        return try {
            val res = reservationRepository.cancelReservation(reservationId)
            if (res.status == "success") {
                Result.Success(Unit)
            } else {
                Result.Error(res.message ?: "예약 취소에 실패했습니다.")
            }
        } catch (e: Exception) {
            Result.Error(e.localizedMessage ?: "네트워크 오류 발생")
        }
    }
}
