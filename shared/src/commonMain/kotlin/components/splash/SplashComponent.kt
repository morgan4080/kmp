package components.splash

import com.arkivanov.decompose.value.Value
import dev.icerock.moko.resources.ImageResource

interface SplashComponent {
    val model: Value<Model>
    data class Model(
        val logo: ImageResource
    )
}