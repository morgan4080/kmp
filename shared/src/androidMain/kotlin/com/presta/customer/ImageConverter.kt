package com.presta.customer
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.ui.graphics.asImageBitmap

actual class ImageConverter {
   actual  fun decodeBase64ToBitmap(base64: String): Any? {
       val decodedBytes = Base64.decode(base64, Base64.DEFAULT)
       val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
       return bitmap.asImageBitmap()
    }

}


