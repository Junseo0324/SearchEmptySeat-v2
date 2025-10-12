package com.example.searchplacement.ui.user.login

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.searchplacement.ui.theme.AppButtonStyle
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Black
import com.example.searchplacement.ui.theme.ButtonMainColor
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.Gray
import com.example.searchplacement.ui.theme.White
import com.example.searchplacement.viewmodel.LoginViewModel
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FindPasswordScreen(navController: NavHostController,viewModel: LoginViewModel) {
    val emailState = remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) {
        Column(
            modifier = Modifier.padding(Dimens.Medium).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "비밀번호 찾기",
                modifier = Modifier.padding(Dimens.Small).fillMaxWidth(),
                style = AppTextStyle.Title
            )

            HorizontalDivider()

            Text(
                text = "비밀번호를 찾으시려면 계정에 연결된 이메일을 입력해주세요.",
                modifier = Modifier
                    .padding(Dimens.Small)
                    .fillMaxWidth(),
                style = AppTextStyle.BodySmall
            )


            Spacer(modifier = Modifier.height(50.dp))

            TextField(
                value = emailState.value,
                onValueChange = { emailState.value = it },
                placeholder = { Text(text = "등록한 이메일을 입력해주세요",style = AppTextStyle.BodySmall) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.Small),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = White,
                    focusedContainerColor = White,
                    focusedTextColor = Black,
                    focusedIndicatorColor = Black,
                    unfocusedIndicatorColor = Black
                )
            )

            Spacer(modifier = Modifier.padding(20.dp).fillMaxWidth())

            Button(
                onClick = {
                    if (emailState.value.isNotEmpty()){
                        viewModel.findPassword(emailState.value)
                    } else {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("이메일을 입력해주세요.")
                        }
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
                Text(
                    text = "완료", style = AppTextStyle.Button.copy(color = White)
                )
            }


        }
    }

    LaunchedEffect(Unit) {
        viewModel.findPasswordResult.collect { response ->
            coroutineScope.launch {
                when(response.status) {
                    "success" -> {
                        snackbarHostState.showSnackbar("작성한 이메일로 임시 비밀번호를 전송하였습니다.")
                        navController.popBackStack()
                    }
                    "error" -> snackbarHostState.showSnackbar("등록했던 이메일을 정확하게 입력해주세요.")
                    else -> snackbarHostState.showSnackbar("비밀번호 찾기에 실패했습니다.")
                }
            }
        }
    }
}
