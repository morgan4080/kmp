package com.presta.customer.ui.components.applyLongTermLoan.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.presta.customer.network.longTermLoans.client.DetailsData
import com.presta.customer.network.longTermLoans.data.LongTermLoansRepository
import com.presta.customer.network.longTermLoans.model.ClientSettingsResponse
import com.presta.customer.network.longTermLoans.model.Guarantor
import com.presta.customer.network.longTermLoans.model.LongTermLoanRequestResponse
import com.presta.customer.network.longTermLoans.model.LongTermLoanResponse
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoanCategoriesResponse
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoanSubCategories
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoanSubCategoriesChildren
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoansProductResponse
import com.presta.customer.prestaDispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ApplyLongTermLoansStoreFactory(
    private val storeFactory: StoreFactory
) : KoinComponent {
    private val longTermLoansRepository by inject<LongTermLoansRepository>()

    fun create(): ApplyLongTermLoansStore =
        object : ApplyLongTermLoansStore,
            Store<ApplyLongTermLoansStore.Intent, ApplyLongTermLoansStore.State, Nothing> by storeFactory.create(
                name = "LongTermLoansStore",
                initialState = ApplyLongTermLoansStore.State(),
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) {}

    private sealed class Msg {
        data class LongTermLoansLoading(val isLoading: Boolean = true) : Msg()
        data class LongTermLoansLoaded(val longTermLoansLoaded: PrestaLongTermLoansProductResponse) :
            Msg()

        data class LongTermLoansBydLoaded(val longTermLoansByIdLoaded: LongTermLoanResponse) :
            Msg()

        data class LongTermLoansCategoriesLoaded(val longTermLoansLoaded: List<PrestaLongTermLoanCategoriesResponse> = listOf()) :
            Msg()

        data class LongTermLoansSubCategoriesLoaded(val longTermLoansSubCategoryLoaded: List<PrestaLongTermLoanSubCategories> = listOf()) :
            Msg()

        data class LongTermLoansSubCategoriesChildrenLoaded(val longTermLoansSubCategoryChildrenLoaded: List<PrestaLongTermLoanSubCategoriesChildren> = listOf()) :
            Msg()

        data class ClientSettingsLoaded(val clientSettingsLoaded: ClientSettingsResponse) :
            Msg()

        data class LongTermLoanRequestLoaded(val longTermLoanRequestResponse: LongTermLoanRequestResponse) :
            Msg()

        data class LongTermLoansFailed(val error: String?) : Msg()
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<ApplyLongTermLoansStore.Intent, Unit, ApplyLongTermLoansStore.State, Msg, Nothing>(
            prestaDispatchers.main
        ) {
        override fun executeAction(action: Unit, getState: () -> ApplyLongTermLoansStore.State) {

        }

        override fun executeIntent(
            intent: ApplyLongTermLoansStore.Intent, getState: () -> ApplyLongTermLoansStore.State
        ): Unit =
            when (intent) {

                is ApplyLongTermLoansStore.Intent.GetLongTermLoansProducts -> getPrestalongTermLoanProducts(
                    token = intent.token

                )

                is ApplyLongTermLoansStore.Intent.GetPrestaLongTermProductById -> getLongTermLoanProductsById(
                    token = intent.token,
                    loanRefId = intent.loanRefId
                )

                is ApplyLongTermLoansStore.Intent.GetLongTermLoansProductsCategories -> getPrestalongTermLoanProductsCategories(
                    token = intent.token,
                )

                is ApplyLongTermLoansStore.Intent.GetLongTermLoansProductsSubCategories -> getPrestalongTermLoanProductsSubCategories(
                    token = intent.token,
                    parent = intent.parent
                )

                is ApplyLongTermLoansStore.Intent.GetLongTermLoansProductsSubCategoriesChildren -> getPrestalongTermLoanProductsSubCategoriesChildren(
                    token = intent.token,
                    parent = intent.parent,
                    child = intent.child
                )

                is ApplyLongTermLoansStore.Intent.GetClientSettings -> getClientSettings(
                    token = intent.token

                )

                is ApplyLongTermLoansStore.Intent.RequestLongTermLoan -> requestLongTermLoan(
                    token = intent.token,
                    details = intent.details,
                    loanProductName = intent.loanProductName,
                    loanProductRefId = intent.loanProductRefId,
                    selfCommitment = intent.selfCommitment,
                    loanAmount = intent.loanAmount,
                    memberRefId = intent.memberRefId,
                    memberNumber = intent.memberNumber,
                    witnessRefId = intent.witnessRefId,
                    guarantorList = intent.guarantorList,
                )
            }

        private var getPrestaLongTermLoansProductsJob: Job? = null

        private fun getPrestalongTermLoanProducts(
            token: String
        ) {
            if (getPrestaLongTermLoansProductsJob?.isActive == true) return

            dispatch(Msg.LongTermLoansLoading())

            getPrestaLongTermLoansProductsJob = scope.launch {
                longTermLoansRepository.getLonTermLoansData(
                    token = token
                ).onSuccess { response ->
                    dispatch(Msg.LongTermLoansLoaded(response))
                    println("Load Success")

                }.onFailure { e ->
                    dispatch(Msg.LongTermLoansFailed(e.message))
                    println("Load failed")
                }

                dispatch(Msg.LongTermLoansLoading(false))
            }
        }

        private var getLongTermLoanProductBYIdJob: Job? = null

        private fun getLongTermLoanProductsById(
            token: String,
            loanRefId: String
        ) {
            if (getLongTermLoanProductBYIdJob?.isActive == true) return

            dispatch(Msg.LongTermLoansLoading())

            getLongTermLoanProductBYIdJob = scope.launch {
                longTermLoansRepository.getLongTermProductLoanById(
                    token = token,
                    loanRefId = loanRefId
                ).onSuccess { response ->
                    dispatch(Msg.LongTermLoansBydLoaded(response))
                    println(":::::::::getLongTermProductListByIdData")
                    println(response)

                }.onFailure { e ->
                    dispatch(Msg.LongTermLoansFailed(e.message))
                }

                dispatch(Msg.LongTermLoansLoading(false))
            }
        }

        private var getPrestaLongTermLoansProductsCategoriesJob: Job? = null

        private fun getPrestalongTermLoanProductsCategories(
            token: String
        ) {
            if (getPrestaLongTermLoansProductsCategoriesJob?.isActive == true) return

            dispatch(Msg.LongTermLoansLoading())

            getPrestaLongTermLoansProductsCategoriesJob = scope.launch {
                longTermLoansRepository.getLongTermLoansCategoriesData(
                    token = token
                ).onSuccess { response ->
                    dispatch(Msg.LongTermLoansCategoriesLoaded(response))
                    println("Load Categories Success")

                }.onFailure { e ->
                    dispatch(Msg.LongTermLoansFailed(e.message))
                    println("Load Categories  failed")
                }

                dispatch(Msg.LongTermLoansLoading(false))
            }
        }

        private var getPrestaLongTermLoansProductsSubCategoriesJob: Job? = null

        private fun getPrestalongTermLoanProductsSubCategories(
            token: String,
            parent: String
        ) {
            if (getPrestaLongTermLoansProductsSubCategoriesJob?.isActive == true) return

            dispatch(Msg.LongTermLoansLoading())

            getPrestaLongTermLoansProductsSubCategoriesJob = scope.launch {
                longTermLoansRepository.getLongTermLoanSubCategoriesData(
                    token = token,
                    parent = parent
                ).onSuccess { response ->
                    dispatch(Msg.LongTermLoansSubCategoriesLoaded(response))
                    println("Load SubCategories Success")

                }.onFailure { e ->
                    dispatch(Msg.LongTermLoansFailed(e.message))
                    println("Load SubCategories  failed")
                }

                dispatch(Msg.LongTermLoansLoading(false))
            }
        }

        private var getPrestaLongTermLoansProductsSubCategoriesChildrenJob: Job? = null

        private fun getPrestalongTermLoanProductsSubCategoriesChildren(
            token: String,
            parent: String,
            child: String
        ) {
            if (getPrestaLongTermLoansProductsSubCategoriesChildrenJob?.isActive == true) return

            dispatch(Msg.LongTermLoansLoading())
            getPrestaLongTermLoansProductsSubCategoriesChildrenJob = scope.launch {
                longTermLoansRepository.getLongTermLoanSubCategoriesChildrenData(
                    token = token,
                    parent = parent,
                    child = child
                ).onSuccess { response ->
                    dispatch(Msg.LongTermLoansSubCategoriesChildrenLoaded(response))
                    println("Load SubCategoriesChildren Success")

                }.onFailure { e ->
                    dispatch(Msg.LongTermLoansFailed(e.message))
                    println("Load SubCategoriesChildren  failed")
                }
                dispatch(Msg.LongTermLoansLoading(false))
            }
        }

        private var getPrestaClientSettingsJob: Job? = null
        private fun getClientSettings(
            token: String
        ) {
            if (getPrestaClientSettingsJob?.isActive == true) return

            dispatch(Msg.LongTermLoansLoading())

            getPrestaClientSettingsJob = scope.launch {
                longTermLoansRepository.getClientSettingsData(
                    token = token
                ).onSuccess { response ->
                    dispatch(Msg.ClientSettingsLoaded(response))
                    println("Load Success")

                }.onFailure { e ->
                    dispatch(Msg.LongTermLoansFailed(e.message))
                    println("Load failed")
                }

                dispatch(Msg.LongTermLoansLoading(false))
            }
        }

        private var requestLongTermLoanJob: Job? = null
        private fun requestLongTermLoan(
            token: String,
            details: DetailsData,
            loanProductName: String,
            loanProductRefId: String,
            selfCommitment: Double,
            loanAmount: Double,
            memberRefId: String,
            memberNumber: String,
            witnessRefId: String?,
            guarantorList: ArrayList<Guarantor>,
        ) {
            if (requestLongTermLoanJob?.isActive == true) return
            dispatch(Msg.LongTermLoansLoading())
            requestLongTermLoanJob = scope.launch {
                longTermLoansRepository.requestLongTermLoan(
                    token,
                    details,
                    loanProductName,
                    loanProductRefId,
                    selfCommitment,
                    loanAmount,
                    memberRefId,
                    memberNumber,
                    witnessRefId,
                    guarantorList,
                ).onSuccess { response ->
                    dispatch(Msg.LongTermLoanRequestLoaded(response))
                    println("LongTerm Loan Request  Success Success")
                }.onFailure { e ->
                    dispatch(Msg.LongTermLoansFailed(e.message))
                }
                dispatch(Msg.LongTermLoansLoading(false))
            }
        }
    }
    private object ReducerImpl :
        Reducer<ApplyLongTermLoansStore.State, Msg> {
        override fun ApplyLongTermLoansStore.State.reduce(msg: Msg): ApplyLongTermLoansStore.State =
            when (msg) {
                is Msg.LongTermLoansLoading -> copy(isLoading = msg.isLoading)
                is Msg.LongTermLoansFailed -> copy(error = msg.error)
                is Msg.LongTermLoansLoaded -> copy(prestaLongTermLoanProducts = msg.longTermLoansLoaded)
                is Msg.LongTermLoansBydLoaded -> copy(prestaLongTermLoanProductById = msg.longTermLoansByIdLoaded)
                is Msg.LongTermLoansCategoriesLoaded -> copy(prestaLongTermLoanProductsCategories = msg.longTermLoansLoaded)
                is Msg.LongTermLoansSubCategoriesLoaded -> copy(
                    prestaLongTermLoanProductsSubCategories = msg.longTermLoansSubCategoryLoaded
                )
                is Msg.LongTermLoansSubCategoriesChildrenLoaded -> copy(
                    prestaLongTermLoanProductsSubCategoriesChildren = msg.longTermLoansSubCategoryChildrenLoaded
                )
                is Msg.ClientSettingsLoaded -> copy(prestaClientSettings = msg.clientSettingsLoaded)
                is Msg.LongTermLoanRequestLoaded -> copy(prestaLongTermLoanRequestData = msg.longTermLoanRequestResponse)
            }
    }

}