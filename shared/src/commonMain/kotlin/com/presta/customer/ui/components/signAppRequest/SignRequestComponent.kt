package com.presta.customer.ui.components.signAppRequest

import com.arkivanov.decompose.value.Value

interface SignRequestComponent {
    val model: Value<Model>

    fun onSelected(item: String)

    data class Model(
        val items: List<String>,
    )
}