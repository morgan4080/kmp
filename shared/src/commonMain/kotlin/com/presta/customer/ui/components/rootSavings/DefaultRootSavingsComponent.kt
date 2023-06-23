package com.presta.customer.ui.components.rootSavings

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
import com.presta.customer.ui.components.savings.DefaultSavingsComponent
import com.presta.customer.ui.components.savings.SavingsComponent
import com.presta.customer.ui.components.savingsTransactionHistory.DefaultSavingsTransactionHistoryComponent
import com.presta.customer.ui.components.savingsTransactionHistory.SavingsTransactionHistoryComponent

class DefaultRootSavingsComponent(
    componentContext: ComponentContext,
    val storeFactory: StoreFactory,
    private val pop: () -> Unit = {},
    private val processTransaction: (
        correlationId: String,
        amount: Double,
        mode: PaymentTypes
    ) -> Unit,
): RootSavingsComponent, ComponentContext by componentContext {
    private val savingsNavigation = StackNavigation<ConfigSavings>()

    private val _childSavingsStack =
        childStack(
            source = savingsNavigation,
            initialConfiguration = ConfigSavings.SavingsHome,
            handleBackButton = true,
            childFactory = ::createSavingsChild,
            key = "applySavingsStack"
        )

    override val childSavingsStack: Value<ChildStack<*, RootSavingsComponent.ChildSavings>> = _childSavingsStack

    private fun createSavingsChild(config: ConfigSavings, componentContext: ComponentContext): RootSavingsComponent.ChildSavings =
        when (config) {
            is ConfigSavings.SavingsHome -> RootSavingsComponent.ChildSavings.SavingsHomeChild(
                savingsHomeComponent(componentContext)
            )
            is ConfigSavings.AddSavings -> RootSavingsComponent.ChildSavings.AddSavingsChild(
                addSavingsComponent(componentContext, config)
            )
            is ConfigSavings.SavingsTransactionHistory -> RootSavingsComponent.ChildSavings.TransactionHistoryChild(
                savingsTransactionHistoryComponent(componentContext)
            )

        }

    private fun savingsHomeComponent(componentContext: ComponentContext): SavingsComponent =
        DefaultSavingsComponent(
            componentContext = componentContext,
            storeFactory = storeFactory,
            mainContext = prestaDispatchers.main,
            onPop = {
                pop()
            },
            onAddSavingsClicked = { sharePrice ->
                savingsNavigation.push(ConfigSavings.AddSavings(sharePrice))
            },
            onSeeAlClicked = {
                savingsNavigation.push(ConfigSavings.SavingsTransactionHistory)
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
                savingsNavigation.pop()
            }
        )

    private fun savingsTransactionHistoryComponent(componentContext: ComponentContext): SavingsTransactionHistoryComponent =
        DefaultSavingsTransactionHistoryComponent(
            componentContext = componentContext
        )


    private sealed class ConfigSavings : Parcelable {
        @Parcelize
        object SavingsHome: ConfigSavings()

        @Parcelize
        data class AddSavings(val sharePrice: Double): ConfigSavings()

        @Parcelize
        object SavingsTransactionHistory: ConfigSavings()

    }

    init {
        lifecycle.subscribe(
            object : Lifecycle.Callbacks {
                override fun onResume() {
                    super.onResume()
                    savingsNavigation.replaceAll(ConfigSavings.SavingsHome)
                }
            }
        )
    }
}