package com.example.searchplacement.ui.owner.main

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.searchplacement.di.AppModule
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Black
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.White
import com.example.searchplacement.ui.utils.rememberImageLoaderWithToken
import com.example.searchplacement.viewmodel.StoreListViewModel


@Composable
fun StoreSelectScreen(
    onStoreSelected: () -> Unit
) {
    val viewModel: StoreListViewModel = hiltViewModel()
    val stores = viewModel.myStores.collectAsState().value
    val selectedStore = viewModel.selectedStore.collectAsState().value
    val imageLoader = rememberImageLoaderWithToken()


    LaunchedEffect(Unit) {
        viewModel.fetchMyStores()
        Log.d("StroeSelectionScreen", "StoreSelectScreen: $stores")
    }

    Column(Modifier
        .padding(Dimens.Medium)
        .fillMaxSize()) {
        Text("내 가게 선택", fontWeight = FontWeight.Bold, fontSize = 24.sp, modifier = Modifier.padding(bottom = Dimens.Default))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            state = rememberLazyGridState(),
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(Dimens.Tiny),
            verticalArrangement = Arrangement.spacedBy(Dimens.Small),
            horizontalArrangement = Arrangement.spacedBy(Dimens.Small)
        ) {
            items(stores) { store ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(Dimens.Tiny)
                        .clickable {
                            viewModel.selectStore(store)
                            onStoreSelected()
                        }
                        .border(1.dp, Black, shape = RoundedCornerShape(Dimens.Default)),
                    colors = CardDefaults.cardColors(
                        containerColor = White
                    )
                ) {
                    Column(horizontalAlignment = Alignment.Start) {
                        AsyncImage(
                            model = "${AppModule.BASE_URL}/api/files/${store.image[0]}",
                            imageLoader = imageLoader,
                            contentDescription = "썸네일",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = store.storeName,
                            style = AppTextStyle.Body.copy(
                            fontWeight = if (selectedStore?.storePK == store.storePK) FontWeight.Bold else FontWeight.SemiBold),
                            modifier = Modifier.padding(Dimens.Tiny),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = store.location,
                            style = AppTextStyle.Body.copy(fontSize = 12.sp, fontWeight = FontWeight.Normal),
                            modifier = Modifier.padding(horizontal = Dimens.Tiny),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}
