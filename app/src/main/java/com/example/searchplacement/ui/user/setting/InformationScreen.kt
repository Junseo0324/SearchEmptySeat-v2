package com.example.searchplacement.ui.user.setting

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.searchplacement.di.AppModule
import com.example.searchplacement.navigation.MainBottomNavItem
import com.example.searchplacement.ui.theme.AppButtonStyle
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Black
import com.example.searchplacement.ui.theme.ButtonMainColor
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.White
import com.example.searchplacement.ui.utils.AddressWebViewDialog
import com.example.searchplacement.viewmodel.MainViewModel
import okhttp3.OkHttpClient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformationScreen(navController: NavHostController,mainViewModel: MainViewModel) {
    val user by mainViewModel.user.collectAsState()
    val IMAGE_URL = "${AppModule.BASE_URL}/api/files/"
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val nameState = remember { mutableStateOf(user?.name ?: "") }
    val locationState = remember { mutableStateOf(user?.location ?: "") }
    val selectedImageUri = remember { mutableStateOf<Uri?>(null) }
    val showWebViewDialog = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("내 정보 변경",style = AppTextStyle.Section)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "뒤로 가기"
                        )
                    }
                },
            )
        },

    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
        ) {

            SelectableProfileImage(
                imageUrl = IMAGE_URL + (user?.image ?: ""),
                token = user?.token ?: "",
                onImageSelected = { uri ->
                    selectedImageUri.value = uri
                },
                context = LocalContext.current
            )


            Text(
                text = "이메일",
                modifier = Modifier.padding(bottom = Dimens.Tiny, start = Dimens.Small),
                style = AppTextStyle.Body.copy(color = Color(0xff676767))
            )

            OutlinedTextField(
                value = user?.email ?: "",
                onValueChange = { },
                enabled = false,
                readOnly = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Red,
                    unfocusedBorderColor = Color.Black,
                    focusedLabelColor = Color.Red,
                    cursorColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.Small)
            )

            Text(
                text = "이름",
                modifier = Modifier.padding(bottom = Dimens.Tiny, start = Dimens.Small),
                style = AppTextStyle.Body.copy(color = Color(0xff676767))
            )

            OutlinedTextField(
                value = nameState.value,
                onValueChange = { nameState.value = it },
                placeholder = { Text("이름") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black,
                    focusedLabelColor = Color.Black,
                    cursorColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.Small)
            )

            Row(
                Modifier.fillMaxWidth().padding(vertical = Dimens.Small),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "선호 지역",
                    modifier = Modifier.padding(bottom = Dimens.Tiny, start = Dimens.Small),
                    style = AppTextStyle.Body.copy(color = Color(0xff676767))
                )

                Button(
                    onClick = {
                        showWebViewDialog.value = true
                    },
                    modifier = Modifier.padding(Dimens.Small).size(120.dp, 40.dp),
                    shape = AppButtonStyle.RoundedShape,
                    colors = ButtonDefaults.buttonColors(containerColor = ButtonMainColor)
                ) {
                    Text("주소 찾기", style = AppTextStyle.Button.copy(color = White))
                }

            }


            OutlinedTextField(
                value = locationState.value,
                onValueChange = {},
                placeholder = { Text("주소 찾기를 통해 주소를 설정해주세요.") },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.Small),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Black,
                    unfocusedBorderColor = Black,
                    focusedLabelColor = Black,
                    cursorColor = Black
                )
            )

            Text(
                text = "연락처",
                modifier = Modifier.padding(bottom = Dimens.Tiny, start = Dimens.Small),
                style = AppTextStyle.Body.copy(color = Color(0xff676767))
            )

            OutlinedTextField(
                value = user?.phone ?: "",
                onValueChange = { },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.Small),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Black,
                    unfocusedBorderColor = Black,
                    focusedLabelColor = Black,
                    cursorColor = Black
                )
            )

            Spacer(modifier = Modifier.height(Dimens.Small))

            Button(
                onClick = {
                    val imagePart = selectedImageUri.value?.let { uri ->
                        mainViewModel.getImageFilePart(context, uri)
                    }
                    mainViewModel.updateUserInfo(
                        userId = user?.userId?.toLong() ?: 0L,
                        editedEmail = user?.email,
                        editedName = nameState.value,
                        editedPassword = null,
                        editedLocation = locationState.value,
                        imageFile = imagePart
                    )
                    navController.navigate(MainBottomNavItem.Setting.screenRoute)
                },
                modifier = Modifier
                    .fillMaxWidth().padding(Dimens.Small),
                shape = AppButtonStyle.RoundedShape,
                colors = ButtonColors(
                    containerColor = ButtonMainColor, contentColor = White,
                    disabledContainerColor = Color.DarkGray, disabledContentColor = Black
                )
            ) {
                Text("저장", style = AppTextStyle.Button.copy(color = White))
            }

            AddressWebViewDialog(
                showDialog = showWebViewDialog.value,
                onDismiss = { showWebViewDialog.value = false },
                onAddressSelected = { selectedAddress ->
                    locationState.value = selectedAddress
                    showWebViewDialog.value = false
                },
                AppModule.BASE_URL
            )


        }
    }
}

@Composable
fun SelectableProfileImage(
    imageUrl: String,
    token: String,
    onImageSelected: (Uri) -> Unit,
    context: Context
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let { onImageSelected(it) }
        }
    )

    val imageLoader = ImageLoader.Builder(context)
        .okHttpClient(
            OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                    chain.proceed(newRequest)
                }.build()
        )
        .build()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.Large),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            imageLoader = imageLoader,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .clickable {
                    launcher.launch("image/*")
                }
        )
    }
}

