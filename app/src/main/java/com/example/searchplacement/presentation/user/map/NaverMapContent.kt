package com.example.searchplacement.presentation.user.map

import android.graphics.Color
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.searchplacement.presentation.user.home.MapPinUi
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker

@Composable
fun NaverMapContent(
    mapPins: List<MapPinUi>,
    onMarkerClick: (Long) -> Unit
) {
    val context = LocalContext.current
    val naverMapHolder = remember { mutableStateOf<NaverMap?>(null) }

    AndroidView(
        factory = {
            MapView(context).apply {
                getMapAsync { naverMap ->
                    naverMapHolder.value = naverMap
                    val cameraUpdate = CameraUpdate.scrollTo(LatLng(37.5665, 126.9780))
                    naverMap.moveCamera(cameraUpdate)
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    )

    LaunchedEffect(mapPins, naverMapHolder.value) {
        val naverMap = naverMapHolder.value ?: return@LaunchedEffect

        mapPins.forEach { pin ->
            Marker().apply {
                position = LatLng(pin.lat, pin.lng)
                map = naverMap
                iconTintColor = androidx.compose.ui.graphics.Color(0xFF4CAF50).toArgb()
                width = 80
                height = 80
                captionText = "가게"
                captionColor = Color.BLACK
                captionTextSize = 14f

                setOnClickListener {
                    onMarkerClick(pin.storePK)
                    true
                }
            }
        }
    }
}