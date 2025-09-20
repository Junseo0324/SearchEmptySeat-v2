package com.example.searchplacement.data.placement

data class PlacementResponse(
    val placementPK: Long,
    val storePK: Long,
    val layout: Map<String, TableLayoutData>,
    val createdDate: String,
    val updatedDate: String,
    val layoutSize: Int
)