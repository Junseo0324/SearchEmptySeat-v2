package com.example.searchplacement.ui.user.reserve.store

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.searchplacement.data.reserve.ReservationRequest
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.ChipBorderColor
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.IconColor
import com.example.searchplacement.ui.theme.IconTextColor
import com.example.searchplacement.ui.theme.White
import com.example.searchplacement.ui.theme.reservationCountColor
import com.example.searchplacement.viewmodel.ReservationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationFlowScreen(
    navController: NavHostController,
    storeId: Long,
) {
    val reservationViewModel: ReservationViewModel = hiltViewModel()
    val menus by reservationViewModel.menus.collectAsState()
    val sections by reservationViewModel.sections.collectAsState()
    val placement by reservationViewModel.placement.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        reservationViewModel.getStoreData(storeId)
        reservationViewModel.fetchMenusAndSections(storeId)
        reservationViewModel.getPlacementByStore(storeId)
    }

    val store = reservationViewModel.storeData.collectAsState().value

    val storeName = store?.storeName ?: ""
    val businessHours = store?.businessHours ?: emptyMap()
    var currentStep by remember { mutableStateOf(ReservationStep.PEOPLE_COUNT) }
    val reservationData by reservationViewModel.reservationData


    val stepProgress = when (currentStep) {
        ReservationStep.PEOPLE_COUNT -> 0.16f
        ReservationStep.DATE_SELECT -> 0.33f
        ReservationStep.TIME_SELECT -> 0.5f
        ReservationStep.TABLE_SELECT -> 0.66f
        ReservationStep.MENU_SELECT -> 0.83f
        ReservationStep.CONFIRMATION -> 1f
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = store?.storeName ?: "정보 없음",
                            style = AppTextStyle.Body.copy(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = when (currentStep) {
                                ReservationStep.PEOPLE_COUNT -> "방문 인원"
                                ReservationStep.DATE_SELECT -> "날짜 선택"
                                ReservationStep.TIME_SELECT -> "시간 선택"
                                ReservationStep.TABLE_SELECT -> "좌석 선택"
                                ReservationStep.MENU_SELECT -> "메뉴 선택"
                                ReservationStep.CONFIRMATION -> "예약 확인"
                            },
                            style = AppTextStyle.Body.copy(fontSize = 14.sp, color = IconColor)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (currentStep == ReservationStep.PEOPLE_COUNT) {
                            navController.popBackStack()
                        } else {
                            currentStep =
                                ReservationStep.entries.toTypedArray()[currentStep.ordinal - 1]
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "뒤로가기")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LinearProgressIndicator(
                progress = { stepProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.Nano),
                color = reservationCountColor,
                trackColor = ChipBorderColor
            )

            Box(modifier = Modifier.weight(1f)) {
                when (currentStep) {
                    ReservationStep.PEOPLE_COUNT -> PeopleCountStep(
                        reservationData = reservationData,
                        onChangePeople = { newCount ->
                            reservationViewModel.updateReservation {
                                it.copy(numberOfPeople = newCount)
                            }
                        }
                    )
                    ReservationStep.DATE_SELECT -> DateSelectStep(
                        reservationData = reservationData,
                        businessHours = businessHours,
                        onSelectedDate = { date ->
                            reservationViewModel.updateReservation {
                                it.copy(selectedDate = date)
                            }
                        }
                    )
                    ReservationStep.TIME_SELECT -> TimeSelectStep(
                        reservationData = reservationData,
                        businessHours = businessHours,
                        onSelectedTime = { time ->
                            reservationViewModel.updateReservation {
                                it.copy(selectedTime = time)
                            }
                        }
                    )
                    ReservationStep.TABLE_SELECT -> TableSelectStep(
                        reservationData = reservationData,
                        placement = placement,
                        onSelectTable = { tableId ->
                            reservationViewModel.updateReservation {
                                it.copy(selectedTable = tableId)
                            }
                        }
                    )
                    ReservationStep.MENU_SELECT -> MenuSelectStep(
                        reservationData = reservationData,
                        menus = menus,
                        sections = sections,
                        onUpdateMenus = { updatedMenus ->
                            reservationViewModel.updateReservation {
                                it.copy(
                                    selectedMenus = updatedMenus,
                                    totalPrice = updatedMenus.values.sumOf { item -> item.price * item.quantity }
                                )
                            }
                        }
                    )
                    ReservationStep.CONFIRMATION -> ConfirmationStep(
                        reservationData,
                        storeName,
                        onPaymentSelected = { payment ->
                            reservationViewModel.updateReservation {
                                it.copy(
                                    paymentMethod = payment
                                )
                            }
                        },

                    )
                }
            }

            Button(
                onClick = {
                    if (currentStep == ReservationStep.CONFIRMATION) {
                        if (reservationData.selectedTable == null) {
                            Toast.makeText(context, "좌석을 선택해주세요", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        val menuMap = reservationData.selectedMenus.mapValues { (_, menu) ->
                            mapOf(
                                "name" to menu.name,
                                "price" to menu.price,
                                "quantity" to menu.quantity
                            )
                        }
                        val request = ReservationRequest(
                            userId = reservationViewModel.userId.value?.toLong() ?: 0L,
                            storePK = storeId,
                            reservationTime = "${reservationData.selectedDate}T${reservationData.selectedTime}",
                            tableNumber = reservationData.selectedTable?.toIntOrNull() ?: 0,
                            menu = menuMap,
                            partySize = reservationData.numberOfPeople,
                            paymentMethod = reservationData.paymentMethod,
                            status = "pending"
                        )
                        reservationViewModel.createReservation(request) { success ->
                            if (success) {
                                Toast.makeText(context, "예약이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                                navController.navigate("store/$storeId") {
                                    popUpTo("reservation_flow") { inclusive = true }
                                }
                            } else {
                                Toast.makeText(context, "예약에 실패했습니다.", Toast.LENGTH_SHORT).show()
                            }
                        }

                    }else {
                        currentStep = ReservationStep.entries.toTypedArray()[currentStep.ordinal + 1]
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.Medium)
                    .height(56.dp),
                shape = RoundedCornerShape(Dimens.Default),
                colors = ButtonDefaults.buttonColors(
                    containerColor = IconTextColor
                ),
                enabled = isStepComplete(currentStep, reservationData)
            ) {
                Text(
                    text = if (currentStep == ReservationStep.CONFIRMATION) "예약 완료" else "다음 단계",
                    style = AppTextStyle.Body.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.width(Dimens.Small))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, null, modifier = Modifier.size(20.dp))
            }
        }
    }
}

