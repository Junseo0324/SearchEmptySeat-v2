package com.example.searchplacement.data.placement

import androidx.compose.runtime.MutableState


data class TableData(
    val id: String,         // 예: "1", "2", ...
    val x: MutableState<Float>,
    val y: MutableState<Float>,
    val table: Int,         // 최대 인원 (ex: 2, 4, 8 ...)
    val min: MutableState<Int>,
    var status: MutableState<Int>         // 0=빈자리, 1=예약, 2=사용중
)

data class TableLayoutData(
    val x: Int,
    val y: Int,
    val table: Int,
    val min: Int,
    val status: Int
)