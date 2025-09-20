package com.example.searchplacement.ui.user.store

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Black
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.ui.theme.Gray
import com.example.searchplacement.ui.theme.UserPrimaryColor
import com.example.searchplacement.ui.theme.White
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.common.data.ExtraStore


@Composable
fun ReservationScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(Dimens.Small)
    )
    {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Dimens.Tiny, horizontal = Dimens.Small),
            elevation = CardDefaults.elevatedCardElevation(1.dp),
            colors = CardColors(
                contentColor = Black, containerColor = White,
                disabledContentColor = Black, disabledContainerColor = White
            )
        ) {
            Text(
                text = "예약 현황",
                style = AppTextStyle.BodyLarge,
                modifier = Modifier.padding(Dimens.Small)
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(Dimens.Tiny),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "현재",
                    style = AppTextStyle.Body.copy(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier.padding(Dimens.Tiny)
                )
                Text(
                    text = "0",
                    style = AppTextStyle.Body.copy(color = UserPrimaryColor)
                )
            }
            Text(
                text = "요일별 평균 인원 요약",
                modifier = Modifier.padding(Dimens.Small),
                style = AppTextStyle.Body
            )
            VicoGraph()
            Text(
                text = "*평균 한달의 데이터로 차이가 존재",
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(Dimens.Tiny),
                style = AppTextStyle.Caption
            )

        }
    }
}

@Composable
fun VicoGraph() {
    val bottomAxisLabelKey = ExtraStore.Key<List<String>>()
    val modelProducer = remember { CartesianChartModelProducer() }
    val data = listOf(
        "월", "화", "수", "목", "금", "토", "일"
    )
    val bottomAxisValueFormatter = CartesianValueFormatter { context, x, _ ->
        context.model.extraStore[bottomAxisLabelKey][x.toInt()]
    }
    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            columnSeries { series(5, 6, 5, 2, 11, 8, 5) }
            lineSeries { series(4, 5, 3, 1, 10, 7, 4) }
            extras { it[bottomAxisLabelKey] = data }
        }
    }
    CartesianChartHost(
        rememberCartesianChart(
            rememberColumnCartesianLayer(
                ColumnCartesianLayer.ColumnProvider.series(
                    rememberLineComponent(fill = fill(UserPrimaryColor), thickness = 16.dp)
                )
            ),
            rememberLineCartesianLayer(
                LineCartesianLayer.LineProvider.series(
                    LineCartesianLayer.Line(LineCartesianLayer.LineFill.single(fill(
                        Gray
                    )))
                )
            ),
            startAxis = VerticalAxis.rememberStart(),
            bottomAxis = HorizontalAxis.rememberBottom(
                itemPlacer = remember {
                    HorizontalAxis.ItemPlacer.segmented()
                },
                valueFormatter = bottomAxisValueFormatter
            ),
        ),
        modelProducer,
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(Dimens.Small)
    )
}