package com.example.searchplacement.ui.utils

fun formatTime(iso: String): String {
    return iso.replace("T", " ").substring(0, 16)
}
