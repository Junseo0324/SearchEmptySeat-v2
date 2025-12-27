package com.example.searchplacement.domain.usecase

import com.example.searchplacement.core.util.Result
import com.example.searchplacement.data.reserve.ReservationRequest
import com.example.searchplacement.domain.repository.ReservationRepository
import javax.inject.Inject

class CreateReservationUseCase @Inject constructor(
    private val reservationRepository: ReservationRepository
) {
    suspend fun execute(request: ReservationRequest): Result<Unit, String> {
        return try {
            val res = reservationRepository.createReservation(request)
            if (res.status == "success") {
                Result.Success(Unit)
            } else {
                Result.Error(res.message ?: "예약 등록에 실패하였습니다.")
            }
        } catch (e: Exception) {
            Result.Error(e.localizedMessage ?: "네트워크 오류 발생")
        }
    }
}
