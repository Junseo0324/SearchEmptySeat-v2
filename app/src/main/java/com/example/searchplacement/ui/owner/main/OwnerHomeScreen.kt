package com.example.searchplacement.ui.owner.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.searchplacement.ui.theme.Dimens
import com.example.searchplacement.viewmodel.OwnerHomeViewModel

@Composable
fun OwnerHomeScreen(storeId: Long){
    val ownerHomeViewModel: OwnerHomeViewModel = hiltViewModel()
    val stores = ownerHomeViewModel.store.collectAsState().value

    LaunchedEffect(Unit) {
        ownerHomeViewModel.getStoreData(storeId)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.Medium)
    ) {
        Text(text =stores?.storeName ?: "", fontSize = 25.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(20.dp))

    }
}