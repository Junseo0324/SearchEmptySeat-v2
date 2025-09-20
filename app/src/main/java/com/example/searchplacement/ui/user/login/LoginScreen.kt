package com.example.searchplacement.ui.user.login

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.searchplacement.activity.MainActivity
import com.example.searchplacement.activity.OwnerMainActivity
import com.example.searchplacement.ui.theme.AppButtonStyle
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.ButtonMainColor
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavHostController,loginViewModel: LoginViewModel = viewModel()) {
    val showPassword = remember { mutableStateOf(false) }
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val context = LocalContext.current
    val snackbarHostState = remember {SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding(), top = Dimens.Small, start = Dimens.Small, end = Dimens.Small)
        ) {
            Spacer(modifier = Modifier.height(100.dp))
            AppTitle()
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = emailState.value,
                    onValueChange = { emailState.value = it },
                    label = { Text("Email") },
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

                Spacer(modifier = Modifier.height(10.dp))

                // 비밀번호 입력
                OutlinedTextField(
                    value = passwordState.value,
                    onValueChange = { passwordState.value = it },
                    label = { Text("Password") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = if (showPassword.value) KeyboardType.Text else KeyboardType.Password),
                    visualTransformation = if (showPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        Box(
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            TextButton(onClick = { showPassword.value = !showPassword.value }) {
                                Text(
                                    if (showPassword.value) "Hide" else "Show",
                                    color = Color.Black
                                )
                            }
                        }
                    },
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
                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "처음이신가요?",
                    style = AppTextStyle.mainPoint,
                    modifier = Modifier
                        .padding(bottom = 30.dp, start = Dimens.Small, end = Dimens.Small)
                        .align(Alignment.End)
                        .clickable {
                            navController.navigate("register")
                        }
                )

                Button(
                    onClick = {
                        coroutineScope.launch {
                            loginViewModel.login(emailState.value,passwordState.value)
                            loginViewModel.loginResult.collect { response ->
                                if (response !=null) {
                                    if (response.status == "success") {
                                        val userType = response.data?.userType ?: "USER"
                                        val intent = when(userType) {
                                            "OWNER" -> Intent(context,OwnerMainActivity::class.java)
                                            else -> Intent(context, MainActivity::class.java)
                                        }
                                        context.startActivity(intent)
                                        (context as? Activity)?.finish()
                                    } else {
                                        snackbarHostState.showSnackbar("로그인 실패: ${response?.message ?: "잘못된 이메일 또는 비밀번호"}")
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(Dimens.Small),
                    shape = AppButtonStyle.RoundedShape,
                    colors = ButtonColors(
                        containerColor = ButtonMainColor, contentColor = Color.Black,
                        disabledContainerColor = Color.DarkGray, disabledContentColor = Color.Black
                    )
                ) {
                    Text("Log In",style= AppTextStyle.Button.copy(color = Color.White))
                }

                // 비밀번호 찾기 텍스트
                Text(
                    text = "Forgot your password?",
                    style = AppTextStyle.mainPoint,
                    modifier = Modifier.align(Alignment.End).padding(Dimens.Small)
                        .clickable {
                            navController.navigate("find_password")
                        }
                )
            }
        }
    }


}

@Composable
fun AppTitle(){
        Text(
            text = "빈자리를 부탁해",
            style = AppTextStyle.Title,
            color = Color.Black,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
}

