package components.auth

import com.arkivanov.decompose.ComponentContext

class DefaultAuthComponent(
    componentContext: ComponentContext,
): AuthComponent, ComponentContext by componentContext {

}