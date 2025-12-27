package com.example.searchplacement.presentation.user.auth.register

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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.searchplacement.presentation.theme.AppTextStyle
import com.example.searchplacement.presentation.theme.Black
import com.example.searchplacement.presentation.theme.CardBorderTransparentColor
import com.example.searchplacement.presentation.theme.Dimens
import com.example.searchplacement.presentation.theme.IconColor
import com.example.searchplacement.presentation.theme.White
import com.example.searchplacement.presentation.theme.loginLogoColor
import com.example.searchplacement.presentation.user.component.ImageInput
import com.example.searchplacement.presentation.user.component.LocationInput
import com.example.searchplacement.presentation.user.component.RegisterPasswordField
import com.example.searchplacement.presentation.user.component.RegisterTextField
import com.example.searchplacement.presentation.user.component.UserTypeInput
import com.example.searchplacement.presentation.utils.getImageFilePart

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RegisterScreen(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CardBorderTransparentColor)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
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
                        .clickable { onAction(RegisterAction.OnBackClick) }
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
                        style = AppTextStyle.Body.copy(fontSize = 14.sp, color = IconColor),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    UserTypeInput(
                        userType = state.userType.toString(),
                        onUserTypeChanged = { onAction(RegisterAction.OnUserTypeChanged(it)) }
                    )

                    if (state.userType?.isNotBlank() ?: false) {
                        RegisterTextField(
                            value = state.email,
                            onValueChange = { onAction(RegisterAction.OnEmailChanged(it)) },
                            label = "이메일",
                            placeholder = "example@email.com",
                            leadingIcon = Icons.Default.Email,
                            imeAction = ImeAction.Next,
                            keyboardType = KeyboardType.Email
                        )
                    }
                    if (state.email.isNotBlank()) {
                        RegisterPasswordField(
                            value = state.password,
                            onValueChange = { onAction(RegisterAction.OnPasswordChanged(it)) },
                            label = "비밀번호",
                            placeholder = "8자 이상 입력",
                            imeAction = ImeAction.Next
                        )
                    }
                    if (state.password.isNotBlank()) {
                        val isMismatch = state.password.isNotBlank() &&
                                state.passwordConfirm.isNotBlank() &&
                                state.password != state.passwordConfirm

                        RegisterPasswordField(
                            value = state.passwordConfirm,
                            onValueChange = { onAction(RegisterAction.OnPasswordConfirmChanged(it)) },
                            label = "비밀번호 확인",
                            placeholder = "비밀번호 재입력",
                            imeAction = ImeAction.Next,
                            isError = isMismatch,
                            errorMessage = "비밀번호가 일치하지 않습니다"
                        )
                    }

                    if (state.password.isNotBlank() &&
                        state.password == state.passwordConfirm
                    ) {
                        RegisterTextField(
                            value = state.name,
                            onValueChange = { onAction(RegisterAction.OnNameChanged(it)) },
                            label = "이름",
                            placeholder = "홍길동",
                            leadingIcon = Icons.Default.Person,
                            imeAction = ImeAction.Next
                        )
                    }
                    if (state.name.isNotBlank()) {
                        RegisterTextField(
                            value = state.phone,
                            onValueChange = { onAction(RegisterAction.OnPhoneChanged(it)) },
                            label = "전화번호",
                            placeholder = "010-1234-5678",
                            leadingIcon = Icons.Default.Phone,
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Phone
                        )
                    }
                    if (state.phone.isNotBlank()) {
                        LocationInput(
                            location = state.location,
                            onLocationChanged = { onAction(RegisterAction.OnLocationChanged(it)) }
                        )
                    }
                    if (state.location.isNotBlank()) {
                        ImageInput(
                            imageUri = state.imageUri,
                            onImageSelected = { onAction(RegisterAction.OnImageUriChanged(it)) }
                        )
                    }

                    if (state.imageUri != null) {
                        Spacer(modifier = Modifier.height(Dimens.Large))

                        Button(
                            onClick = {
                                val imageFilePart =
                                    getImageFilePart(context, state.imageUri)
                                onAction(RegisterAction.OnRegisterClick(imageFilePart))
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(Dimens.Default),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent
                            ),
                            enabled = !state.isLoading
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        color = if (state.isLoading) Color.Gray else loginLogoColor,
                                        shape = RoundedCornerShape(Dimens.Default)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (state.isLoading) {
                                    CircularProgressIndicator(color = White)
                                } else {
                                    Text(
                                        "사용자로 가입하기",
                                        style = AppTextStyle.Body.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = White
                                        )
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(Dimens.Medium))

                        Text(
                            text = "가입하시면 서비스 이용약관 및 개인정보 처리방침에 동의하는 것으로 간주됩니다.",
                            style = AppTextStyle.Body.copy(
                                fontSize = 12.sp,
                                color = IconColor
                            ),
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

@Preview
@Composable
private fun RegisterScreenPreview() {
    RegisterScreen(
        state = RegisterState(
            userType = "OWNER",
            email = "Email",
            password = "Password",
            passwordConfirm = "Password",
            name = "Name",
            phone = "010-1234-5678",
            location = "Location",
        )
    )
}