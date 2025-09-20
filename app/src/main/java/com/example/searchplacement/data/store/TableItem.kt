package com.example.searchplacement.data.store

import java.util.UUID

data class TableItem(
    val id: String = UUID.randomUUID().toString(),
    val type: String, // 예: "1x1", "2x2"
    val minPeople: Int,
    val maxPeople: Int,
    var offsetX: Float,
    var offsetY: Float,
    var status: Boolean = true // true = 사용 가능
)
