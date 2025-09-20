package com.example.searchplacement.data.placement

data class PlacementRequest(
    val storePK: Long,
    val layout: Map<String, TableLayoutData>,
    val layoutSize: Int
)
