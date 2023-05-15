package components.welcome

import com.arkivanov.decompose.value.Value
import components.countries.Country
import dev.icerock.moko.resources.ImageResource

data class UserEducationScreens(val label: String, val title: String, val image: ImageResource)
interface WelcomeComponent {
    val model: Value<Model>

    fun onGetStarted(country: Country)

    data class Model(
        val items: List<UserEducationScreens>,
    )
}