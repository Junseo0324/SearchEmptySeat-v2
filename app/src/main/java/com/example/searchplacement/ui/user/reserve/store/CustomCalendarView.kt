package com.example.searchplacement.ui.user.reserve.store

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.searchplacement.ui.theme.Black
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.Gray
import com.example.searchplacement.ui.theme.UserPrimaryColor
import com.example.searchplacement.ui.utils.getDatesForMonth
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun CustomCalendarView(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentYearMonth by remember { mutableStateOf(YearMonth.now()) }

    val daysOfWeek = listOf("일", "월", "화", "수", "목", "금", "토")
    val dates = remember(currentYearMonth) {
        getDatesForMonth(currentYearMonth.year, currentYearMonth.monthValue)
    }

    Column(modifier = modifier.padding(Dimens.Medium)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {
                currentYearMonth = currentYearMonth.minusMonths(1)
            }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "이전 달")
            }

            Text(
                text = "${currentYearMonth.year}년 ${currentYearMonth.monthValue}월",
                style = MaterialTheme.typography.titleMedium
            )

            IconButton(onClick = {
                currentYearMonth = currentYearMonth.plusMonths(1)
            }) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "다음 달")
            }
        }

        Spacer(modifier = Modifier.height(Dimens.Small))

        Row(modifier = Modifier.fillMaxWidth()) {
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            userScrollEnabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            items(dates) { date ->
                val isCurrentMonth = date.monthValue == currentYearMonth.monthValue
                val isSelected = date == selectedDate

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(Dimens.Tiny)
                        .background(
                            when {
                                isSelected -> UserPrimaryColor
                                !isCurrentMonth -> Color.LightGray.copy(alpha = 0.2f)
                                else -> Color.Transparent
                            }
                        )
                        .clickable(enabled = isCurrentMonth) {
                            onDateSelected(date)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = date.dayOfMonth.toString(),
                        color = if (isCurrentMonth) Black else Gray
                    )
                }
            }
        }
    }
}