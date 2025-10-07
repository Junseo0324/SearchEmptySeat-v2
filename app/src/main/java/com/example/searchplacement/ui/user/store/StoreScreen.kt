package com.example.searchplacement.ui.user.store

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Black
import com.example.searchplacement.ui.theme.ButtonMainColor
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.IconColor
import com.example.searchplacement.ui.theme.IconTextColor
import com.example.searchplacement.ui.theme.White
import com.example.searchplacement.ui.user.store.menu.MenuDisplayScreen
import com.example.searchplacement.ui.user.store.reservation.ReservationStatusContent
import com.example.searchplacement.ui.user.store.review.StoreReview
import com.example.searchplacement.ui.user.store.table.TableViewScreen
import com.example.searchplacement.viewmodel.StoreViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreScreen(
    navController: NavHostController,
    storeId: Long,
    storeViewModel: StoreViewModel
) {
    val tabs = remember { listOf("예약현황", "리뷰", "자리현황", "메뉴") }

    var selectedTabIndex by remember { mutableStateOf(0) }

    val storeData = storeViewModel.storeData.collectAsState()
    val isFavorite by storeViewModel.isFavorite.collectAsState()
    val scrollState = rememberScrollState()


    LaunchedEffect(storeId) {
        storeViewModel.getStoreData(storeId)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            storeData.value?.let { store ->
                StoreImageGallery(
                    store = store,
                    onBackClick = { navController.popBackStack() },
                    isFavorite = isFavorite,
                    onFavoriteClick = {
                        storeViewModel.toggleFavorite(storeId)
                    }
                )

                StoreInfoSection(store = store, navController = navController)

                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = White,
                    contentColor = Black,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            height = 3.dp,
                            color = Black
                        )
                    }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            modifier = Modifier.height(48.dp)
                        ) {
                            Text(
                                text = title,
                                fontSize = 15.sp,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTabIndex == index) IconTextColor else IconColor
                            )
                        }
                    }
                }

                when (selectedTabIndex) {
                    0 -> ReservationStatusContent()
                    1 -> StoreReview(storeId)
                    2 -> TableViewScreen(storeId)
                    3 -> MenuDisplayScreen(storeId)
                }
            } ?: run {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
        storeData.value?.let {
            Button(
                onClick = {
                    navController.navigate("reserveStore")
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(Dimens.Medium)
                    .height(56.dp),
                shape = RoundedCornerShape(Dimens.Default),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ButtonMainColor
                )
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(Dimens.Small))
                Text(
                    text = "예약하기",
                    style = AppTextStyle.Body.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}