package com.example.searchplacement.ui.user.reserve.store

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.searchplacement.data.reserve.ReservationData
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.ChipBorderColor
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.IconColor
import com.example.searchplacement.ui.theme.IconTextColor
import com.example.searchplacement.ui.theme.StoreTabBackgroundColor
import com.example.searchplacement.ui.theme.White
import com.example.searchplacement.ui.theme.reservationCountColor

@Composable
fun PeopleCountStep(
    reservationData: ReservationData,
    onChangePeople: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(StoreTabBackgroundColor)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.Small)
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = reservationCountColor,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "방문 인원을 선택해주세요",
                style = AppTextStyle.Body.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = IconTextColor
                )
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(Dimens.Medium),
            colors = CardDefaults.cardColors(containerColor = White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.Large),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens.Large)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimens.Large),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            if (reservationData.numberOfPeople > 1) onChangePeople(reservationData.numberOfPeople - 1)
                        },
                        modifier = Modifier
                            .size(56.dp)
                            .border(Dimens.Nano, ChipBorderColor, CircleShape)
                    ) {
                        Icon(Icons.Default.Remove, null, modifier = Modifier.size(Dimens.Large))
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = reservationData.numberOfPeople.toString(),
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = reservationCountColor
                        )
                        Text(
                            text = "명",
                            style = AppTextStyle.Body.copy(color = IconColor)
                        )
                    }

                    IconButton(
                        onClick = {
                            if (reservationData.numberOfPeople < 8)
                                onChangePeople(reservationData.numberOfPeople + 1)
                        },
                        modifier = Modifier
                            .size(56.dp)
                            .border(Dimens.Nano, ChipBorderColor, CircleShape)
                    ) {
                        Icon(Icons.Default.Add, null, modifier = Modifier.size(Dimens.Large))
                    }
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.Default),
                    verticalArrangement = Arrangement.spacedBy(Dimens.Default),
                    modifier = Modifier.height(200.dp)
                ) {
                    items(listOf(1, 2, 4, 8)) { count ->
                        Card(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clickable { onChangePeople(count) },
                            shape = RoundedCornerShape(Dimens.Default),
                            colors = CardDefaults.cardColors(
                                containerColor = if (reservationData.numberOfPeople == count)
                                    Color(0xFFE3F2FD)
                                else
                                    White
                            ),
                            border = if (reservationData.numberOfPeople == count)
                                BorderStroke(Dimens.Nano, reservationCountColor)
                            else
                                BorderStroke(1.dp, ChipBorderColor)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "${count}명",
                                    fontSize = 18.sp,
                                    fontWeight = if (reservationData.numberOfPeople == count)
                                        FontWeight.Bold else FontWeight.Normal,
                                    color = if (reservationData.numberOfPeople == count)
                                        reservationCountColor else IconTextColor
                                )
                                Text(
                                    text = "${count}인석",
                                    style = AppTextStyle.Body.copy(
                                        fontSize = 12.sp,
                                        color = IconColor
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}