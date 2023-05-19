package components.welcome

import com.arkivanov.decompose.value.Value
import components.countries.Country
import components.root.DefaultRootComponent
import dev.icerock.moko.resources.ImageResource

data class UserEducationScreens(val label: String, val title: String, val imageDark: ImageResource, val imageLight: ImageResource)
interface WelcomeComponent {
    val model: Value<Model>

    fun onGetStarted(country: Country, onBoardingContext: DefaultRootComponent.OnBoardingContext)

    data class Model(
        val items: List<UserEducationScreens>,
        val onBoardingContext: DefaultRootComponent.OnBoardingContext
    )
}