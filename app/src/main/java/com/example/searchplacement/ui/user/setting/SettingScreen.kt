package com.example.searchplacement.ui.user.setting

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.searchplacement.activity.LoginActivity
import com.example.searchplacement.di.AppModule
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Black
import com.example.searchplacement.ui.theme.ButtonMainColor
import com.example.searchplacement.ui.theme.CardBorderTransparentColor
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.Gray
import com.example.searchplacement.ui.theme.White
import com.example.searchplacement.ui.utils.rememberImageLoaderWithToken
import com.example.searchplacement.viewmodel.MainViewModel

@Composable
fun SettingScreen(navController: NavHostController, mainViewModel: MainViewModel) {
    val user by mainViewModel.user.collectAsState()
    val IMAGE_URL = "${AppModule.BASE_URL}/api/files/"
    val imageLoader = rememberImageLoaderWithToken()

    val context = LocalContext.current
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        mainViewModel.logoutEvent.collect {
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.Small)
    ) {

        Column(verticalArrangement = Arrangement.spacedBy(Dimens.Medium)) {
            Text(
                text = "개인정보 변경",
                style = AppTextStyle.BodyGray
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate("information")
                    },
                shape = RoundedCornerShape(Dimens.Default),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = Dimens.Nano)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimens.Medium),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.Medium),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (user?.image != null) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(IMAGE_URL + user?.image)
                                .crossfade(true)
                                .build(),
                            imageLoader = imageLoader,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(CardBorderTransparentColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "기본 프로필",
                                modifier = Modifier.size(32.dp),
                                tint = Black
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(Dimens.Tiny)
                    ) {
                        Text(
                            text = user?.name ?: "",
                            style = AppTextStyle.BodyText
                        )
                        Text(
                            text = user?.email ?: "",
                            style = AppTextStyle.BodyGray.copy(fontWeight = FontWeight.Normal)
                        )
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate("checkPassword") },
                shape = RoundedCornerShape(Dimens.Default),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = Dimens.Nano)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Dimens.Medium),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Dimens.Default),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(Dimens.Small))
                                .background(White),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "비밀번호",
                                tint = ButtonMainColor,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Text(
                            text = "비밀번호 변경",
                            style = AppTextStyle.Button.copy(color = Black)
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))

        Column(
            verticalArrangement = Arrangement.spacedBy(Dimens.Small)
        ) {
            OutlinedButton(
                onClick = { showLogoutDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(Dimens.Small),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Gray
                )
            ) {
                Text(
                    text = "로그아웃",
                    style = AppTextStyle.BodySmall.copy(fontSize = 14.sp)
                )
            }

            TextButton(
                onClick = { showDeleteDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                Text(
                    text = "회원탈퇴",
                    style = AppTextStyle.redPoint.copy(fontWeight = FontWeight.Medium)
                )
            }
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Text(
                    text = "로그아웃",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text("정말 로그아웃 하시겠습니까?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        mainViewModel.logout()
                    }
                ) {
                    Text("로그아웃", color = Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLogoutDialog = false }
                ) {
                    Text("취소", color = Gray)
                }
            }
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(
                    text = "회원탈퇴",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("정말 회원탈퇴 하시겠습니까?")
                    Text(
                        text = "모든 데이터가 삭제되며 복구할 수 없습니다.",
                        style = AppTextStyle.BodySmall
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        // TODO : 회원 탈퇴 처리
                    }
                ) {
                    Text("탈퇴", color = Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("취소", color = Gray)
                }
            }
        )
    }


}
