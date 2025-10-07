package com.example.searchplacement.ui.user.store.table

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TableBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.IconColor
import com.example.searchplacement.ui.theme.ViewCountColor

@Composable
fun EmptyTableSection() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.TableBar,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = ViewCountColor
        )
        Spacer(modifier = Modifier.height(Dimens.Medium))
        Text(
            text = "아직 등록된 좌석이 없습니다.",
            style = AppTextStyle.Body.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(Dimens.Small))
        Text(
            text = "가게에 문의해주세요.",
            style = AppTextStyle.Body.copy(fontSize = 14.sp, color = IconColor)
        )
    }
}