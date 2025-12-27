package com.example.searchplacement.presentation.user.home

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.searchplacement.presentation.theme.AppTextStyle
import com.example.searchplacement.presentation.theme.Black
import com.example.searchplacement.presentation.theme.Dimens
import com.example.searchplacement.presentation.theme.White
import com.example.searchplacement.presentation.user.map.NaverMapContent

@Composable
fun HomeScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    onNavigateToSearch: () -> Unit
) {

    Column {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            NaverMapContent(
                mapPins = state.mapPins,
                onMarkerClick = { storePK ->
                    onAction(HomeAction.OnMarkerClick(storePK))
                }
            )
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopStart)
            ) {
                HeaderSection()

                Spacer(modifier = Modifier.height(Dimens.Medium))

                SearchBar(onNavigateToSearch)
            }

            // 핀 상세 정보 오버레이
            state.selectedPinDetail?.let { detail ->
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
                                onAction(HomeAction.OnStoreDetailClick(detail.storePK))
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
    }
}