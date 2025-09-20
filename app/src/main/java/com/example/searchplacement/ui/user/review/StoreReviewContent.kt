package com.example.searchplacement.ui.user.review

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.searchplacement.ui.theme.AppTextStyle
import com.example.searchplacement.ui.theme.Dimens

@Composable
fun StoreReviewContent(
    onSubmit: (Float, String, List<Uri>) -> Unit
) {
    var rating by remember { mutableStateOf(4f) }
    var content by remember { mutableStateOf("") }
    var imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            if (imageUris.size < 5) {
                imageUris = imageUris + it
            }
        }
    }

    Column(Modifier.fillMaxWidth().padding(Dimens.Small)) {
        Text("별점: ${String.format("%.1f", rating)}")

        RatingBarView(
            rating = rating,
            onRatingChanged = { rating = it }
        )

        Spacer(Modifier.height(Dimens.Small))
        Text("방문 후기", style = AppTextStyle.Body)
        Spacer(Modifier.height(Dimens.Small))
        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("방문 후기를 입력해주세요") }
        )

        Spacer(Modifier.height(Dimens.Small))
        Text("사진 (${imageUris.size}/5)")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            repeat(5) { index ->
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray)
                        .clickable {
                            if (index >= imageUris.size) launcher.launch("image/*")
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
                        Icon(Icons.Default.Add, contentDescription = null)
                    }
                }
            }
        }

        if (imageUris.isNotEmpty()) {
            Button(
                onClick = { imageUris = emptyList() },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("사진 초기화")
            }
        }

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { onSubmit(rating, content, imageUris) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("리뷰 작성")
        }
    }
}
