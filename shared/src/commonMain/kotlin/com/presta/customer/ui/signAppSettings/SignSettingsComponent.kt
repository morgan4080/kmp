package com.presta.customer.ui.signAppSettings

import com.arkivanov.decompose.value.Value

interface SignSettingsComponent {
    val model: Value<Model>

    fun onSelected(item: String)

    data class Model(
        val items: List<String>,
    )
}