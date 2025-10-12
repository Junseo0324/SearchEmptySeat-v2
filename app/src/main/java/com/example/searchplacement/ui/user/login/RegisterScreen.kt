package com.example.searchplacement.ui.user.login

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.searchplacement.data.member.SignUpRequest
import com.example.searchplacement.di.AppModule
import com.example.searchplacement.ui.theme.AppButtonStyle
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Black
import com.example.searchplacement.ui.theme.ButtonMainColor
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.Gray
import com.example.searchplacement.ui.theme.White
import com.example.searchplacement.ui.utils.AddressWebViewDialog
import com.example.searchplacement.ui.utils.getImageFilePart
import com.example.searchplacement.viewmodel.LoginViewModel


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RegisterScreen(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
) {
//    var currentScreen by remember { mutableStateOf(0) }
//    val signUpData = remember { mutableStateOf(SignUpRequest()) }
//    val context = LocalContext.current
//    val imageUri = remember { mutableStateOf<Uri?>(null) }
//    val scrollState = rememberScrollState()

    val signUpData = remember { mutableStateOf(SignUpRequest()) }
    val context = LocalContext.current
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val showWebView = remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Column(
        Modifier
            .fillMaxSize()
            .padding(Dimens.Small)
            .verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(80.dp))
        Text(
            text = "빈자리를 부탁해",
            style = AppTextStyle.Title,
            color = Color.Black,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        UserTypeInput(signUpData)
        if (signUpData.value.userType.isNotBlank()) {
            EmailInput(signUpData)
        }
        if (signUpData.value.email.isNotBlank()) {
            PasswordInput(signUpData)
        }
        if (signUpData.value.password.isNotBlank()) {
            NameInput(signUpData)
        }
        if (signUpData.value.name.isNotBlank()) {
            PhoneInput(signUpData)
        }
        if (signUpData.value.phone.isNotBlank()) {
            LocationInput(signUpData, showWebView)
        }
        if (signUpData.value.location.isNotBlank()) {
            ImageInput(imageUri)
        }
        if (imageUri.value != null) {
            Button(
                onClick = {
                    val imageFilePart = getImageFilePart(context, imageUri.value!!)
                    loginViewModel.register(
                        email = signUpData.value.email,
                        password = signUpData.value.password,
                        name = signUpData.value.name,
                        phone = signUpData.value.phone,
                        location = signUpData.value.location,
                        userType = signUpData.value.userType,
                        imageFile = imageFilePart
                    )
                    navController.navigate("login")
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = AppButtonStyle.RoundedShape,
                colors = ButtonDefaults.buttonColors(containerColor = ButtonMainColor)
            ) {
                Text("회원가입", color = Color.White)
            }
        }
    }

//    AddressWebViewDialog(
//        showDialog = showWebView.value,
//        onDismiss = { showWebView.value = false },
//        onAddressSelected = { selected -> signUpData.value = signUpData.value.copy(location = selected) },
//        url = AppModule.BASE_URL
//    )

}

@Composable
fun UserTypeScreen(signUpData: MutableState<SignUpRequest>, onNext: () -> Unit) {
    var userType by remember { mutableStateOf(signUpData.value.userType) }
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            "형태", style = AppTextStyle.Section, modifier = Modifier.padding(Dimens.Small),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.Medium),
            horizontalArrangement = Arrangement.Center
        ) {
            SegmentedButton("사용자", userType == "USER") { userType = "USER" }
            SegmentedButton("점주", userType == "OWNER") { userType = "OWNER" }
        }

        Button(
            onClick = {
                signUpData.value = signUpData.value.copy(userType = userType)
                onNext()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = AppButtonStyle.RoundedShape,
            colors = ButtonColors(
                containerColor = ButtonMainColor, contentColor = Black,
                disabledContainerColor = Color.DarkGray, disabledContentColor = Black
            )
        ) {
            Text("다음", color = White)
        }
    }
}


@Composable
fun EmailScreen(signUpData: MutableState<SignUpRequest>, onNext: () -> Unit) {
    var email by remember { mutableStateOf(signUpData.value.email) }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("이메일") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black,
                focusedLabelColor = Color.Black,
                cursorColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
        )

        Button(
            onClick = {
                signUpData.value = signUpData.value.copy(email = email)
                onNext()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = AppButtonStyle.RoundedShape,
            colors = ButtonColors(
                containerColor = ButtonMainColor, contentColor = Color.Black,
                disabledContainerColor = Color.DarkGray, disabledContentColor = Color.Black
            )
        ) {
            Text("다음", color = Color.White)
        }
    }
}

@Composable
fun PasswordScreen(signUpData: MutableState<SignUpRequest>, onNext: () -> Unit) {
    var password by remember { mutableStateOf(signUpData.value.password) }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("비밀번호") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black,
                focusedLabelColor = Color.Black,
                cursorColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
        )

        Button(
            onClick = {
                signUpData.value = signUpData.value.copy(password = password)
                onNext()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = AppButtonStyle.RoundedShape,
            colors = ButtonColors(
                containerColor = ButtonMainColor, contentColor = Color.Black,
                disabledContainerColor = Color.DarkGray, disabledContentColor = Color.Black
            )
        ) {
            Text("다음", color = Color.White)
        }

    }
}

@Composable
fun NameScreen(signUpData: MutableState<SignUpRequest>, onNext: () -> Unit) {
    var textState by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(signUpData.value.name))
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        OutlinedTextField(
            value = textState,
            onValueChange = { textState = it },
            label = { Text("이름") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black,
                focusedLabelColor = Color.Black,
                cursorColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
        )

        Button(
            onClick = {
                signUpData.value = signUpData.value.copy(name = textState.text)
                onNext()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = AppButtonStyle.RoundedShape,
            colors = ButtonColors(
                containerColor = ButtonMainColor, contentColor = Color.Black,
                disabledContainerColor = Color.DarkGray, disabledContentColor = Color.Black
            )
        ) {
            Text("다음", color = Color.White)
        }

    }
}

@Composable
fun PhoneScreen(signUpData: MutableState<SignUpRequest>, onNext: () -> Unit) {
    var phone by remember { mutableStateOf(signUpData.value.phone) }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("핸드폰 번호") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black,
                focusedLabelColor = Color.Black,
                cursorColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
        )

        Button(
            onClick = {
                signUpData.value = signUpData.value.copy(phone = phone)
                onNext()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = AppButtonStyle.RoundedShape,
            colors = ButtonColors(
                containerColor = ButtonMainColor, contentColor = Color.Black,
                disabledContainerColor = Color.DarkGray, disabledContentColor = Color.Black
            )
        ) {
            Text("다음", color = Color.White)
        }

    }
}

@Composable
fun LocationScreen(signUpData: MutableState<SignUpRequest>, onNext: () -> Unit) {
    var selectedAddress by remember { mutableStateOf("") }
    var showWebView by remember { mutableStateOf(false) }
    var context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = {
                showWebView = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = AppButtonStyle.RoundedShape,
            colors = ButtonDefaults.buttonColors(containerColor = ButtonMainColor)
        ) {
            Text("주소 찾기", color = White)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("선택된 주소: $selectedAddress", style = AppTextStyle.BodyLarge)

        // 다음 버튼
        Button(
            onClick = {
                if (selectedAddress.isNotBlank()) {
                    signUpData.value = signUpData.value.copy(location = selectedAddress)
                    onNext()
                } else {
                    Toast.makeText(context, "주소를 입력해주세요", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = AppButtonStyle.RoundedShape,
            colors = ButtonDefaults.buttonColors(containerColor = ButtonMainColor)
        ) {
            Text("다음", color = Color.White)
        }
    }

    AddressWebViewDialog(
        showDialog = showWebView,
        onDismiss = { showWebView = false },
        onAddressSelected = { address ->
            selectedAddress = address
        },
        url = AppModule.BASE_URL
    )

}


@Composable
fun ImageScreen(imageUri: MutableState<Uri?>, onComplete: () -> Unit) {
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                imageUri.value = uri
            }
        }

    var context = LocalContext.current


    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 이미지 미리보기
        imageUri.value?.let { uri ->
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = "Selected Image",
                modifier = Modifier.height(200.dp)
            )
        }

        // 이미지 선택 버튼
        Button(
            onClick = {
                launcher.launch("image/*")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = AppButtonStyle.RoundedShape,
            colors = ButtonColors(
                containerColor = ButtonMainColor, contentColor = Color.Black,
                disabledContainerColor = Color.DarkGray, disabledContentColor = Color.Black
            )
        ) {
            Text("이미지 선택", color = Color.White)
        }

        // 완료 버튼
        Button(
            onClick = {
                if (imageUri != null) {
                    onComplete()
                } else {
                    Toast.makeText(context, "프로필 이미지를 설정해주세요.", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = AppButtonStyle.RoundedShape,
            colors = ButtonColors(
                containerColor = ButtonMainColor, contentColor = Black,
                disabledContainerColor = Gray, disabledContentColor = Black
            )
        ) {
            Text("완료", color = White)
        }
    }
}


@Composable
fun UserTypeInput(signUpData: MutableState<SignUpRequest>) {
    var userType by remember { mutableStateOf(signUpData.value.userType.ifBlank { "USER" }) }
    LaunchedEffect(userType) {
        signUpData.value = signUpData.value.copy(userType = userType)
    }
    Row(
        modifier = Modifier.fillMaxWidth().padding(Dimens.Small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("형태", style = AppTextStyle.BodyLarge, modifier = Modifier.padding(Dimens.Small))
        Row(
            modifier = Modifier.fillMaxWidth().padding(Dimens.Medium).weight(1f),
            horizontalArrangement = Arrangement.Center
        ) {
            SegmentedButton("사용자", userType == "USER") { userType = "USER" }
            SegmentedButton("점주", userType == "OWNER") { userType = "OWNER" }
        }
    }
}

@Composable
fun EmailInput(signUpData: MutableState<SignUpRequest>) {
    var email by remember { mutableStateOf(signUpData.value.email) }
    Row(
        modifier = Modifier.fillMaxWidth().padding(Dimens.Small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("이메일", style = AppTextStyle.BodyLarge, modifier = Modifier.weight(0.25f).padding(Dimens.Small),textAlign = TextAlign.Start)
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                signUpData.value = signUpData.value.copy(email = it)
            },
            label = { Text("이메일") },
            modifier = Modifier.fillMaxWidth().weight(0.75f),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black,
                focusedLabelColor = Color.Black,
                cursorColor = Color.Black
            )
        )
    }
}

@Composable
fun PasswordInput(signUpData: MutableState<SignUpRequest>) {
    var password by remember { mutableStateOf(signUpData.value.password) }
    Row(
        modifier = Modifier.fillMaxWidth().padding(Dimens.Small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("패스워드", style = AppTextStyle.BodyLarge, modifier = Modifier.weight(0.25f).padding(Dimens.Small),textAlign = TextAlign.Start)
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                signUpData.value = signUpData.value.copy(password = it)
            },
            label = { Text("비밀번호") },
            modifier = Modifier.fillMaxWidth().weight(0.75f),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black,
                focusedLabelColor = Color.Black,
                cursorColor = Color.Black
            )
        )
    }
}

@Composable
fun NameInput(signUpData: MutableState<SignUpRequest>) {
    var name by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(signUpData.value.name))
    }
    Row(
        modifier = Modifier.fillMaxWidth().padding(Dimens.Small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("이름", style = AppTextStyle.BodyLarge, modifier = Modifier.weight(0.25f).padding(Dimens.Small),textAlign = TextAlign.Start)
        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                signUpData.value = signUpData.value.copy(name = it.text)
            },
            label = { Text("이름") },
            modifier = Modifier.fillMaxWidth().weight(0.75f),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black,
                focusedLabelColor = Color.Black,
                cursorColor = Color.Black
            )
        )
    }
}

@Composable
fun PhoneInput(signUpData: MutableState<SignUpRequest>) {
    var phone by remember { mutableStateOf(signUpData.value.phone) }
    Row(
        modifier = Modifier.fillMaxWidth().padding(Dimens.Small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "핸드폰",
            style = AppTextStyle.BodyLarge,
            modifier = Modifier.weight(0.25f).padding(Dimens.Small),
            textAlign = TextAlign.Start
        )
        OutlinedTextField(
            value = phone,
            onValueChange = {
                phone = it
                signUpData.value = signUpData.value.copy(phone = it)
            },
            label = { Text("핸드폰 번호") },
            modifier = Modifier.fillMaxWidth().weight(0.75f),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black,
                focusedLabelColor = Color.Black,
                cursorColor = Color.Black
            )
        )
    }
}

@Composable
fun LocationInput(signUpData: MutableState<SignUpRequest>, showWebView: MutableState<Boolean>) {
    var addressMain by remember { mutableStateOf(signUpData.value.location) }
    var addressDetail by remember { mutableStateOf("") }

    LaunchedEffect(addressMain, addressDetail) {
        if (addressMain.isNotBlank()) {
            signUpData.value = signUpData.value.copy(location = "$addressMain $addressDetail")
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(Dimens.Small)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "주소",
                style = AppTextStyle.BodyLarge,
                modifier = Modifier.weight(0.25f).padding(Dimens.Small),
                textAlign = TextAlign.Start
            )
            Button(
                onClick = { showWebView.value = true },
                modifier = Modifier.fillMaxWidth().weight(0.75f),
                shape = AppButtonStyle.RoundedShape,
                colors = ButtonDefaults.buttonColors(containerColor = ButtonMainColor)
            ) {
                Text("주소 찾기", color = White)
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "상세 주소",
                style = AppTextStyle.BodyLarge,
                modifier = Modifier.weight(0.25f).padding(Dimens.Small),
                textAlign = TextAlign.Start
            )
            OutlinedTextField(
                value = addressDetail,
                onValueChange = {
                    addressDetail = it
                    signUpData.value =
                        signUpData.value.copy(location = "$addressMain $addressDetail")
                },
                label = { Text("상세 주소 입력") },
                modifier = Modifier.fillMaxWidth().weight(0.75f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black,
                    focusedLabelColor = Color.Black,
                    cursorColor = Color.Black
                )
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "전체 주소 : $addressMain  $addressDetail",
            style = AppTextStyle.Body.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Start,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )


        AddressWebViewDialog(
            showDialog = showWebView.value,
            onDismiss = { showWebView.value = false },
            onAddressSelected = { selected ->
                addressMain = selected
            },
            url = AppModule.BASE_URL
        )
    }
}

@Composable
fun ImageInput(imageUri: MutableState<Uri?>) {
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) imageUri.value = uri
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(Dimens.Small),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "이미지",
                style = AppTextStyle.BodyLarge,
                modifier = Modifier.weight(0.25f).padding(Dimens.Small),
                textAlign = TextAlign.Start
            )
            Box(
                modifier = Modifier
                    .weight(0.75f)
                    .aspectRatio(1f)
                    .padding(end = 12.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                if (imageUri.value != null) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUri.value),
                        contentDescription = "선택된 이미지",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(MaterialTheme.shapes.medium)
                    )

                    IconButton(
                        onClick = { imageUri.value = null },
                        modifier = Modifier
                            .size(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "이미지 제거",
                            tint = Color.Black,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Gray.copy(alpha = 0.2f), shape = MaterialTheme.shapes.medium)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { launcher.launch("image/*") },
            modifier = Modifier
                .fillMaxWidth(),
            shape = AppButtonStyle.RoundedShape,
            colors = ButtonDefaults.buttonColors(containerColor = ButtonMainColor)
        ) {
            Text(
                if (imageUri.value == null) "이미지 선택" else "다시 선택",
                color = White
            )
        }
    }

}

