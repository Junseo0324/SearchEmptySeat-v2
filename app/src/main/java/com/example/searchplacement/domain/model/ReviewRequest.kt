package com.example.searchplacement.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ReviewRequest(
    val storePK: Long,
    val rating: Float,
    val content: String
)
