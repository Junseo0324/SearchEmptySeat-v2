package com.example.searchplacement.ui.owner.selection

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.searchplacement.data.dto.StoreSelectDTO
import com.example.searchplacement.di.AppModule
import com.example.searchplacement.navigation.OwnerBottomNavItem
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.CardBorderTransparentColor
import com.example.searchplacement.ui.theme.CardInnerColor
import com.example.searchplacement.ui.theme.ChipBorderColor
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.IconColor
import com.example.searchplacement.ui.theme.RatingColor
import com.example.searchplacement.ui.theme.White
import com.example.searchplacement.ui.theme.isOpenColor
import com.example.searchplacement.ui.theme.registerColor
import com.example.searchplacement.ui.utils.rememberImageLoaderWithToken
import com.example.searchplacement.viewmodel.StoreSelectViewModel

@Composable
fun StoreSelectScreen(
    navController: NavHostController
) {
    val viewModel: StoreSelectViewModel = hiltViewModel()
    val stores = viewModel.myStores.collectAsState().value
    val selectedStore = viewModel.selectedStore.collectAsState().value


    LaunchedEffect(Unit) {
        viewModel.fetchMyStores()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CardBorderTransparentColor)
    ) {
        HeaderSection()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.Medium),
            verticalArrangement = Arrangement.spacedBy(Dimens.Medium)
        ) {
            items(stores) { store ->
                StoreCard(
                    store = store,
                    onClick = {
                        navController.navigate(OwnerBottomNavItem.Home.screenRoute)
                    }
                )
            }

            item {
                AddStoreCard(
                    onClick = {
                        navController.navigate("storeRegister")
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(Dimens.Medium))
            }
        }
    }
}

@Composable
fun HeaderSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(Dimens.Medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Store,
            contentDescription = "매장",
            tint = registerColor,
            modifier = Modifier.size(32.dp)
        )

        Spacer(modifier = Modifier.width(Dimens.Default))

        Column {
            Text(
                text = "내 매장 관리",
                style = AppTextStyle.Body.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = "관리할 매장을 선택하세요",
                style = AppTextStyle.Body.copy(fontSize = 14.sp, color = IconColor)
            )
        }
    }
}

@Composable
fun StoreCard(
    store: StoreSelectDTO,
    onClick: () -> Unit
) {
    val imageLoader = rememberImageLoaderWithToken()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(Dimens.Default),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.Nano)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.Medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = "${AppModule.BASE_URL}/api/files/${store.image[0]}",
                imageLoader = imageLoader,
                contentDescription = "썸네일",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(Dimens.Default))
                    .background(CardInnerColor),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(Dimens.Medium))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = store.storeName,
                    style = AppTextStyle.Body.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(Dimens.Tiny))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = IconColor,
                        modifier = Modifier.size(Dimens.Default)
                    )
                    Spacer(modifier = Modifier.width(Dimens.Tiny))
                    Text(
                        text = store.location,
                        style = AppTextStyle.Body.copy(fontSize = 13.sp, color = IconColor)
                    )
                }

                Spacer(modifier = Modifier.height(Dimens.Tiny))

                Text(
                    text = store.category.first(),
                    style = AppTextStyle.Body.copy(
                        fontSize = 12.sp,
                        color = IconColor
                    )
                )
            }

            Spacer(modifier = Modifier.width(Dimens.Default))

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = RoundedCornerShape(Dimens.Medium),
                    color = if (store.isAvailable) isOpenColor else RatingColor
                ) {
                    Text(
                        text = if (store.isAvailable) "영업중" else "준비중",
                        modifier = Modifier.padding(horizontal = Dimens.Default, vertical = 6.dp),
                        style = AppTextStyle.Body.copy(fontSize = 12.sp, fontWeight = FontWeight.Bold, color = White)
                    )
                }

                Spacer(modifier = Modifier.height(Dimens.Default))

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "이동",
                    tint = IconColor,
                    modifier = Modifier.size(Dimens.Large)
                )
            }
        }
    }
}

@Composable
fun AddStoreCard(
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(Dimens.Default),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(
            width = Dimens.Nano,
            color = ChipBorderColor
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        color = CardBorderTransparentColor,
                        shape = RoundedCornerShape(30.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "매장 추가",
                    tint = IconColor,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(Dimens.Default))

            Text(
                text = "새 매장 등록하기",
                style = AppTextStyle.Body.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = IconColor
                )
            )
        }
    }
}