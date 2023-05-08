package components.welcome

import com.arkivanov.decompose.value.Value

interface WelcomeComponent {
    val model: Value<Model>

    fun onGetStarted()

    data class Model(
        val items: List<String>,
    )
}