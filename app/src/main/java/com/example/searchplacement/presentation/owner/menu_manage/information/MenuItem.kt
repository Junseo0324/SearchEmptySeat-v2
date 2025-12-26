package com.example.searchplacement.presentation.owner.menu_manage.information

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import com.example.searchplacement.data.menu.MenuResponse
import com.example.searchplacement.core.di.AppModule
import com.example.searchplacement.presentation.theme.Black
import com.example.searchplacement.presentation.theme.Dimens
import com.example.searchplacement.presentation.theme.IconColor
import com.example.searchplacement.presentation.theme.RedPoint
import com.example.searchplacement.presentation.theme.White


@Composable
fun MenuItem(
    menu: MenuResponse,
    imageLoader: ImageLoader,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.Small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val imageUrl = menu.image?.firstOrNull()
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
                Text("No Img", fontSize = 12.sp, color = Black)
            }
        }
        Column(Modifier.weight(1f)) {
            Text(menu.name, fontWeight = FontWeight.SemiBold)
            Text("${menu.price}원", color = IconColor, fontSize = 15.sp)
            Text(menu.description, fontSize = 13.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
        }
        IconButton(onClick = onEditClick) {
            Icon(Icons.Outlined.Edit, contentDescription = "수정")
        }
        IconButton(onClick = onDeleteClick) {
            Icon(Icons.Outlined.Delete, tint = RedPoint, contentDescription = "삭제")
        }
    }
}