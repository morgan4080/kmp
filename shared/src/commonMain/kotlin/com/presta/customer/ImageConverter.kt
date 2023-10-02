package com.presta.customer

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.coroutines.flow.MutableStateFlow


expect class ImageConverter() {
    fun decodeBase64ToBitmap(base64: String): MutableStateFlow<ImageBitmap?>
}