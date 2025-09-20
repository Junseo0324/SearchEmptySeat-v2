package com.example.searchplacement.data.review

data class ReviewRequest(
    val userPK: Long,
    val storePK: Long,
    val image: List<String> = emptyList(),
    val rating: Int,
    val content: String
)
