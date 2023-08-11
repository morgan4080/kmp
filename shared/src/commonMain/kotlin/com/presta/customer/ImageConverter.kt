package com.presta.customer


expect class ImageConverter() {
    fun decodeBase64ToBitmap(base64: String): Any?
}