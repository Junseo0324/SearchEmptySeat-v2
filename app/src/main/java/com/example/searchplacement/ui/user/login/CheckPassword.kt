package com.example.searchplacement.ui.user.login

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.searchplacement.ui.theme.AppButtonStyle
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Black
import com.example.searchplacement.ui.theme.ButtonMainColor
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.Gray
import com.example.searchplacement.ui.theme.White
import com.example.searchplacement.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckPassword(navController: NavHostController, mainViewModel: MainViewModel) {
    val showPassword = remember { mutableStateOf(false) }
    val passwordState = remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val user by mainViewModel.user.collectAsState()
    val context = LocalContext.current

    val passwordFocusRequester = remember { FocusRequester() }

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        passwordFocusRequester.requestFocus()
        keyboardController?.show()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {Text("현재 비밀번호")},
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "뒤로 가기"
                        )
                    }
                },
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(Dimens.Small)
        ) {
            Text(text = "현재 비밀번호를 입력하세요.", style = AppTextStyle.Body, modifier = Modifier.padding(horizontal = 8.dp))

            OutlinedTextField(
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                label = { Text("Password") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = if (showPassword.value) KeyboardType.Text else KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        passwordFocusRequester.requestFocus()
                    }
                ),                visualTransformation = if (showPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    Box(
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        TextButton(onClick = { showPassword.value = !showPassword.value }) {
                            Text(
                                if (showPassword.value) "Hide" else "Show",
                                color = Black
                            )
                        }
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Black,
                    unfocusedBorderColor = Black,
                    focusedLabelColor = Black,
                    cursorColor = Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.Small)
                    .focusRequester(passwordFocusRequester)
            )

            Button(
                onClick = {
                    coroutineScope.launch {

                        mainViewModel.authPassword(user?.email ?: "email", passwordState.value)

                        mainViewModel.loginResult.collect { response ->
                            if (response != null) {
                                if (response.status == "success") {
                                    navController.navigate("updatePassword")
                                } else {
                                    Toast.makeText(context, "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            } else {
//                                Toast.makeText(context, "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
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
                    containerColor = ButtonMainColor, contentColor = Black,
                    disabledContainerColor = Gray, disabledContentColor = Black
                )
            ) {
                Text("확인", color = White)
            }

        }
    }

}