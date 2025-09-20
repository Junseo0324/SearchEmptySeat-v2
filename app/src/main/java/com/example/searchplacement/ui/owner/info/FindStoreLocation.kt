package com.example.searchplacement.ui.owner.info

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.searchplacement.ui.utils.AddressWebViewDialog
import com.example.searchplacement.ui.theme.ButtonMainColor

@Composable
fun StoreLocationInput(
    address: String,
    addressDetail: String,
    onAddressChange: (String) -> Unit,
    onAddressDetailChange: (String) -> Unit,
    url: String
) {
    var showDialog by remember { mutableStateOf(false) }

    Column {
        OutlinedTextField(
            value = address,
            onValueChange = {},
            label = { Text("매장 주소(검색)") },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .clickable { showDialog = true }
        )
        Button(
            onClick = { showDialog = true },
            modifier = Modifier.padding(bottom = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ButtonMainColor)
        ) {
            Text("주소 검색")
        }
        OutlinedTextField(
            value = addressDetail,
            onValueChange = { onAddressDetailChange(it) },
            label = { Text("상세 주소") },
            modifier = Modifier.fillMaxWidth()
        )

        AddressWebViewDialog(
            showDialog = showDialog,
            onDismiss = { showDialog = false },
            onAddressSelected = {
                onAddressChange(it)
                showDialog = false
            },
            url = url
        )
    }
}
