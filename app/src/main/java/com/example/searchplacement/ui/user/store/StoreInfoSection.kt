package com.example.searchplacement.ui.user.store

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.searchplacement.data.store.StoreResponse
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.IconColor
import com.example.searchplacement.ui.theme.IconTextColor
import com.example.searchplacement.ui.theme.RatingColor
import com.example.searchplacement.ui.theme.RedPoint
import com.example.searchplacement.ui.theme.UserPrimaryColor
import com.example.searchplacement.ui.theme.White
import com.example.searchplacement.ui.theme.isOpenColor
import com.example.searchplacement.ui.utils.sortDay
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreInfoSection(store: StoreResponse,navController: NavHostController) {
    val today = remember { LocalDate.now() }
    val todayDay = today.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREAN)
    val todayHours = store.businessHours[todayDay] ?: "영업시간 정보 없음"

    var showSheet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(Dimens.Default)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = store.storeName,
                style = AppTextStyle.BodyLarge.copy(fontSize = 24.sp),
                modifier = Modifier.weight(1f)
            )

            val isHoliday = store.regularHolidays?.get(todayDay) == 1
            val (_, statusText, badgeColor) = remember(todayHours,isHoliday) {
                val now = LocalTime.now()
                val parts = todayHours.split("-").map { it.trim() }

                if (isHoliday) {
                    Triple(false, "휴무", IconColor)
                } else if (parts.size == 2) {
                    try {
                        val openTime = LocalTime.parse(parts[0])
                        val closeTime = LocalTime.parse(parts[1])
                        if (now.isAfter(openTime) && now.isBefore(closeTime)) {
                            Triple(true, "영업중", isOpenColor)
                        } else {
                            Triple(false, "영업종료", IconColor)
                        }
                    } catch (e: Exception) {
                        Triple(false, "정보없음", IconColor)
                    }
                } else {
                    Triple(false, "정보없음",IconColor)
                }
            }

            Box(
                modifier = Modifier
                    .background(
                        color = badgeColor,
                        shape = RoundedCornerShape(Dimens.Small)
                    )
                    .padding(horizontal = Dimens.Default, vertical = 6.dp)
            ) {
                Text(
                    text = statusText,
                    style = AppTextStyle.mainPoint.copy(color = White, fontSize = 13.sp)
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens.Default),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens.Tiny),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = RatingColor,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = String.format("%.1f", store.averageRating),
                    style = AppTextStyle.Body.copy(fontWeight = FontWeight.Bold)
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens.Tiny),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = RedPoint,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = store.favoriteCount.toString(),
                    style = AppTextStyle.Body.copy(fontSize = 15.sp)
                )
            }
        }

        Row(
            modifier = Modifier
                .clickable { showSheet = true }
                .padding(vertical = Dimens.Tiny),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AccessTime,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = IconColor
            )
            Text(
                text = "$todayDay: $todayHours",
                fontSize = 14.sp,
                color = IconTextColor
            )
        }

        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSheet = false },
                containerColor = White,
                dragHandle = { BottomSheetDefaults.DragHandle() }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(Dimens.Default)
                ) {
                    Text(
                        text = "영업시간",
                        style = AppTextStyle.Body.copy(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )

                    store.businessHours.entries.sortedBy { sortDay(it.key) }.forEach { (day, hours) ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = day,
                                style = AppTextStyle.Body.copy(
                                    color = if (day == todayDay) UserPrimaryColor else IconColor,
                                    fontWeight = if (day == todayDay) FontWeight.Bold else FontWeight.Normal
                                )
                            )
                            Text(
                                text = hours,
                                style = AppTextStyle.Body.copy(
                                    color = if (hours == "휴무") Color.Red else IconTextColor
                                )
                            )
                        }
                    }
                }
            }
        }


        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = IconColor
            )
            Text(
                text = store.location,
                style = AppTextStyle.Body.copy(fontSize = 14.sp, color = IconTextColor),
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
                navController.currentBackStackEntry?.savedStateHandle?.set("store", store)
                navController.navigate("map_with_store")
            }
        ) {
            Icon(
                imageVector = Icons.Default.MyLocation,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = UserPrimaryColor
            )
            Text(
                text = "1.2km",
                style = AppTextStyle.Body.copy(fontSize = 14.sp, color = UserPrimaryColor),
            )
        }

        if (store.description.isNotEmpty()) {
            Text(
                text = store.description,
                style = AppTextStyle.Body.copy(fontSize = 14.sp, color = IconColor),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
