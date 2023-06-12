package com.presta.customer.ui.components.rootBottomStack

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.presta.customer.network.payments.data.PaymentTypes
import com.presta.customer.ui.components.profile.DefaultProfileComponent
import com.presta.customer.ui.components.profile.ProfileComponent
import com.presta.customer.ui.components.rootLoans.DefaultRootLoansComponent
import com.presta.customer.ui.components.rootSavings.DefaultRootSavingsComponent
import com.presta.customer.ui.components.rootSavings.RootSavingsComponent
import com.presta.customer.ui.components.sign.DefaultSignComponent
import com.presta.customer.ui.components.sign.SignComponent
import com.presta.customer.ui.components.rootLoans.RootLoansComponent
import com.presta.customer.prestaDispatchers


class DefaultRootBottomComponent(
    componentContext: ComponentContext,
    val storeFactory: StoreFactory,
    private val logoutToSplash: () -> Unit,
    private val gotoAllTransactions: () -> Unit,
    private val gotoPayLoans: () -> Unit,
    private val gotoPayRegistrationFees: (correlationId: String, amount: Double) -> Unit,
    private val processTransaction: (
        correlationId: String,
        amount: Double,
        mode: PaymentTypes
    ) -> Unit,
) : RootBottomComponent, ComponentContext by componentContext {

    private val navigationBottomStackNavigation = StackNavigation<ConfigBottom>()

    private val _childStackBottom =
        childStack(
            source = navigationBottomStackNavigation,
            initialConfiguration = ConfigBottom.Profile,
            handleBackButton = true,
            childFactory = ::createChildBottom,
            key = "authStack"
        )

    override val childStackBottom: Value<ChildStack<*, RootBottomComponent.ChildBottom>> = _childStackBottom
    
    private fun createChildBottom(config: ConfigBottom, componentContext: ComponentContext): RootBottomComponent.ChildBottom =
        when (config) {
            is ConfigBottom.Profile -> RootBottomComponent.ChildBottom.ProfileChild(profileComponent(componentContext))
            is ConfigBottom.RootLoans -> RootBottomComponent.ChildBottom.RootLoansChild(rootLoansComponent(componentContext))
            is ConfigBottom.RootSavings -> RootBottomComponent.ChildBottom.RootSavingsChild(rootSavingsComponent(componentContext))
            is ConfigBottom.Sign -> RootBottomComponent.ChildBottom.SignChild(signComponent(componentContext))
        }

    private fun profileComponent(componentContext: ComponentContext): ProfileComponent =
        DefaultProfileComponent(
            componentContext = componentContext,
            storeFactory = storeFactory,
            mainContext = prestaDispatchers.main,
            gotoAllTransactions = {
                gotoAllTransactions()
            },
            logoutToSplash = {
                logoutToSplash()
            },
            gotoSavings = {
                navigationBottomStackNavigation.bringToFront(ConfigBottom.RootSavings)
            },
            gotoLoans = {
                navigationBottomStackNavigation.bringToFront(ConfigBottom.RootLoans)
            },
            gotoPayLoans = {
                gotoPayLoans()
            },
            gotoStatement = {

            },
            onConfirmClicked = { correlationId, amount ->
                gotoPayRegistrationFees(correlationId, amount)
            }
        )

    private fun rootLoansComponent(componentContext: ComponentContext): RootLoansComponent =
        DefaultRootLoansComponent(
            componentContext = componentContext,
            storeFactory = storeFactory,
            pop = {
                navigationBottomStackNavigation.pop()
            },
            navigateToProfile = {
                navigationBottomStackNavigation.bringToFront(ConfigBottom.Profile)
            }
        )

    private fun rootSavingsComponent(componentContext: ComponentContext): RootSavingsComponent =
        DefaultRootSavingsComponent(
            componentContext = componentContext,
            storeFactory = storeFactory,
            pop = {
                navigationBottomStackNavigation.pop()
            },
            processTransaction = { correlationId, amount, mode ->
                processTransaction(correlationId, amount, mode)
            }
        )
    private fun signComponent(componentContext: ComponentContext): SignComponent =
        DefaultSignComponent(
            componentContext = componentContext,
            onSelected = {

            }
        )

    override fun onProfileTabClicked() {
        navigationBottomStackNavigation.bringToFront(ConfigBottom.Profile)
    }

    override fun onLoanTabClicked() {
        navigationBottomStackNavigation.bringToFront(ConfigBottom.RootLoans)
    }

    override fun onSavingsTabClicked() {
        navigationBottomStackNavigation.bringToFront(ConfigBottom.RootSavings)
    }

    override fun onSignTabClicked() {
        navigationBottomStackNavigation.bringToFront(ConfigBottom.Sign)
    }

    private sealed class ConfigBottom : Parcelable {
        @Parcelize
        object Profile : ConfigBottom()
        @Parcelize
        object RootLoans : ConfigBottom()
        @Parcelize
        object RootSavings : ConfigBottom()
        @Parcelize
        object Sign : ConfigBottom()
    }
}