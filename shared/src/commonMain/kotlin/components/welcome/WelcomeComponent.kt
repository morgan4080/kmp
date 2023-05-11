package components.welcome

import com.arkivanov.decompose.value.Value
import dev.icerock.moko.resources.ImageResource

data class UserEducationScreens(val label: String, val title: String, val image: String)
interface WelcomeComponent {
    val model: Value<Model>

    fun onGetStarted()

    data class Model(
        val items: List<UserEducationScreens>,
    )
}