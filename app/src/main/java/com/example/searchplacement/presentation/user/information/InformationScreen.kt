package com.example.searchplacement.presentation.user.information

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.searchplacement.core.di.AppModule
import com.example.searchplacement.domain.model.User
import com.example.searchplacement.presentation.theme.AppButtonStyle
import com.example.searchplacement.presentation.theme.AppTextStyle
import com.example.searchplacement.presentation.theme.Black
import com.example.searchplacement.presentation.theme.ButtonMainColor
import com.example.searchplacement.presentation.theme.Dimens
import com.example.searchplacement.presentation.theme.White
import okhttp3.OkHttpClient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformationScreen(
    state: InformationState,
    onAction: (InformationAction) -> Unit,
    onNavigateBack: () -> Unit
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("내 정보 변경",style = AppTextStyle.Section)
                },
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack() }) {
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
                .verticalScroll(rememberScrollState())
        ) {

            SelectableProfileImage(
                imageUrl = if (state.selectedImageUri != null) state.selectedImageUri.toString() else "${AppModule.BASE_URL}/api/files/" + (state.user?.image ?: ""),
                token = state.user?.token ?: "",
                onImageSelected = { uri ->
                    onAction(InformationAction.OnImageSelected(uri))
                },
                context = LocalContext.current,
                isUri = state.selectedImageUri != null
            )

            Text(
                text = "이메일",
                modifier = Modifier.padding(bottom = Dimens.Tiny, start = Dimens.Small),
                style = AppTextStyle.Body.copy(color = Color(0xff676767))
            )

            OutlinedTextField(
                value = state.user?.email ?: "",
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
                value = state.editedName,
                onValueChange = { onAction(InformationAction.OnNameChange(it)) },
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
                        onAction(InformationAction.OpenAddressDialog)
                    },
                    modifier = Modifier.padding(Dimens.Small).size(120.dp, 40.dp),
                    shape = AppButtonStyle.RoundedShape,
                    colors = ButtonDefaults.buttonColors(containerColor = ButtonMainColor)
                ) {
                    Text("주소 찾기", style = AppTextStyle.Button.copy(color = White))
                }
            }

            OutlinedTextField(
                value = state.editedLocation,
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
                value = state.user?.phone ?: "",
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
                    val user = state.user
                    if (user != null) {
                        onAction(
                            InformationAction.UpdateUserInfo(
                                user = User(
                                    userId = user.userId,
                                    name = state.editedName,
                                    email = user.email,
                                    phone = user.phone,
                                    location = state.editedLocation,
                                    image = user.image,
                                    token = user.token
                                ),
                                imageUri = state.selectedImageUri
                            )
                        )
                    }
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
        }
    }
}

@Composable
fun SelectableProfileImage(
    imageUrl: String,
    token: String,
    onImageSelected: (Uri) -> Unit,
    context: Context,
    isUri: Boolean = false
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let { onImageSelected(it) }
        }
    )

    val imageLoader = if (isUri) {
        ImageLoader(context)
    } else {
        ImageLoader.Builder(context)
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
    }

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
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .clickable {
                    launcher.launch("image/*")
                }
        )
    }
}

