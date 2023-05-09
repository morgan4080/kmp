package components.profile

import com.arkivanov.decompose.value.Value

interface ProfileComponent {
    val model: Value<Model>

    fun onLoanSelected()

    data class Model(
        val items: List<String>,
    )
}