package com.presta.customer
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.flow.MutableStateFlow

actual class ImageConverter {
   actual  fun decodeBase64ToBitmap(base64: String): MutableStateFlow<ImageBitmap?> {
       val bitmapData: MutableStateFlow<ImageBitmap?> = MutableStateFlow(null)
       val decodedBytes = Base64.decode(base64, Base64.DEFAULT)
       val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
       bitmapData.value = bitmap.asImageBitmap()

       return bitmapData
    }

}


