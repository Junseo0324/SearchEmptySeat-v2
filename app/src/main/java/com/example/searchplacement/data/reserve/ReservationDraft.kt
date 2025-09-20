package com.example.searchplacement.data.reserve

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ReservationDraft(
    val storePK: Long,
    val userId: Long,
    val partySize: Int,
    val reservationTime: String
): Parcelable