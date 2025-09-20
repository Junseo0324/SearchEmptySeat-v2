package com.example.searchplacement.ui.user.store

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.searchplacement.data.store.StoreResponse
import com.example.searchplacement.ui.theme.AppTextStyle
import com.google.android.gms.location.LocationServices
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun StoreMapScreen(navController: NavHostController, store: StoreResponse) {
    val context = LocalContext.current
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    val mapView = remember { MapView(context) }
    var naverMap by remember { mutableStateOf<NaverMap?>(null) }

    var storeLatLng by remember { mutableStateOf<LatLng?>(null) }
    var userLatLng by remember { mutableStateOf<LatLng?>(null) }

    var storeMarker: Marker? by remember { mutableStateOf(null) }
    var userMarker: Marker? by remember { mutableStateOf(null) }

    var showStoreInfo by remember { mutableStateOf(false) }

    val today = LocalDate.now()
    val todayKorean = today.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREAN)
    val todayHours = store.businessHours[todayKorean] ?: "휴무일"
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    userLatLng = LatLng(it.latitude, it.longitude)
                }
            }
        } else {
            Log.w("StoreMapScreen", "위치 권한 거부됨")
        }
    }

    // 매장 주소 → 위경도
    LaunchedEffect(store.location) {
        try {
            val geocoder = Geocoder(context, Locale.KOREAN)
            val result = geocoder.getFromLocationName(store.location, 1)
            if (!result.isNullOrEmpty()) {
                val loc = result[0]
                storeLatLng = LatLng(loc.latitude, loc.longitude)
            }
        } catch (e: Exception) {
            Log.e("StoreMapScreen", "Geocoding 실패: ${e.message}")
        }
    }

    // 현재 위치 요청
    LaunchedEffect(Unit) {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        val granted = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        if (granted) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    userLatLng = LatLng(it.latitude, it.longitude)
                }
            }
        } else {
            permissionLauncher.launch(permission)
        }
    }

    // 마커 표시 및 카메라 이동
    LaunchedEffect(naverMap, storeLatLng, userLatLng) {
        naverMap?.let { nMap ->
            // 기존 마커 제거
            storeMarker?.map = null
            userMarker?.map = null

            // 매장 마커 갱신
            storeLatLng?.let {
                storeMarker = Marker().apply {
                    position = it
                    captionText = store.storeName
                    map = nMap
                    setOnClickListener {
                        showStoreInfo = true
                        true
                    }
                }
            }

            // 사용자 마커 갱신
            userLatLng?.let {
                userMarker = Marker().apply {
                    position = it
                    captionText = "내 위치"
                    iconTintColor = Color.Blue.hashCode()
                    width = 60
                    height = 60
                    map = nMap
                }
            }

//            // 카메라 이동: 두 위치가 모두 있는 경우 → 자동 줌 아웃
//            if (storeLatLng != null && userLatLng != null) {
//                val bounds = LatLngBounds.Builder()
//                    .include(storeLatLng)
//                    .include(userLatLng)
//                    .build()
//
//                val cameraUpdate = CameraUpdate.fitBounds(bounds, 100)
//                    .animate(CameraAnimation.Fly, 1000)
//
//                nMap.moveCamera(cameraUpdate)
//            } else if (storeLatLng != null) {
//                // 매장만 있을 경우
//                nMap.moveCamera(
//                    CameraUpdate.scrollTo(storeLatLng!!).animate(CameraAnimation.Fly, 1000)
//                )
//
//
//            }

            nMap.moveCamera(
                CameraUpdate.scrollTo(storeLatLng!!).animate(CameraAnimation.Fly, 1000)
            )
        }
    }


    Column(Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxSize()) {
            AndroidView(
                factory = {
                    mapView.apply {
                        getMapAsync { map -> naverMap = map }
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            // 뒤로 가기 버튼
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
                    .background(Color.White, shape = CircleShape)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로 가기")
            }

            if (showStoreInfo) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                        .background(Color.White.copy(alpha = 0.95f), shape = RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    //todo 이미지 추가 예정

                    Text(text = store.storeName, style = AppTextStyle.BodyLarge)
                    Text(text = store.location, style = AppTextStyle.Body)
//                    Text(text = "별점: ${String.format("%.1f", store.averageRating)}", style = AppTextStyle.redPoint)
                    Text(text = todayHours, style = AppTextStyle.Caption.copy(fontSize = 14.sp))
                    Button(
                        onClick = {
                            navController.popBackStack()
                            navController.navigate("store/${store.storePK}")
                        },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("매장 상세 보기")
                    }
                }
            }
        }
    }
}
