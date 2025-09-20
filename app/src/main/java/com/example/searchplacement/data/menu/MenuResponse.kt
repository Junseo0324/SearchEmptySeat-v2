package com.example.searchplacement.data.menu

import com.example.searchplacement.data.section.MenuSectionResponse

data class MenuResponse(
    val menuPK: Long,                   // 메뉴 ID
    val name: String,                   // 메뉴 이름
    val section: MenuSectionResponse?, // 연결된 섹션 정보
    val image: List<String>?,          // 이미지 URL 리스트
    val price: Int,                    // 가격
    val description: String,           // 설명
    val available: Boolean           // 품절 여부
)

