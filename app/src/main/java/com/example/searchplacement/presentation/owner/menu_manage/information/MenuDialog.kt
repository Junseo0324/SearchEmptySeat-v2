package com.example.searchplacement.presentation.owner.menu_manage.information

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.searchplacement.data.menu.MenuResponse
import com.example.searchplacement.data.section.MenuSectionResponse
import com.example.searchplacement.di.AppModule
import com.example.searchplacement.presentation.owner.info.uriToFile
import com.example.searchplacement.presentation.theme.White
import java.io.File


@Composable
fun MenuDialog(
    title: String,
    menu: MenuResponse? = null,
    imageLoader: ImageLoader,
    sections: List<MenuSectionResponse>,
    selectedSection: MenuSectionResponse? = null,
    onSectionSelected: (MenuSectionResponse) -> Unit = {},
    onAddSectionClick: () -> Unit = {},
    onDismiss: () -> Unit,
    onConfirm: (name: String, price: Int, section: MenuSectionResponse, desc: String, imageFile: File?) -> Unit
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf(menu?.name ?: "") }
    var price by remember { mutableStateOf(menu?.price?.toString() ?: "") }
    var sectionState by remember { mutableStateOf(selectedSection ?: sections.firstOrNull()) }
    var desc by remember { mutableStateOf(menu?.description ?: "") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            imageUri = it
        }
    }


    AlertDialog(
        containerColor = White,
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("메뉴 이름") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("가격") },
                    modifier = Modifier.fillMaxWidth()
                )

                SectionDropdown(
                    sections = sections,
                    selectedSection = sectionState,
                    onSectionSelected = {
                        sectionState = it
                        onSectionSelected(it)
                    },
                    onAddSectionClick = onAddSectionClick
                )

                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("설명") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))
                Text("이미지 (1개)", fontWeight = FontWeight.Bold)

                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray)
                        .clickable { launcher.launch("image/*") }
                        .align(Alignment.CenterHorizontally),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        imageUri != null -> {
                            Image(
                                painter = rememberAsyncImagePainter(imageUri),
                                contentDescription = "선택된 이미지",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        menu?.image?.isNotEmpty() == true -> {
                            AsyncImage(
                                model = "${AppModule.BASE_URL}/api/files/${menu.image.first()}",
                                imageLoader = imageLoader,
                                contentDescription = "기존 이미지",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        else -> {
                            Icon(Icons.Default.Add, contentDescription = "이미지 추가", tint = Color.White, modifier = Modifier.size(40.dp))
                        }
                    }
                }

                if (imageUri != null) {
                    TextButton(
                        onClick = { imageUri = null },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("이미지 제거")
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val imageFile = imageUri?.let { uriToFile(context, it) }
                if (sectionState != null) {
                    onConfirm(
                        name,
                        price.toIntOrNull() ?: 0,
                        sectionState!!,
                        desc,
                        imageFile
                    )
                }
            }) {
                Text("확인")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}