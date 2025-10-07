package com.example.searchplacement.ui.user.store.table

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.searchplacement.ui.theme.AppTextStyle

@Composable
fun TableStatusSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatusItem(color = Color(0xFF81C784), label = "예약 가능")
        StatusItem(color = Color(0xFFE57373), label = "예약 중")
        StatusItem(color = Color(0xFFB0BEC5), label = "이용 불가")
    }
}

@Composable
fun StatusItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(14.dp)
                .background(color, shape = RoundedCornerShape(3.dp))
        )
        Spacer(Modifier.width(6.dp))
        Text(label, style = AppTextStyle.Body.copy(color = Color.DarkGray, fontSize = 13.sp))
    }
}
