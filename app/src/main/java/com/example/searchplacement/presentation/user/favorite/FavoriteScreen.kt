package com.example.searchplacement.presentation.user.favorite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.searchplacement.presentation.theme.AppTextStyle
import com.example.searchplacement.presentation.theme.Dimens
import com.example.searchplacement.presentation.theme.IconTextColor
import com.example.searchplacement.presentation.theme.RedPoint

@Composable
fun FavoriteScreen(
    state: FavoriteState = FavoriteState(),
    onAction: (FavoriteAction) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = Dimens.Medium),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.Small)
        ) {
            Icon(
                imageVector = Icons.Outlined.FavoriteBorder,
                contentDescription = null,
                tint = RedPoint,
                modifier = Modifier.size(Dimens.Large)
            )
            Text(
                text = "찜 목록",
                style = AppTextStyle.Body.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = IconTextColor
                )
            )
        }
        Column(
            Modifier
                .fillMaxSize()
                .padding(Dimens.Small)
        ) {
            LazyColumn {
                state.favorites?.let { favoriteList ->
                    items(favoriteList) { favorite ->
                        FavoriteList(
                            store = favorite.store,
                            onStoreClick = {
                                onAction(FavoriteAction.OnStoreClick(favorite.store.storePK))
                            },
                            onRemoveFavorite = {
                                onAction(FavoriteAction.OnFavoriteToggle(favorite.store.storePK))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoriteScreenPreview() {
    FavoriteScreen()
}