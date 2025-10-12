package com.example.searchplacement.ui.user.login

import android.net.Uri
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.searchplacement.data.member.SignUpRequest
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Black
import com.example.searchplacement.ui.theme.CardBorderTransparentColor
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.IconColor
import com.example.searchplacement.ui.theme.White
import com.example.searchplacement.ui.theme.loginLogoColor
import com.example.searchplacement.ui.user.register.EmailInput
import com.example.searchplacement.ui.user.register.ImageInput
import com.example.searchplacement.ui.user.register.LocationInput
import com.example.searchplacement.ui.user.register.NameInput
import com.example.searchplacement.ui.user.register.PasswordConfirmInput
import com.example.searchplacement.ui.user.register.PasswordInput
import com.example.searchplacement.ui.user.register.PhoneInput
import com.example.searchplacement.ui.user.register.UserTypeInput
import com.example.searchplacement.ui.utils.getImageFilePart
import com.example.searchplacement.viewmodel.LoginViewModel


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RegisterScreen(
    navController: NavHostController
) {
    val loginViewModel: LoginViewModel = hiltViewModel()
    val signUpData = remember { mutableStateOf(SignUpRequest()) }
    val context = LocalContext.current
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val showWebView = remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CardBorderTransparentColor)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.Medium),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "뒤로가기",
                    modifier = Modifier
                        .size(Dimens.Large)
                        .clickable { navController.popBackStack() }
                )
                Text(
                    text = "뒤로가기",
                    style = AppTextStyle.Body,
                    modifier = Modifier.padding(start = Dimens.Small)
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.Medium),
                shape = RoundedCornerShape(Dimens.Medium),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(defaultElevation = Dimens.Nano)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimens.Large)
                ) {
                    Text(
                        text = "회원가입",
                        style = AppTextStyle.Title.copy(fontSize = 24.sp),
                        color = Black,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(Dimens.Small))

                    Text(
                        text = "빈자리를 부탁해와 함께 시작하세요",
                        style = AppTextStyle.Body.copy(fontSize = 14.sp ,color = IconColor),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    UserTypeInput(signUpData)

                    if (signUpData.value.userType.isNotBlank()) {
                        EmailInput(signUpData)
                    }
                    if (signUpData.value.email.isNotBlank()) {
                        PasswordInput(signUpData)
                    }
                    if (signUpData.value.password.isNotBlank()) {
                        PasswordConfirmInput(signUpData)
                    }

                    if (signUpData.value.password.isNotBlank() &&
                        signUpData.value.password == signUpData.value.passwordConfirm) {
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
                        Spacer(modifier = Modifier.height(Dimens.Large))

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
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(Dimens.Default),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent
                            )
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        color = loginLogoColor,
                                        shape = RoundedCornerShape(Dimens.Default)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "사용자로 가입하기",
                                    style = AppTextStyle.Body.copy(fontWeight = FontWeight.Bold,color = White)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(Dimens.Medium))

                        Text(
                            text = "가입하시면 서비스 이용약관 및 개인정보 처리방침에 동의하는 것으로 간주됩니다.",
                            style = AppTextStyle.Body.copy(fontSize = 12.sp,color = IconColor),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}