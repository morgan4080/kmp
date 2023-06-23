package com.presta.customer.ui.helpers

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

fun isOverdue(loanDate: String): Boolean {
    val todayInstant = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).toInstant(TimeZone.currentSystemDefault())
    val replacementPattern = Regex("\\s")
    val loanDateInstant = LocalDateTime.parse(loanDate.replace(regex = replacementPattern, replacement = "T")).toInstant(TimeZone.currentSystemDefault())
    return loanDateInstant.minus(todayInstant).isNegative()
}