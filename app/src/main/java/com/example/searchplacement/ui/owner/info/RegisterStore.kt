package com.example.searchplacement.ui.owner.info

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.searchplacement.data.store.StoreRequest
import com.example.searchplacement.di.AppModule
import com.example.searchplacement.navigation.OwnerBottomNavItem
import com.example.searchplacement.ui.theme.ButtonMainColor
import com.example.searchplacement.viewmodel.MainViewModel
import com.example.searchplacement.viewmodel.OwnerStoreViewModel
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun RegisterStore(
    navController: NavHostController,
) {
    val ownerStoreViewModel: OwnerStoreViewModel = hiltViewModel()
    val mainViewModel: MainViewModel = hiltViewModel()
    val context = LocalContext.current
    val user by mainViewModel.user.collectAsState()

    var storeName by remember { mutableStateOf("") }
    var storeDesc by remember { mutableStateOf("") }
    var storeAddress by remember { mutableStateOf("") }
    var storeAddressDetail by remember { mutableStateOf("") }
    var bizNum by remember { mutableStateOf("") }
    var bank by remember { mutableStateOf("") }
    var accountNum by remember { mutableStateOf("") }
    var depositor by remember { mutableStateOf("") }
    val bankOptions = listOf("국민", "신한", "농협", "카카오", "토스")
    //카테고리 관련
    val categoryMap = mapOf(
        "CHICKEN" to "치킨",
        "CAFE" to "카페",
        "PIZZA" to "피자",
        "FASTFOOD" to "패스트푸드",
        "CHINESEFOOD" to "중식",
        "KOREANFOOD" to "한식",
        "SNACK" to "분식",
        "JAPANESEFOOD" to "일식",
        "WESTERNFOOD" to "양식",
        "ASIANFOOD" to "아시안",
        "MEAT" to "고기"
    )

    var selectedCategories by remember { mutableStateOf(setOf<String>()) }
    var showCategoryDialog by remember { mutableStateOf(false) }

    val defaultBusinessHours = mapOf(
        "월" to "09:00~18:00",
        "화" to "09:00~18:00",
        "수" to "09:00~18:00",
        "목" to "09:00~18:00",
        "금" to "09:00~18:00",
        "토" to "09:00~18:00",
        "일" to "09:00~18:00"
    )
    val defaultRegularHolidays = mapOf(
        "월요일" to 0,
        "화요일" to 0,
        "수요일" to 0,
        "목요일" to 0,
        "금요일" to 0,
        "토요일" to 0,
        "일요일" to 0
    )
    var imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }


    val registerResult by ownerStoreViewModel.registerResult.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }


    LaunchedEffect(registerResult) {
        if (registerResult?.status == "success") {
            snackbarHostState.showSnackbar("가게 등록 성공")
            navController.navigate(OwnerBottomNavItem.Store.screenRoute)
        } else if (registerResult?.status == "fail") {
            snackbarHostState.showSnackbar("등록 실패: ${registerResult?.message}")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Text(
            "매장 정보", style = TextStyle(
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 30.dp)
        )

        SectionTitle("매장 기본 정보")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("관리자", Modifier.padding(end = 20.dp))
            Text(user?.name ?: "", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("연락처", Modifier.padding(end = 20.dp))
            Text(user?.phone ?: "", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("매장 이름", Modifier.padding(end = 20.dp))
            OutlinedTextField(
                value = storeName,
                onValueChange = { storeName = it },
                label = { Text("매장 이름(필수)") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        Text("매장 소개", modifier = Modifier.padding(vertical = 5.dp))
        OutlinedTextField(
            value = storeDesc,
            onValueChange = { storeDesc = it },
            label = { Text("매장 소개(필수)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp)
        )

        SectionTitle("매장 위치")
        StoreLocationInput(
            address = storeAddress,
            addressDetail = storeAddressDetail,
            onAddressChange = { storeAddress = it },
            onAddressDetailChange = { storeAddressDetail = it },
            url = AppModule.BASE_URL
        )

        SectionTitle("사업자 정보")
        OutlinedTextField(
            value = bizNum,
            onValueChange = { bizNum = it },
            label = { Text("사업자등록번호(필수)") },
            modifier = Modifier.fillMaxWidth()
        )
        // 은행 선택 (Dropdown)
        BankDropdown(
            selectedBank = bank,
            onBankSelected = { bank = it },
            options = bankOptions
        )
        OutlinedTextField(
            value = accountNum,
            onValueChange = { accountNum = it },
            label = { Text("계좌번호(필수)") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = depositor,
            onValueChange = { depositor = it },
            label = { Text("예금주(필수)") },
            modifier = Modifier.fillMaxWidth()
        )

        SectionTitle("카테고리 선택")
        OutlinedButton(
            onClick = { showCategoryDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            val display = selectedCategories.map { categoryMap[it] ?: it }
            Text(if (display.isEmpty()) "카테고리 선택" else display.joinToString(", "))
        }
        if (showCategoryDialog) {
            CategorySelectDialog(
                selected = selectedCategories,
                onSelectedChanged = {
                    selectedCategories = it
                },
                onDismiss = { showCategoryDialog = false }
            )
        }

        SectionTitle("사진 등록 (최대 5장)")
        Text(
            "*첫 번째 사진이 대표 이미지입니다.",
            color = Color.Gray,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 10.dp)
        )
        ImageUploadField(imageUris, onImagesChanged = { imageUris = it })

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                coroutineScope.launch {
                    val request = StoreRequest(
                        storeName = storeName,
                        location = "$storeAddress $storeAddressDetail",
                        description = storeDesc,
                        businessRegistrationNumber = bizNum,
                        bank = bank,
                        accountNumber = accountNum,
                        depositor = depositor,
                        businessHours = defaultBusinessHours,
                        category = selectedCategories.toList(),
                        regularHolidays = defaultRegularHolidays,
                        temporaryHolidays = emptyList()
                    )
                    val imageFiles = imageUris.map { uri -> uriToFile(context, uri) }
                    ownerStoreViewModel.registerStore(request, imageFiles)
                    navController.navigate(OwnerBottomNavItem.Store.screenRoute)
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = ButtonMainColor)
        ) {
            Text("저장")
        }
    }
}

fun uriToFile(context: Context, uri: Uri): File {
    val inputStream = context.contentResolver.openInputStream(uri)
    val file = File(context.cacheDir, "temp_image_${System.currentTimeMillis()}")
    inputStream?.use { input ->
        file.outputStream().use { output ->
            input.copyTo(output)
        }
    }
    return file
}
