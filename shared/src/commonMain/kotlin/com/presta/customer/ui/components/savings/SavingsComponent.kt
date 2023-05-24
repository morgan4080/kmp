package com.presta.customer.ui.components.savings

import com.arkivanov.decompose.value.Value

interface SavingsComponent {
    val model: Value<Model>

    fun onSelected(item: String)

    data class Model(
        val items: List<String>,
    )
}