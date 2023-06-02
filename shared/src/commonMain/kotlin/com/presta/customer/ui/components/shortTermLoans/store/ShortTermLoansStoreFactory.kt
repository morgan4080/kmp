package com.presta.customer.ui.components.shortTermLoans.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.presta.customer.network.shortTermLoans.data.ShortTermLoansRepository
import com.presta.customer.network.shortTermLoans.model.PrestaShortTermProductsListResponse
import com.presta.customer.network.shortTermLoans.model.PrestaShortTermTopUpListResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import prestaDispatchers

class ShortTermLoansStoreFactory(
    private val storeFactory: StoreFactory
) : KoinComponent {
    private val shortTermLoansRepository by inject<ShortTermLoansRepository>()

    fun create(): ShortTermLoansStore =
        object : ShortTermLoansStore,
            Store<ShortTermLoansStore.Intent, ShortTermLoansStore.State, Nothing> by storeFactory.create(
                name = "ShortTermLoansStore",
                initialState = ShortTermLoansStore.State(),
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) {}

    private sealed class Msg {
        data class ShortTermLoansLoading(val isLoading: Boolean = true) : Msg()
        data class ShortTermLoansProductsListLoaded(val shortTermLoansProductList: List<PrestaShortTermProductsListResponse> = listOf()) : Msg()
        data class ShortTermLoanProductsByIdLoaded(val shortTermLoansProductById : PrestaShortTermProductsListResponse): Msg()
        data class ShortTermTopUpListLoaded(val shortTermTopUpList: PrestaShortTermTopUpListResponse) : Msg()

        data class ShortTermLoansFailed(val error: String?) : Msg()
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<ShortTermLoansStore.Intent, Unit, ShortTermLoansStore.State, Msg, Nothing>(
            prestaDispatchers.main
        ) {
        override fun executeAction(action: Unit, getState: () -> ShortTermLoansStore.State) {

        }

        override fun executeIntent(
            intent: ShortTermLoansStore.Intent, getState: () -> ShortTermLoansStore.State
        ): Unit =
            when (intent) {
                is ShortTermLoansStore.Intent.GetPrestaShortTermProductList -> getShortTermProductList(
                    token = intent.token,
                    refId = intent.refId
                )
                is ShortTermLoansStore.Intent.GetPrestaShortTermTopUpList -> getShortTermTopUpList(
                    token = intent.token,
                    session_id = intent.session_id,
                    refId = intent.refId
                )

                is ShortTermLoansStore.Intent.GetPrestaShortTermProductById -> getShortTermLoanProductsById(
                    token = intent.token,
                    loanId = intent.loanId
                )

            }
        //Get ShortTermLoansProducts
        private var getShortTermProductListJob: Job? = null

        private fun getShortTermProductList(
            token: String,
            refId: String
        ) {
            if (getShortTermProductListJob?.isActive == true) return

            dispatch(Msg.ShortTermLoansLoading())

            getShortTermProductListJob = scope.launch {
                shortTermLoansRepository.getShortTermProductListData(
                    token = token,
                    memberRefId = refId
                ).onSuccess { response ->
                    println(":::::::::getShortTermProductListData")
                    println(response)
                    dispatch(Msg.ShortTermLoansProductsListLoaded(response))
                }.onFailure { e ->
                    dispatch(Msg.ShortTermLoansFailed(e.message))
                }

                dispatch(Msg.ShortTermLoansLoading(false))
            }
        }

        //GetShortTermLoansProductById

        private var getShortTermLoanProductBYIdJob: Job? = null

        private fun getShortTermLoanProductsById(
            token: String,
            loanId:String
        ) {
            if (getShortTermLoanProductBYIdJob?.isActive == true) return

            dispatch(Msg.ShortTermLoansLoading())

            getShortTermLoanProductBYIdJob= scope.launch {
                shortTermLoansRepository.getShortTermProductLoanById(
                    token = token,
                    loanId = loanId
                ).onSuccess { response ->
                    println(":::::::::getShortTermProductListByIdData")
                    println(response)
                    dispatch(Msg.ShortTermLoanProductsByIdLoaded(response))
                }.onFailure { e ->
                    dispatch(Msg.ShortTermLoansFailed(e.message))
                }

                dispatch(Msg.ShortTermLoansLoading(false))
            }
        }

        //Get Short  termTop Up list

        private var getShortTermTopUpListJob: Job? = null
        private fun getShortTermTopUpList(
            token: String,
            session_id:String,
            refId: String

        ) {
            if (getShortTermTopUpListJob?.isActive == true) return

            dispatch(Msg.ShortTermLoansLoading())

            getShortTermTopUpListJob = scope.launch {
                shortTermLoansRepository.getShortTermTopUpListData(
                    token = token,
                    session_id = session_id,
                    memberRefId = refId
                ).onSuccess { response ->
                    println(":::::::::getShortTermToPUpListData")
                    println(response)
                    dispatch(Msg.ShortTermTopUpListLoaded(response))
                }.onFailure { e ->
                    dispatch(Msg.ShortTermLoansFailed(e.message))
                    //Test
                    println("An error occured ")
                }

                dispatch(Msg.ShortTermLoansLoading(false))
            }
        }
    }
    private object ReducerImpl : Reducer<ShortTermLoansStore.State, Msg> {
        override fun ShortTermLoansStore.State.reduce(msg: Msg): ShortTermLoansStore.State =
            when (msg) {
                is Msg.ShortTermLoansLoading -> copy(isLoading = msg.isLoading)
                is Msg.ShortTermLoansFailed -> copy(error = msg.error)
                is Msg.ShortTermLoansProductsListLoaded -> copy(prestaShortTermProductList = msg.shortTermLoansProductList)
                is Msg.ShortTermTopUpListLoaded->copy(prestaShortTermTopUpList=msg.shortTermTopUpList)
                is Msg.ShortTermLoanProductsByIdLoaded->copy(prestaShortTermLoanProductById=msg.shortTermLoansProductById)
            }
    }
}