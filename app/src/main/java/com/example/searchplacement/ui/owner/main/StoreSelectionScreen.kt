package com.example.searchplacement.ui.owner.main

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.searchplacement.di.AppModule
import com.example.searchplacement.ui.utils.rememberImageLoaderWithToken
import com.example.searchplacement.viewmodel.StoreListViewModel


@Composable
fun StoreSelectScreen(
    viewModel: StoreListViewModel,
    onStoreSelected: () -> Unit
) {
    val stores = viewModel.myStores.collectAsState().value
    val selectedStore = viewModel.selectedStore.collectAsState().value
    val imageLoader = rememberImageLoaderWithToken()


    LaunchedEffect(Unit) {
        viewModel.fetchMyStores()
    }

    Column(Modifier.padding(16.dp).fillMaxSize()) {
        Text("내 가게 선택", fontWeight = FontWeight.Bold, fontSize = 24.sp, modifier = Modifier.padding(bottom = 12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            state = rememberLazyGridState(),
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(stores) { store ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(4.dp)
                        .clickable {
                            viewModel.selectStore(store)
                            onStoreSelected()
                        }
                        .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp)),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
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
                            fontSize = 16.sp,
                            fontWeight = if (selectedStore?.storePK == store.storePK) FontWeight.Bold else FontWeight.SemiBold,
                            modifier = Modifier.padding(4.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = store.location,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(horizontal = 4.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}
