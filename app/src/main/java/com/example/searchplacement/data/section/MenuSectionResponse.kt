package com.example.searchplacement.data.section

data class MenuSectionResponse(
    val sectionPK: Long,
    val name: String,
    var priority: Int,
    val createdDate: String?,
    val updatedDate: String?
)
