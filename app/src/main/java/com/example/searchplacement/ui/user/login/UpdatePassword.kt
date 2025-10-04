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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.searchplacement.navigation.MainBottomNavItem
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.ButtonMainColor
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdatePassword(navController: NavHostController, mainViewModel: MainViewModel) {
    val showPassword = remember { mutableStateOf(false) }
    val checkShowPassword = remember { mutableStateOf(false) }
    val passwordState = remember { mutableStateOf("") }
    val checkPwState = remember { mutableStateOf("") }
    val showError = remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val user by mainViewModel.user.collectAsState()
    val updateResult by mainViewModel.passwordUpdateResult.collectAsState()
    val context = LocalContext.current

    val passwordMismatch = showError.value && passwordState.value != checkPwState.value

    val passwordFocusRequester = remember { FocusRequester() }
    val checkFocusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {Text("새 비밀번호")},
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
                .padding(Dimens.Default)
        ) {
            Text(
                text = "새로운 비밀번호를 입력해주세요.",
                style = AppTextStyle.Body,
                modifier = Modifier.fillMaxWidth().padding(horizontal = Dimens.Small)
            )

            OutlinedTextField(
                value = passwordState.value,
                onValueChange = {
                    passwordState.value = it
                    showError.value = false
                },
                label = { Text("New") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = if (showPassword.value) KeyboardType.Text else KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        checkFocusRequester.requestFocus()
                    }
                ),
                visualTransformation = if (showPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    Box(modifier = Modifier.padding(end = Dimens.Small)) {
                        TextButton(onClick = { showPassword.value = !showPassword.value }) {
                            Text(if (showPassword.value) "Hide" else "Show", color = Color.Black)
                        }
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (passwordMismatch) Color.Red else Color.Black,
                    unfocusedBorderColor = if (passwordMismatch) Color.Red else Color.Black,
                    focusedLabelColor = Color.Black,
                    cursorColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.Small)
                    .focusRequester(passwordFocusRequester)
            )

            Text(
                text = "비밀번호를 다시 한번 입력해주세요.",
                style = AppTextStyle.Body,
                modifier = Modifier.fillMaxWidth().padding(horizontal = Dimens.Small)
            )

            OutlinedTextField(
                value = checkPwState.value,
                onValueChange = {
                    checkPwState.value = it
                    showError.value = false
                },
                label = { Text("Check") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = if (checkShowPassword.value) KeyboardType.Text else KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        if (passwordState.value == checkPwState.value && passwordState.value.isNotBlank()) {
                            mainViewModel.updatePassword(user?.userId?.toLong() ?: 0L, passwordState.value)
                            showError.value = false
                        } else {
                            showError.value = true
                        }
                    }
                ),
                visualTransformation = if (checkShowPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    Box(modifier = Modifier.padding(end = Dimens.Small)) {
                        TextButton(onClick = {
                            checkShowPassword.value = !checkShowPassword.value
                        }) {
                            Text(
                                if (checkShowPassword.value) "Hide" else "Show",
                                color = Color.Black
                            )
                        }
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (passwordMismatch) Color.Red else Color.Black,
                    unfocusedBorderColor = if (passwordMismatch) Color.Red else Color.Black,
                    focusedLabelColor = Color.Black,
                    cursorColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.Small)
                    .focusRequester(checkFocusRequester)
            )

            if (passwordMismatch) {
                Text(
                    text = "비밀번호가 일치하지 않습니다.",
                    style = AppTextStyle.redPoint,
                    modifier = Modifier.padding(start = Dimens.Medium, bottom = Dimens.Small)
                )
            }

            Button(
                onClick = {
                    coroutineScope.launch {
                        if (passwordState.value == checkPwState.value && passwordState.value.isNotBlank()) {
                            mainViewModel.updatePassword(
                                user?.userId?.toLong() ?: 0L,
                                passwordState.value
                            )
                            showError.value = false
                        } else {
                            showError.value = true
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(Dimens.Small),
                shape = MaterialTheme.shapes.large,
                colors = ButtonColors(
                    containerColor = ButtonMainColor,
                    contentColor = Color.Black,
                    disabledContainerColor = Color.DarkGray,
                    disabledContentColor = Color.Black
                )
            ) {
                Text("변경", color = Color.White)
            }
        }
    }


    LaunchedEffect(Unit) {
        passwordFocusRequester.requestFocus()
        keyboardController?.show()
    }

    LaunchedEffect(updateResult) {
        updateResult?.let {
            if (it.status == "success") {
                Toast.makeText(context, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show()
                navController.popBackStack(
                    route = MainBottomNavItem.Setting.screenRoute,
                    inclusive = false
                )
            } else {
                Toast.makeText(context,"오류가 발생했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                navController.popBackStack(
                    route = MainBottomNavItem.Setting.screenRoute,
                    inclusive = false
                )
            }
        }
    }
}