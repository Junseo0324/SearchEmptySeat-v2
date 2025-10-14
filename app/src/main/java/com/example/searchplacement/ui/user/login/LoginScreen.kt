package com.example.searchplacement.ui.user.login

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.searchplacement.activity.OwnerMainActivity
import com.example.searchplacement.navigation.MainBottomNavItem
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.ChipBorderColor
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.IconColor
import com.example.searchplacement.ui.theme.IconTextColor
import com.example.searchplacement.ui.theme.ViewCountColor
import com.example.searchplacement.ui.theme.White
import com.example.searchplacement.ui.theme.loginLogoColor
import com.example.searchplacement.ui.theme.reservationCountColor
import com.example.searchplacement.viewmodel.LoginViewModel

@Composable
fun LoginScreen(navController: NavHostController) {
    val loginViewModel: LoginViewModel = hiltViewModel()
    val loginResult by loginViewModel.loginResult.collectAsState()
    var showPassword by remember { mutableStateOf(false) }
    var emailState by remember { mutableStateOf("") }
    var passwordState by remember { mutableStateOf("") }
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val passwordFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(loginResult) {
        loginResult?.let { response ->
            when (response.status) {
                "success" -> {
                    val userType = response.data?.userType ?: "USER"
                    if (userType == "OWNER") {
                        val intent = Intent(context, OwnerMainActivity::class.java)
                        context.startActivity(intent)
                        (context as? Activity)?.finish()
                    } else {
                        navController.navigate(MainBottomNavItem.Home.screenRoute)
                    }
                }

                "fail", "error" -> {
                    snackbarHostState.showSnackbar(response.message ?: "로그인 실패")
                }
            }
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { focusManager.clearFocus() })
                }
                .padding(paddingValues)
                .padding(horizontal = Dimens.Large),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(Dimens.Large))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(loginLogoColor, Color(0xFF764BA2))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "로고",
                    tint = Color.White,
                    modifier = Modifier.size(56.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "빈자리를 부탁해",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = IconTextColor,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(Dimens.Small))

            Text(
                text = "매장의 빈자리를 쉽고 빠르게 예약하세요",
                fontSize = 15.sp,
                color = IconColor,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(Dimens.Small)
            ) {
                Text(
                    text = "이메일",
                    style = AppTextStyle.Body.copy(fontSize = 14.sp, color = IconTextColor)
                )

                OutlinedTextField(
                    value = emailState,
                    onValueChange = { emailState = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    placeholder = {
                        Text(
                            "example@email.com",
                            style = AppTextStyle.Body.copy(fontSize = 15.sp, color = ViewCountColor)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            tint = IconColor
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(Dimens.Default),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = loginLogoColor,
                        unfocusedBorderColor = ChipBorderColor,
                        focusedContainerColor = Color(0xFFFAFAFA),
                        unfocusedContainerColor = Color(0xFFFAFAFA),
                        cursorColor = loginLogoColor
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { passwordFocusRequester.requestFocus() }
                    )
                )
            }

            Spacer(modifier = Modifier.height(Dimens.Medium))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(Dimens.Small)
            ) {
                Text(
                    text = "비밀번호",
                    style = AppTextStyle.Body.copy(fontSize = 14.sp, color = IconTextColor)
                )

                OutlinedTextField(
                    value = passwordState,
                    onValueChange = { passwordState = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .focusRequester(passwordFocusRequester),
                    placeholder = {
                        Text(
                            "••••••••",
                            style = AppTextStyle.Body.copy(fontSize = 15.sp, color = ViewCountColor)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = IconColor
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                imageVector = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (showPassword) "비밀번호 숨기기" else "비밀번호 보기",
                                tint = IconColor
                            )
                        }
                    },
                    singleLine = true,
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    shape = RoundedCornerShape(Dimens.Default),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = loginLogoColor,
                        unfocusedBorderColor = ChipBorderColor,
                        focusedContainerColor = Color(0xFFFAFAFA),
                        unfocusedContainerColor = Color(0xFFFAFAFA),
                        cursorColor = loginLogoColor
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 로그인 버튼
            Button(
                onClick = {
                    loginViewModel.login(emailState, passwordState)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(Dimens.Default),
                colors = ButtonDefaults.buttonColors(
                    containerColor = loginLogoColor,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp
                )
            ) {
                Text(
                    text = "로그인",
                    style = AppTextStyle.Body.copy(fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(Dimens.Large))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "또는",
                    style = AppTextStyle.Body.copy(fontSize = 14.sp, color = IconColor)
                )
            }

            Spacer(modifier = Modifier.height(Dimens.Large))

            // 회원가입 버튼
            OutlinedButton(
                onClick = { navController.navigate("register") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(Dimens.Default),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color(0xFF667EEA)
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    width = 1.5.dp
                )
            ) {
                Text(
                    text = "회원가입",
                    style = AppTextStyle.Body.copy(fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(Dimens.Large))

            Text(
                text = "비밀번호를 잊으셨나요?",
                style = AppTextStyle.Body.copy(fontSize = 14.sp, color = reservationCountColor),
                modifier = Modifier
                    .clickable { navController.navigate("find_password") }
                    .padding(Dimens.Small)
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}


