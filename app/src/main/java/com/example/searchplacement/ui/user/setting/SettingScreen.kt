package com.example.searchplacement.ui.user.setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.searchplacement.R
import com.example.searchplacement.di.AppModule
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Black
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.White
import com.example.searchplacement.ui.utils.rememberImageLoaderWithToken
import com.example.searchplacement.viewmodel.MainViewModel

@Composable
fun SettingScreen(navController: NavHostController, mainViewModel: MainViewModel) {
    val user by mainViewModel.user.collectAsState()
    val IMAGE_URL = "${AppModule.BASE_URL}/api/files/"
    val imageLoader = rememberImageLoaderWithToken()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.Small)
    ) {
        Text(text = "개인정보 변경",
            style = AppTextStyle.Section,
            modifier = Modifier.fillMaxWidth().padding(vertical = Dimens.Small)
        )
        Card(
            Modifier
                .padding(Dimens.Small)
                .fillMaxWidth(),
            colors = CardColors(
                contentColor = Black, containerColor = White,
                disabledContentColor = Black, disabledContainerColor = White
            ),
            elevation = CardDefaults.elevatedCardElevation(1.dp)
        )
        {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.Small),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(IMAGE_URL + user?.image)
                        .crossfade(true)
                        .build(),
                    imageLoader = imageLoader,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                )

                Column(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(Dimens.Small)
                        .clickable {
                            navController.navigate("information")
                        }
                ) {
                    Row(
                        Modifier.padding(horizontal = Dimens.Tiny),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = user?.name ?: "닉네임",
                            style = AppTextStyle.BodyLarge,
                            modifier = Modifier.clickable {
                                navController.navigate("information")
                            })
                        Spacer(modifier = Modifier.width(Dimens.Small))
                        Image(
                            painter = painterResource(id = R.drawable.ic_right_arrow),
                            contentDescription = null,
                            modifier = Modifier.padding(Dimens.Tiny)
                        )

                    }
                }

            }



        }
        SettingCard(navController,"비밀번호 변경","checkPassword")
    }
}
