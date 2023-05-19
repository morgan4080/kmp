package components.splash

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.presta.customer.MR

class DefaultSplashComponent(
    componentContext: ComponentContext
): SplashComponent, ComponentContext by componentContext {
    override val model: Value<SplashComponent.Model> =
        MutableValue(SplashComponent.Model(
            logo = MR.images.user_education_1
        ))
}