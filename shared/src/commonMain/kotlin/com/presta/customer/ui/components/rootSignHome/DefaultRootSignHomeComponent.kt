package com.presta.customer.ui.components.rootSignHome

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.presta.customer.network.payments.data.PaymentTypes
import com.presta.customer.prestaDispatchers
import com.presta.customer.ui.components.addSavings.AddSavingsComponent
import com.presta.customer.ui.components.addSavings.DefaultAddSavingsComponent
import com.presta.customer.ui.components.applyLongTermLoan.ApplyLongTermLoanComponent
import com.presta.customer.ui.components.applyLongTermLoan.DefaultApplyLongtermLoanComponent
import com.presta.customer.ui.signAppHome.DefaultSignHomeComponent
import com.presta.customer.ui.signAppHome.SignHomeComponent

class DefaultRootSignHomeComponent(
    componentContext: ComponentContext,
    val storeFactory: StoreFactory,
    private val pop: () -> Unit = {},
    private val processTransaction: (
        correlationId: String,
        amount: Double,
        mode: PaymentTypes
    ) -> Unit,
): RootSignHomeComponent, ComponentContext by componentContext {
    private val signHomeNavigation = StackNavigation<ConfigSavings>()

    private val _childSignHomeStack =
        childStack(
            source = signHomeNavigation,
            initialConfiguration = ConfigSavings.SavingsHome,
            handleBackButton = true,
            childFactory = ::createSavingsChild,
            key = "signHomeStack"
        )

    override val childSignHomeStack: Value<ChildStack<*, RootSignHomeComponent.ChildHomeSign>> = _childSignHomeStack

    private fun createSavingsChild(config: ConfigSavings, componentContext: ComponentContext): RootSignHomeComponent.ChildHomeSign =
        when (config) {
            is ConfigSavings.SavingsHome -> RootSignHomeComponent.ChildHomeSign.SignHomeChild(
                signHomeComponent(componentContext)
            )
            is ConfigSavings.AddSavings -> RootSignHomeComponent.ChildHomeSign.AddSavingsChild(
                addSavingsComponent(componentContext, config)
            )
            is ConfigSavings.ApplyLongTermLoans -> RootSignHomeComponent.ChildHomeSign.ApplyLongTermLoansChild(
                applyLongTermLoanComponent(componentContext)
            )

        }

    private fun signHomeComponent(componentContext: ComponentContext): SignHomeComponent=
        DefaultSignHomeComponent(
            componentContext = componentContext,
            onItemClicked = {
                signHomeNavigation.push(ConfigSavings.ApplyLongTermLoans)
            }

        )

    private fun addSavingsComponent(componentContext: ComponentContext, config: ConfigSavings.AddSavings): AddSavingsComponent =
        DefaultAddSavingsComponent(
           componentContext = componentContext,
            storeFactory = storeFactory,
            mainContext = prestaDispatchers.main,
            sharePrice = config.sharePrice,
            onConfirmClicked = {correlationId, amount, mode ->
                processTransaction(correlationId, amount, mode)
            },
            onBackNavClicked = {
                signHomeNavigation.pop()
            }
        )

    private fun applyLongTermLoanComponent(componentContext: ComponentContext): ApplyLongTermLoanComponent =
        DefaultApplyLongtermLoanComponent(
            componentContext = componentContext,
            onItemClicked = {

            }

        )


    private sealed class ConfigSavings : Parcelable {
        @Parcelize
        object SavingsHome: ConfigSavings()

        @Parcelize
        data class AddSavings(val sharePrice: Double): ConfigSavings()

        @Parcelize
        object ApplyLongTermLoans: ConfigSavings()

    }

    init {
        lifecycle.subscribe(
            object : Lifecycle.Callbacks {
                override fun onResume() {
                    super.onResume()
                    signHomeNavigation.replaceAll(ConfigSavings.SavingsHome)
                }
            }
        )
    }
}