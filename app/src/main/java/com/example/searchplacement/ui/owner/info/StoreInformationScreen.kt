package com.example.searchplacement.ui.owner.info

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.searchplacement.data.store.StoreRequest
import com.example.searchplacement.di.AppModule
import com.example.searchplacement.navigation.OwnerBottomNavItem
import com.example.searchplacement.ui.theme.ButtonMainColor
import com.example.searchplacement.ui.utils.rememberImageLoaderWithToken
import com.example.searchplacement.viewmodel.MainViewModel
import com.example.searchplacement.viewmodel.StoreListViewModel

@Composable
fun StoreInformationScreen(navController: NavHostController) {
    val storeListViewModel: StoreListViewModel = hiltViewModel()
    val mainViewModel: MainViewModel = hiltViewModel()
    val store by storeListViewModel.selectedStore.collectAsState()
    val user by mainViewModel.user.collectAsState()
    var isEdit by remember { mutableStateOf(false) }
    var storeName by remember { mutableStateOf("") }
    var storeDesc by remember { mutableStateOf("") }
    var storeAddress by remember { mutableStateOf("") }
    var storeAddressDetail by remember { mutableStateOf("") }
    var bizNum by remember { mutableStateOf("") }
    var bank by remember { mutableStateOf("") }
    var accountNum by remember { mutableStateOf("") }
    var depositor by remember { mutableStateOf("") }
    val bankOptions = listOf("국민", "신한", "농협", "카카오", "토스", "하나", "우리", "기업 IBK")
    var imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    val context = LocalContext.current
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
    val token = user?.token ?: ""
    val imageLoader = rememberImageLoaderWithToken()



    LaunchedEffect(isEdit, store) {
        if (isEdit && store != null) {
            storeName = store?.storeName ?: ""
            storeDesc = store?.description ?: ""
            storeAddress = store?.location ?: ""
            bizNum = store?.businessRegistrationNumber ?: ""
            bank = store?.bank ?: ""
            accountNum = store?.accountNumber ?: ""
            depositor = store?.depositor ?: ""
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
        if (!isEdit) {
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
                Text(user?.phone ?: "정보를 등록해 주세요.", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("매장 이름", Modifier.padding(end = 20.dp))
                Text(
                    store?.storeName ?: "정보를 등록해 주세요.",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Text("매장 소개", modifier = Modifier.padding(vertical = 5.dp))
            Text(
                text = store?.description ?: "정보를 등록해 주세요.",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp).border(1.dp, Color.Black, shape = RoundedCornerShape(6.dp)).padding(10.dp),
                lineHeight = 20.sp,
                minLines = 3,
                maxLines = 3
            )

            SectionTitle("매장 위치")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("주소", Modifier.padding(end = 20.dp))
                Text(
                    store?.location ?: "정보를 등록해 주세요.",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            SectionTitle("사업자 정보")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("사업자 번호", Modifier.padding(end = 20.dp))
                Text(
                    store?.businessRegistrationNumber ?: "정보를 등록해 주세요.",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("은행", Modifier.padding(end = 20.dp))
                Text(store?.bank ?: "정보를 등록해 주세요.", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("계좌번호", Modifier.padding(end = 20.dp))
                Text(
                    store?.accountNumber ?: "정보를 등록해 주세요.",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("예금주", Modifier.padding(end = 20.dp))
                Text(
                    store?.depositor ?: "정보를 등록해 주세요.",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            SectionTitle("카테고리")
            Text(
                text = store?.category?.joinToString(", ") ?: "정보를 등록해 주세요.",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
            )

            SectionTitle("매장 사진")
            Text(
                "*첫 번째 사진이 대표 이미지입니다.",
                color = Color.Gray,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 10.dp)
            )

            if (store?.image?.isEmpty() == true) {
                Text("정보를 등록해 주세요.", modifier = Modifier.padding(10.dp))
            } else {
                LazyRow(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(store!!.image) { fileName ->
                        val imageUrl = "${AppModule.BASE_URL}/api/files/$fileName"
                        AsyncImage(
                            model = imageUrl,
                            imageLoader = imageLoader,
                            contentDescription = null,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { isEdit = true },
                colors = ButtonDefaults.buttonColors(containerColor = ButtonMainColor)
            ) {
                Text("정보 수정")
            }

        } else {
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
                Text(user?.phone ?: "정보를 등록해 주세요.", fontSize = 18.sp, fontWeight = FontWeight.Bold)
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
                    label = { Text("매장 이름") },
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

            ImageUploadField(imageUris = imageUris, onImagesChanged = { imageUris = it })

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    val request = StoreRequest(
                        storeName = storeName,
                        location = storeAddress,
                        description = storeDesc,
                        businessRegistrationNumber = bizNum,
                        bank = bank,
                        accountNumber = accountNum,
                        depositor = depositor,
                        businessHours = store?.businessHours ?: emptyMap(),
                        category = selectedCategories.toList(),
                        regularHolidays = store?.regularHolidays ?: emptyMap(),
                        temporaryHolidays = store?.temporaryHolidays ?: emptyList()
                    )

                    val imageFiles = imageUris.map { uri -> uriToFile(context, uri) }

                    storeListViewModel.updateStore(
                        storeId = store?.storePK ?: 0,
                        request = request,
                        images = if (imageFiles.isNotEmpty()) imageFiles else null
                    )
                    isEdit = false
                    navController.navigate(OwnerBottomNavItem.Store.screenRoute)
                },
                colors = ButtonDefaults.buttonColors(containerColor = ButtonMainColor)
            ) {
                Text("저장")
            }

        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        title, style = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        ),
        modifier = Modifier.padding(bottom = 5.dp)
    )
    HorizontalDivider(thickness = 2.dp, color = Color.Black)
}

@Composable
fun BankDropdown(
    selectedBank: String,
    onBankSelected: (String) -> Unit,
    options: List<String>
) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
        ) {
            OutlinedTextField(
                value = selectedBank,
                onValueChange = {},
                label = { Text("은행(필수)") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown Arrow",
                        modifier = Modifier.clickable { expanded = true }
                    )
                }
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = Color.White
        ) {
            options.forEach { bank ->
                DropdownMenuItem(
                    text = { Text(bank) },
                    onClick = {
                        onBankSelected(bank)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun ImageUploadField(
    imageUris: List<Uri>,
    onImagesChanged: (List<Uri>) -> Unit
) {
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            if (imageUris.size < 5) {
                onImagesChanged(imageUris + it)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(5) { index ->
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray)
                        .clickable {
                            if (index >= imageUris.size) {
                                launcher.launch("image/*")
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (index < imageUris.size) {
                        Image(
                            painter = rememberAsyncImagePainter(imageUris[index]),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Image",
                            tint = Color.White,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            }
        }

        if (imageUris.isNotEmpty()) {
            Button(
                onClick = { onImagesChanged(emptyList()) },
                modifier = Modifier.padding(top = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                ),
                border = BorderStroke(1.dp, Color.Black)
            ) {
                Text("사진 초기화")
            }
        }
    }
}

