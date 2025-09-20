package com.example.searchplacement.data.section

data class MenuSectionBulkUpdateRequest(
    val sectionPK: Long,
    val name: String,
    val priority: Int
)
