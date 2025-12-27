package com.example.searchplacement.presentation.user.setting

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
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.searchplacement.core.di.AppModule
import com.example.searchplacement.presentation.theme.AppTextStyle
import com.example.searchplacement.presentation.theme.Black
import com.example.searchplacement.presentation.theme.ButtonMainColor
import com.example.searchplacement.presentation.theme.CardBorderTransparentColor
import com.example.searchplacement.presentation.theme.Dimens
import com.example.searchplacement.presentation.theme.Gray
import com.example.searchplacement.presentation.theme.IconTextColor
import com.example.searchplacement.presentation.theme.White
import com.example.searchplacement.presentation.theme.reservationCountColor
import com.example.searchplacement.presentation.utils.rememberImageLoaderWithToken

@Composable
fun SettingScreen(
    state: SettingState,
    onAction: (SettingAction) -> Unit,
    onNavigateToInformation: () -> Unit,
    onNavigateToCheckPassword: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = Dimens.Medium),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.Small)
        ) {
            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = null,
                tint = reservationCountColor,
                modifier = Modifier.size(Dimens.Large)
            )
            Text(
                text = "마이 페이지",
                style = AppTextStyle.Body.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = IconTextColor
                )
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.Small)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(Dimens.Medium)) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onNavigateToInformation()
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
                        if (state.image != null) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data("${AppModule.BASE_URL}/api/files/" + state.image)
                                    .crossfade(true)
                                    .build(),
                                imageLoader = rememberImageLoaderWithToken(),
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
                                text = state.name ?: "",
                                style = AppTextStyle.BodyText
                            )
                            Text(
                                text = state.email ?: "",
                                style = AppTextStyle.BodyGray.copy(fontWeight = FontWeight.Normal)
                            )
                        }
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToCheckPassword() },
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
                    onClick = { onAction(SettingAction.OpenLogoutDialog) },
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
                    onClick = { onAction(SettingAction.OpenDeleteDialog) },
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
    }
}
