package com.presta.customer.ui.components.splash

import com.arkivanov.decompose.value.Value
import com.presta.customer.organisation.Organisation
interface SplashComponent {
    val model: Value<Model>
    fun onSignUpClicked()
    fun onSignInClicked()
    data class Model(
        val organisation: Organisation
    )
}