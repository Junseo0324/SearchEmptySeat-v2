package com.example.searchplacement.presentation.owner.menu_manage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.searchplacement.data.menu.MenuResponse
import com.example.searchplacement.core.di.AppModule
import com.example.searchplacement.presentation.theme.Dimens
import com.example.searchplacement.presentation.theme.IconColor
import com.example.searchplacement.presentation.theme.White
import com.example.searchplacement.presentation.utils.rememberImageLoaderWithToken

@Composable
fun MenuStockItem(
    menu: MenuResponse,
    isAvailable: Boolean,
    onToggle: (Boolean) -> Unit
) {
    val imageLoader = rememberImageLoaderWithToken()
    val imageUrl = menu.image?.firstOrNull()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {

        if (!imageUrl.isNullOrEmpty()) {
            AsyncImage(
                model = "${AppModule.BASE_URL}/api/files/$imageUrl",
                imageLoader = imageLoader,
                contentDescription = menu.name,
                modifier = Modifier
                    .size(100.dp)
            )
        } else {
            Box(
                Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(Dimens.Default))
                    .background(White),
                contentAlignment = Alignment.Center
            ) {
                Text("No Img", fontSize = 12.sp, color = IconColor)
            }
        }

        Column(Modifier
            .weight(1f)
            .padding(10.dp)) {
            Text(menu.name, fontWeight = FontWeight.SemiBold)
            Text("${menu.price}원", color = IconColor, fontSize = 15.sp)
            Text(menu.description, fontSize = 13.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
        }

        MultiToggleSwitch(
            labels = listOf("판매", "품절"),
            selectedIndex = if (isAvailable) 0 else 1,
            onToggle = { index -> onToggle(index == 0) },
            modifier = Modifier.width(100.dp)
        )

    }
}