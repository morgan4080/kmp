package components.splash

import com.arkivanov.decompose.value.Value
import dev.icerock.moko.resources.ImageResource
import organisation.Organisation
interface SplashComponent {
    val model: Value<Model>
    fun onSignUpClicked()
    fun onSignInClicked()
    data class Model(
        val organisation: Organisation
    )
}