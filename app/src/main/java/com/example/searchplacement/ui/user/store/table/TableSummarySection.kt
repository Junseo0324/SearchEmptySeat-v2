package com.example.searchplacement.ui.user.store.table

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.White

@Composable
fun TableSummarySection(groupedSeats: Map<Int, Int>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White, RoundedCornerShape(Dimens.Default))
            .padding(Dimens.Medium),
        verticalArrangement = Arrangement.spacedBy(Dimens.Medium)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Dimens.Default)
        ) {
            groupedSeats.forEach { (type, count) ->
                TableTypeCard(modifier = Modifier.weight(1f),type, count)
            }
        }
    }
}

@Composable
fun TableTypeCard(modifier: Modifier,type: Int, count: Int) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(Dimens.Default),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.Nano)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.Medium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Dimens.Default)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = Color(0xFFF5F5F5),
                        shape = RoundedCornerShape(Dimens.Large)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    tint = Color(0xFF95A5A6),
                    modifier = Modifier.size(Dimens.Large)
                )
            }

            Text(
                text = "${type} 인석",
                style = AppTextStyle.Body.copy(fontWeight = FontWeight.Bold)
            )

            Text(
                text = "${count}석",
                style = AppTextStyle.Body.copy(fontSize = 14.sp)
            )
        }
    }
}


