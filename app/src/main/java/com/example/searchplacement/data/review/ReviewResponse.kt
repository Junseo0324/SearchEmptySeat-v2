package com.example.searchplacement.data.review

data class ReviewResponse(
    val reviewPK: Long,
    val userName: String,
    val rating: Double,
    val content: String,
    val image: List<String> = emptyList(),
    val createdDate: String
)
