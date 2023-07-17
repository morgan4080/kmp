package com.presta.customer.ui.components.favouriteGuarantors

import com.arkivanov.decompose.ComponentContext

class DefaultFavouriteGuarantorsComponent (
    componentContext: ComponentContext,
    private val onItemClicked: () -> Unit,
    private val onProductClicked: () -> Unit
): FavouriteGuarantorsComponent, ComponentContext by componentContext {
    override fun onBackNavClicked() {
        onItemClicked()
    }

    override fun onProductSelected() {
      onProductClicked()
    }
}