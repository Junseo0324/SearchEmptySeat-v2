package com.example.searchplacement.ui.utils

import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import dagger.hilt.android.internal.managers.FragmentComponentManager.findActivity

@Composable
fun AddressWebViewDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onAddressSelected: (String) -> Unit,
    url: String
) {
    if (showDialog) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Surface(
                tonalElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
                    .padding(5.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    AndroidView(
                        factory = { context ->
                            val activity = findActivity(context)
                            if (activity != null) {
                                WebView(activity).apply {
                                    settings.javaScriptEnabled = true
                                    settings.javaScriptCanOpenWindowsAutomatically = true
                                    settings.domStorageEnabled = true
                                    webChromeClient = WebChromeClient()

                                    addJavascriptInterface(object {
                                        @JavascriptInterface
                                        fun processDATA(address: String, sigungu: String) {
                                            onAddressSelected(address)
                                            onDismiss()
                                        }
                                    }, "Android")

                                    loadUrl("$url/utils/PostcodeMap.html")
                                }
                            } else {
                                // fallback: 그래도 에러 방지
                                WebView(context)
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
