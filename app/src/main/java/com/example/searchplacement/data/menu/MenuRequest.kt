package com.example.searchplacement.data.menu

data class MenuRequest(
    val storePK: Long,         // 소속 매장
    val name: String,          // 메뉴 이름
    val section: String?,      // 섹션 이름 (nullable - 없으면 섹션 생성 안됨)
    val priority: Int,         // 우선순위 (섹션용)
    val price: Int,            // 가격
    val description: String,   // 설명
    val available: Boolean     // 품절 여부
)