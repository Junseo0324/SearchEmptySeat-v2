package com.example.searchplacement.ui.owner.placement

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.searchplacement.ui.theme.ButtonMainColor


@Composable
fun StoreSizeSelectionScreen(onNext: (String) -> Unit) {
    val selectedSize = remember { mutableStateOf("") }
    val storeSizes = listOf("1~20 테이블", "20 ~ 40 테이블", "40개 이상")

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("매장 크기를 선택하세요", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 10.dp))
        HorizontalDivider()
        storeSizes.forEach { size ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { selectedSize.value = size }
                    .background(
                        if (selectedSize.value == size) Color.LightGray else Color.Transparent,
                        RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp)
            ) {
                RadioButton(
                    selected = selectedSize.value == size,
                    onClick = { selectedSize.value = size }
                )
                Text(text = size, modifier = Modifier.padding(start = 8.dp))
            }
        }

        Button(
            onClick = {
                val sizeIndex = storeSizes.indexOf(selectedSize.value) + 1
                onNext(sizeIndex.toString()) },
            enabled = selectedSize.value.isNotEmpty(),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp),
            colors = ButtonColors(
                containerColor = ButtonMainColor, contentColor = Color.Black,
                disabledContainerColor = Color.LightGray, disabledContentColor = Color.White
            )
        ) {
            Text("다음")
        }
    }
}

