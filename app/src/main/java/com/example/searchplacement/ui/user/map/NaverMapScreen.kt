package com.example.searchplacement.ui.user.map

import android.graphics.Color
import android.location.Geocoder
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Black
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.White
import com.example.searchplacement.ui.user.home.MapViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import java.util.Locale

@Composable
fun NaverMapScreen(
    navController: NavHostController,
    mapViewModel: MapViewModel
) {
    val context = LocalContext.current
    val mapPins by mapViewModel.mapPins.collectAsState()
    val mapPinDetail by mapViewModel.mapPinDetail.collectAsState()



    LaunchedEffect(Unit) {
        mapViewModel.loadMapPins()
    }



    val naverMapHolder = remember { mutableStateOf<NaverMap?>(null) }

    Box(
        modifier = Modifier
            .padding(Dimens.Small)
            .border(1.dp, Black, shape = RoundedCornerShape(12.dp)),
    ) {
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

        if (mapPinDetail?.data != null) {
            val detail = mapPinDetail!!.data!!
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(Dimens.Small)
                    .border(1.dp, Black, RoundedCornerShape(12.dp))
                    .background(White.copy(alpha = 0.7f))
                    .padding(Dimens.Medium)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = detail.storeName, style = AppTextStyle.BodyLarge)
                        Spacer(modifier = Modifier.height(Dimens.Tiny))
                        Text(text = "빈자리: ${detail.availableSeats}석")
                    }
                    IconButton(
                        onClick = {
                            navController.navigate("store/${detail.storePK}")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "가게로 이동"
                        )
                    }

                }
            }
        }
    }

    LaunchedEffect(mapPins) {
        val naverMap = naverMapHolder.value ?: return@LaunchedEffect
        val geocoder = Geocoder(context, Locale.KOREA)

        mapPins?.data?.forEach { pin ->
            try {
                val addr = geocoder.getFromLocationName(pin.location, 1)
                if (!addr.isNullOrEmpty()) {
                    val lat = addr[0].latitude
                    val lng = addr[0].longitude
                    val marker = Marker().apply {
                        position = LatLng(lat, lng)
                        map = naverMap
                        iconTintColor = androidx.compose.ui.graphics.Color.Red.toArgb()
                        width = 80
                        height = 80
                        captionText = "가게"
                        captionColor = Color.BLACK
                        captionTextSize = 14f

                        setOnClickListener {
                            mapViewModel.loadMapPinDetail(pin.storePK)
                            true
                        }
                    }

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    LaunchedEffect(naverMapHolder.value) {
        val naverMap = naverMapHolder.value ?: return@LaunchedEffect
        val geocoder = Geocoder(context, Locale.KOREA)

        mapPins?.data?.forEach { pin ->
            try {
                val addr = geocoder.getFromLocationName(pin.location, 1)
                if (!addr.isNullOrEmpty()) {
                    val lat = addr[0].latitude
                    val lng = addr[0].longitude
                    val marker = Marker().apply {
                        position = LatLng(lat, lng)
                        map = naverMap
                        iconTintColor = androidx.compose.ui.graphics.Color.Red.toArgb()
                        width = 80
                        height = 80
                        captionText = "가게"
                        captionColor = Color.BLACK
                        captionTextSize = 14f


                        setOnClickListener {
                            mapViewModel.loadMapPinDetail(pin.storePK)
                            true
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}