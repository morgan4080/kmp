package com.presta.customer.ui.helpers

import kotlinx.datetime.LocalDateTime

fun formatDate(date: String): String {
    val replacementPattern = Regex("\\s")
    val localDateTime = LocalDateTime.parse(date.replace(regex = replacementPattern, replacement = "T"))
    return "${localDateTime.dayOfMonth} ${localDateTime.month}, ${localDateTime.time}"
}