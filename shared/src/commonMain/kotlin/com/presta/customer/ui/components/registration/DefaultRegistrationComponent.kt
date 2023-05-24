package com.presta.customer.ui.components.registration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.store.StoreFactory

class DefaultRegistrationComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
): RegistrationComponent, ComponentContext by componentContext {

}