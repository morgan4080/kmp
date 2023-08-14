package com.presta.customer.ui.components.rootSignHome

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.presta.customer.prestaDispatchers
import com.presta.customer.ui.components.signAppHome.DefaultSignHomeComponent
import com.presta.customer.ui.components.signAppHome.SignHomeComponent

class DefaultRootSignHomeComponent(
    componentContext: ComponentContext,
    val storeFactory: StoreFactory,
    private val pop: () -> Unit = {},
    private val onApplyLoanClicked: () -> Unit = {},
    private val onLoanRequestsListClicked: () -> Unit = {},
    private val onGuarantorshipRequestsClicked: () -> Unit = {},
    private val onFavouriteGuarantorsClicked: () -> Unit = {},
    private val onWitnessRequestClicked: () -> Unit = {}
) : RootSignHomeComponent, ComponentContext by componentContext {
    private val signHomeNavigation = StackNavigation<ConfigSignHome>()

    private val _childSignHomeStack =
        childStack(
            source = signHomeNavigation,
            initialConfiguration = ConfigSignHome.SavingsHome,
            handleBackButton = true,
            childFactory = ::createSavingsChild,
            key = "signHomeStack"
        )

    override val childSignHomeStack: Value<ChildStack<*, RootSignHomeComponent.ChildHomeSign>> =
        _childSignHomeStack

    override fun applyLongTermLoan() {
        onApplyLoanClicked()
    }

    private fun createSavingsChild(
        config: ConfigSignHome,
        componentContext: ComponentContext
    ): RootSignHomeComponent.ChildHomeSign =
        when (config) {
            is ConfigSignHome.SavingsHome -> RootSignHomeComponent.ChildHomeSign.SignHomeChild(
                signHomeComponent(componentContext)
            )


        }

    private fun signHomeComponent(componentContext: ComponentContext): SignHomeComponent =
        DefaultSignHomeComponent(
            componentContext = componentContext,
            onApplyLoanClicked ={
                onApplyLoanClicked()
            },
            onGuarantorshipRequestsClicked = {
                onGuarantorshipRequestsClicked()
            },
            onFavouriteGuarantorsClicked = {
                onFavouriteGuarantorsClicked()

            },
            witnessRequestClicked = {
                onWitnessRequestClicked()
            },
            storeFactory = storeFactory,
            mainContext = prestaDispatchers.main,
            onGotoLoanRequestsClicked = {
                onLoanRequestsListClicked()

            }
        )
    private sealed class ConfigSignHome : Parcelable {
        @Parcelize
        object SavingsHome : ConfigSignHome()

    }

    init {
        lifecycle.subscribe(
            object : Lifecycle.Callbacks {
                override fun onResume() {
                    super.onResume()
                    signHomeNavigation.replaceAll(ConfigSignHome.SavingsHome)
                }
            }
        )
    }
}