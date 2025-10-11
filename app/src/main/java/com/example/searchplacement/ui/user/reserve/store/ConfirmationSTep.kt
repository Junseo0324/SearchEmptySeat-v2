package com.example.searchplacement.ui.user.reserve.store

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.searchplacement.data.reserve.ReservationData
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.ChipBorderColor
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.IconTextColor
import com.example.searchplacement.ui.theme.StoreTabBackgroundColor
import com.example.searchplacement.ui.theme.White
import com.example.searchplacement.ui.theme.reservationCountColor
import java.time.format.DateTimeFormatter

@Composable
fun ConfirmationStep(
    reservationData: ReservationData,
    storeName: String,
    onPaymentSelected: (String) -> Unit
) {
    var selectedPaymentMethod by rememberSaveable {
        mutableStateOf(reservationData.paymentMethod ?: "offline")
    }

    LaunchedEffect(selectedPaymentMethod) {
        if (reservationData.paymentMethod != selectedPaymentMethod) {
            onPaymentSelected(selectedPaymentMethod)
        }
    }
    LaunchedEffect(reservationData.paymentMethod) {
        reservationData.paymentMethod.let { vmValue ->
            if (vmValue != selectedPaymentMethod) selectedPaymentMethod = vmValue
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(StoreTabBackgroundColor),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Text(
                text = "예약 정보 확인",
                style = AppTextStyle.Body.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = IconTextColor)
            )
        }

        // 예약 정보
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(Dimens.Medium),
                colors = CardDefaults.cardColors(containerColor = White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(Dimens.Medium)
                ) {
                    InfoRow("매장", storeName)
                    InfoRow("인원", "${reservationData.numberOfPeople}명")
                    InfoRow(
                        "날짜",
                        reservationData.selectedDate?.format(DateTimeFormatter.ofPattern("yyyy. M. d."))
                            ?: ""
                    )
                    InfoRow("시간", reservationData.selectedTime ?: "")
                    InfoRow("좌석", getTableName(reservationData.selectedTable))
                }
            }
        }

        // 주문 메뉴
        if (reservationData.selectedMenus.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(Dimens.Medium),
                    colors = CardDefaults.cardColors(containerColor = White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(Dimens.Default)
                    ) {
                        Text(
                            text = "주문 메뉴",
                            style = AppTextStyle.Body.copy(fontWeight = FontWeight.Bold, color = IconTextColor)
                        )

                        reservationData.selectedMenus.values.forEach { menu ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${menu.name} × ${menu.quantity}",
                                    style = AppTextStyle.Body.copy(fontSize = 14.sp, color = IconTextColor)
                                )
                                Text(
                                    text = "${String.format("%,d", menu.price * menu.quantity)}원",
                                    style = AppTextStyle.Body.copy(fontSize = 14.sp, color = IconTextColor)
                                )
                            }
                        }

                        HorizontalDivider(color = ChipBorderColor)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "총 금액",
                                style = AppTextStyle.Body.copy(fontWeight = FontWeight.Bold, color = IconTextColor)
                            )
                            Text(
                                text = "${String.format("%,d", reservationData.totalPrice)}원",
                                style = AppTextStyle.Body.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = reservationCountColor)
                            )
                        }
                    }
                }
            }
        }

        // 결제 방법
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(Dimens.Medium),
                colors = CardDefaults.cardColors(containerColor = White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(Dimens.Default)
                ) {
                    Text(
                        text = "결제 방법",
                        style = AppTextStyle.Body.copy(fontWeight = FontWeight.Bold, color = IconTextColor)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        PaymentMethodButton(
                            icon = Icons.Default.CreditCard,
                            label = "카드결제",
                            isSelected = selectedPaymentMethod == "card",
                            onClick = {
                                selectedPaymentMethod = "card"
                            },
                            modifier = Modifier.weight(1f)
                        )
                        PaymentMethodButton(
                            icon = Icons.Default.PhoneAndroid,
                            label = "모바일결제",
                            isSelected = selectedPaymentMethod == "mobile",
                            onClick = {
                                selectedPaymentMethod = "mobile"
                            },
                            modifier = Modifier.weight(1f)
                        )
                        PaymentMethodButton(
                            icon = Icons.Default.Money,
                            label = "현장결제",
                            isSelected = selectedPaymentMethod == "offline",
                            onClick = {
                                selectedPaymentMethod = "offline"
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}


fun getTableName(tableId: String?): String {
    return when (tableId) {
        "table_1" -> "중앙 4인석"
        "table_2" -> "대형 8인석"
        "table_3" -> "입구 4인석"
        "table_4" -> "창가 2인석"
        else -> "미선택"
    }
}
