package com.presta.customer

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.coroutines.flow.MutableStateFlow

actual class ImageConverter {
    actual fun decodeBase64ToBitmap(base64: String): MutableStateFlow<ImageBitmap?> {
        return MutableStateFlow(null)
    }

}