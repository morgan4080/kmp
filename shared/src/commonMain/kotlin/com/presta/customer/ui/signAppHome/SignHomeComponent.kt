package com.presta.customer.ui.signAppHome

import com.arkivanov.decompose.value.Value

interface SignHomeComponent {
    val model: Value<Model>

    fun onSelected(item: String)

    data class Model(
        val items: List<String>,
    )
}