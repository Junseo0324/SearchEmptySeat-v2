package com.example.searchplacement.presentation.owner.menu_manage

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.searchplacement.presentation.theme.AppTextStyle
import com.example.searchplacement.presentation.theme.Black
import com.example.searchplacement.presentation.theme.IconColor
import com.example.searchplacement.presentation.theme.UserPrimaryColor
import com.example.searchplacement.presentation.theme.White


@Composable
fun MultiToggleSwitch(
    labels: List<String>,
    selectedIndex: Int,
    onToggle: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val switchWidth = 50.dp
    val switchHeight = 36.dp
    val totalWidth = switchWidth * labels.size

    val animatedOffset by animateDpAsState(
        targetValue = switchWidth * selectedIndex,
        label = "ToggleSlide"
    )

    Box(
        modifier = modifier
            .width(totalWidth)
            .height(switchHeight)
            .background(IconColor)
    ) {

        Box(
            modifier = Modifier
                .offset(x = animatedOffset)
                .width(switchWidth)
                .fillMaxHeight()
                .background(UserPrimaryColor)
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
        ) {
            labels.forEachIndexed { index, label ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable { onToggle(index) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        style = AppTextStyle.Body.copy(
                            fontSize = 13.sp, color = if (selectedIndex == index) White else Black,
                        )
                    )
                }
            }
        }
    }
}