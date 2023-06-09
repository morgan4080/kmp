package com.presta.customer.ui.components.sign

import com.arkivanov.decompose.value.Value

interface SignComponent {
    val model: Value<Model>
    fun onAddSavingsSelected()
    fun onSeeALlSelected()

    data class Model(
        val items: List<String>,
    )
}