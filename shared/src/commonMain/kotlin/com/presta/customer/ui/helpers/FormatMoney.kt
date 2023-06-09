package com.presta.customer.ui.helpers

import kotlin.math.roundToInt

val replacementPattern = Regex("\\B(?=(\\d{3})+(?!\\d))")

fun formatMoney(money: Double?): String {
    if (money == null) return "0"
    val roundOff = (money * 100.0).roundToInt().toDouble() / 100.0
    return roundOff.toString().replace(regex = replacementPattern, replacement = ",")
}