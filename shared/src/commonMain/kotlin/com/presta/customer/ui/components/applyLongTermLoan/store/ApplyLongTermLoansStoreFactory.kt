package com.presta.customer.ui.components.applyLongTermLoan.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.presta.customer.network.longTermLoans.client.DetailsData
import com.presta.customer.network.longTermLoans.data.LongTermLoansRepository
import com.presta.customer.network.longTermLoans.model.ActorType
import com.presta.customer.network.longTermLoans.model.ClientSettingsResponse
import com.presta.customer.network.longTermLoans.model.Guarantor
import com.presta.customer.network.longTermLoans.model.LongTermLoanRequestResponse
import com.presta.customer.network.longTermLoans.model.LongTermLoanResponse
import com.presta.customer.network.longTermLoans.model.PrestaGuarantorAcceptanceResponse
import com.presta.customer.network.longTermLoans.model.PrestaLoanByRefIdResponse
import com.presta.customer.network.longTermLoans.model.PrestaLoanRequestByRequestRefId
import com.presta.customer.network.longTermLoans.model.PrestaLoanRequestEligibility
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoanCategoriesResponse
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoanSubCategories
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoanSubCategoriesChildren
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoansProductResponse
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoansRequestsListResponse
import com.presta.customer.network.longTermLoans.model.PrestaZohoSignUrlResponse
import com.presta.customer.network.longTermLoans.model.favouriteGuarantor.PrestaFavouriteGuarantorResponse
import com.presta.customer.network.longTermLoans.model.guarantorResponse.PrestaGuarantorResponse
import com.presta.customer.network.longTermLoans.model.witnessRequests.PrestaWitnessRequestResponse
import com.presta.customer.network.signHome.model.PrestaSignUserDetailsResponse
import com.presta.customer.prestaDispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ApplyLongTermLoansStoreFactory(
    private val storeFactory: StoreFactory,
    private val loanRefId: String = "",
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

        data class GuarontorshipRequestsLoaded(val guarantorShipRequestsLoaded: List<PrestaGuarantorResponse> = listOf()) :
            Msg()

        data class LongTermLoanRequestByRefIdLoaded(val longTermLoanRequestByRefIdResponse: PrestaLoanByRefIdResponse) :
            Msg()

        data class GuarantorAcceptanceResponseLoaded(val guarantorAcceptanceResponse: PrestaGuarantorAcceptanceResponse) :
            Msg()
        data class LoanRequestEligibility(val loanRequestEligibility: PrestaLoanRequestEligibility) :
            Msg()
        data class SetExistingLoanRequest(val loanRefId: String) :
            Msg()

        data object ClearExisting: Msg()
        data object ClearExistingError: Msg()

        data class LoanByLoanRequestRefIdLoaded(val loanByLoanRequestRefId: PrestaLoanRequestByRequestRefId) :
            Msg()

        data class ZohoSignUrlLoaded(val zohoSignUrlResponse: PrestaZohoSignUrlResponse) :
            Msg()

        data class LongTermLoansRequestsListLoaded(val longTermLoansRequestsListResponse: PrestaLongTermLoansRequestsListResponse) :
            Msg()

        data class LongTermLoansRequestsFilteredListLoaded(val longTermLoansRequestsFilteredListResponse: PrestaLongTermLoansRequestsListResponse) :
            Msg()

        data class LongTermLoansRequestsSpecificProductLoaded(val longTermLoansRequestsSpecificProduct: PrestaLongTermLoansRequestsListResponse) :
            Msg()

        //Replaced  guarantor
        data class UpdatedGuarantorLoaded(val replacedGuarantorResponse: String) :
            Msg()

        data class WitnessRequestsLoaded(val witnessRequestsLoaded: List<PrestaWitnessRequestResponse> = listOf()) :
            Msg()

        data class WitnessAcceptanceStatusLoaded(val witnessAcceptanceResponseLoaded: PrestaWitnessRequestResponse) :
            Msg()

        data class FavouriteGuarantorLoaded(val favouriteGuarantorLoaded: List<PrestaFavouriteGuarantorResponse> = listOf()) :
            Msg()

        data class AddedFavouriteGuarantorLoaded(val addedfavouriteGuarantorLoaded: PrestaFavouriteGuarantorResponse) :
            Msg()

        data class FavouriteGuarantorDeleted(val favouriteGuarantorDeletedResponse: String) :
            Msg()

        data class LoanRequestDeleted(val loanRequestDeleted: String) :
            Msg()

        data class LoadedSignTenantByPhoneNumber(val signTenantByPhoneNumberResponse: PrestaSignUserDetailsResponse) :
            Msg()

        data class LongTermLoansFailed(val error: String?) : Msg()
        data object ClearSignUrl : Msg()
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<ApplyLongTermLoansStore.Intent, Unit, ApplyLongTermLoansStore.State, Msg, Nothing>(
            prestaDispatchers.main
        ) {
        override fun executeAction(action: Unit, getState: () -> ApplyLongTermLoansStore.State) {
            dispatch(Msg.SetExistingLoanRequest(loanRefId))
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

                is ApplyLongTermLoansStore.Intent.GetPrestaGuarantorshipRequests -> getprestaGuarontorsRequests(
                    token = intent.token,
                    memberRefId = intent.memberRefId
                )

                is ApplyLongTermLoansStore.Intent.GetPrestaLongTermLoanRequestByRefId -> getPrestaLongTermLoanRequestByRefId(
                    token = intent.token,
                    loanRequestRefId = intent.loanRequestRefId
                )

                is ApplyLongTermLoansStore.Intent.GetGuarantorAcceptanceStatus -> getGuarantorAcceptanceResponse(
                    token = intent.token,
                    guarantorshipRequestRefId = intent.guarantorshipRequestRefId,
                    isAccepted = intent.isAccepted

                )

                is ApplyLongTermLoansStore.Intent.GetPrestaLoanByLoanRequestRefId -> getLoanByLoanRequestRefId(
                    token = intent.token,
                    loanRequestRefId = intent.loanRequestRefId
                )

                is ApplyLongTermLoansStore.Intent.GetZohoSignUrl -> requestZohoSignUrl(
                    token = intent.token,
                    loanRequestRefId = intent.loanRequestRefId,
                    actorRefId = intent.actorRefId,
                    actorType = intent.actorType
                )

                is ApplyLongTermLoansStore.Intent.GetPrestaLongTermLoansRequestsList -> getprestaLongTermLoansRequestsList(
                    token = intent.token,
                    memberRefId = intent.memberRefId
                )

                is ApplyLongTermLoansStore.Intent.GetPrestaLongTermLoansRequestsFilteredList -> getprestaLongTermLoansRequestsFilteredList(
                    token = intent.token,
                    memberRefId = intent.memberRefId
                )

                is ApplyLongTermLoansStore.Intent.GetPrestaLongTermLoansRequestsSpecificProduct -> getprestaLongTermLoansRequestsSpecificProduct(
                    token = intent.token,
                    productRefId = intent.productRefId,
                    memberRefId = intent.memberRefId
                )

                is ApplyLongTermLoansStore.Intent.ReplaceLoanGuarantor -> replaceLoanGuarantor(
                    token = intent.token,
                    loanRequestRefId = intent.loanRequestRefId,
                    guarantorRefId = intent.guarantorRefId,
                    memberRefId = intent.memberRefId
                )

                is ApplyLongTermLoansStore.Intent.GetPrestaWitnessRequests -> getprestaWitnessRequests(
                    token = intent.token,
                    memberRefId = intent.memberRefId
                )

                is ApplyLongTermLoansStore.Intent.GetWitnessAcceptanceStatus -> getprestaWitnessAcceptanceStatus(
                    token = intent.token,
                    loanRequestRefId = intent.loanRequestRefId,
                    isAccepted = intent.isAccepted
                )

                is ApplyLongTermLoansStore.Intent.GetPrestaFavouriteGuarantor -> getprestaFavouriteGuarantor(
                    token = intent.token,
                    memberRefId = intent.memberRefId
                )

                is ApplyLongTermLoansStore.Intent.AddFavouriteGuarantor -> getAddedprestaFavouriteGuarantor(
                    token = intent.token,
                    memberRefId = intent.memberRefId,
                    guarantorRefId = intent.guarantorRefId
                )

                is ApplyLongTermLoansStore.Intent.DeleteFavouriteGuarantor -> deleteFavouriteGuarantor(
                    token = intent.token,
                    refId = intent.refId

                )

                is ApplyLongTermLoansStore.Intent.DeleteLoanRequest -> deleteLoanRequest(
                    token = intent.token,
                    loanRequestNumber = intent.loanRequestNumber
                )

                is ApplyLongTermLoansStore.Intent.LoadTenantByPhoneNumber -> loadPrestaTenantByPhoneNumber(
                    token = intent.token,
                    phoneNumber = intent.phoneNumber
                )

                is ApplyLongTermLoansStore.Intent.CheckLoanRequestEligibility -> getLoanRequestEligibilityByMemberRefId(
                    token = intent.token,
                    memberRefId = intent.memberRefId
                )

                is ApplyLongTermLoansStore.Intent.CloseWebView -> dispatch(Msg.ClearSignUrl)

                is ApplyLongTermLoansStore.Intent.ClearExisting -> dispatch(Msg.ClearExisting)
                is ApplyLongTermLoansStore.Intent.ClearExistingError -> dispatch(Msg.ClearExistingError)
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
                }.onFailure { e ->
                    dispatch(Msg.LongTermLoansFailed(e.message))
                    println("Reason of failure" + e.message)
                }
                dispatch(Msg.LongTermLoansLoading(false))
            }
        }

        private var getprestaGuarontorshipRequestsJob: Job? = null

        private fun getprestaGuarontorsRequests(
            token: String,
            memberRefId: String
        ) {
            if (getprestaGuarontorshipRequestsJob?.isActive == true) return

            dispatch(Msg.LongTermLoansLoading())

            getprestaGuarontorshipRequestsJob = scope.launch {
                longTermLoansRepository.getGuarantorshipRequests(
                    token = token,
                    memberRefId = memberRefId
                ).onSuccess { response ->
                    dispatch(Msg.GuarontorshipRequestsLoaded(response))

                }.onFailure { e ->
                    dispatch(Msg.LongTermLoansFailed(e.message))
                }

                dispatch(Msg.LongTermLoansLoading(false))
            }
        }

        private var getLongTermLoanRequestByRefIdJob: Job? = null
        private fun getPrestaLongTermLoanRequestByRefId(
            token: String,
            loanRequestRefId: String
        ) {
            if (getLongTermLoanRequestByRefIdJob?.isActive == true) return

            dispatch(Msg.LongTermLoansLoading())

            getLongTermLoanRequestByRefIdJob = scope.launch {
                longTermLoansRepository.getLongTermProductLoanRequestByRefId(
                    token = token,
                    loanRequestRefId = loanRequestRefId
                ).onSuccess { response ->
                    dispatch(Msg.LongTermLoanRequestByRefIdLoaded(response))
                    println(":::::::::getLongTermLoanRequestByRefId")
                    println(response)

                }.onFailure { e ->
                    dispatch(Msg.LongTermLoansFailed(e.message))
                }

                dispatch(Msg.LongTermLoansLoading(false))
            }
        }

        private var checkLoanRequestEligibilityJob: Job? = null

        private fun getLoanRequestEligibilityByMemberRefId(
            token: String,
            memberRefId: String
        ) {
            if (checkLoanRequestEligibilityJob?.isActive == true) return

            dispatch(Msg.LongTermLoansLoading())

            checkLoanRequestEligibilityJob = scope.launch {
                longTermLoansRepository.checkLoanRequestEligibilityByMemberRefId(
                    token,
                    memberRefId
                ).onSuccess { response ->
                    dispatch(Msg.LoanRequestEligibility(response))
                }.onFailure { e ->
                    dispatch(Msg.LongTermLoansFailed(e.message))
                }

                dispatch(Msg.LongTermLoansLoading(false))
            }
        }

        private var getGuarantorAcceptanceResponseJob: Job? = null
        private fun getGuarantorAcceptanceResponse(
            token: String,
            guarantorshipRequestRefId: String,
            isAccepted: Boolean
        ) {
            if (getGuarantorAcceptanceResponseJob?.isActive == true) return
            dispatch(Msg.LongTermLoansLoading())
            getGuarantorAcceptanceResponseJob = scope.launch {
                longTermLoansRepository.getGuarantorAcceptanceStatus(
                    token = token,
                    guarantorshipRequestRefId = guarantorshipRequestRefId,
                    isAccepted = isAccepted
                ).onSuccess { response ->
                    dispatch(Msg.GuarantorAcceptanceResponseLoaded(response))
                }.onFailure { e ->
                    dispatch(Msg.LongTermLoansFailed(e.message))
                }

                dispatch(Msg.LongTermLoansLoading(false))
            }
        }


        private var getLoanByLoanRequestByRefIdJob: Job? = null
        private fun getLoanByLoanRequestRefId(
            token: String,
            loanRequestRefId: String
        ) {
            if (getLoanByLoanRequestByRefIdJob?.isActive == true) return

            dispatch(Msg.LongTermLoansLoading())

            getLoanByLoanRequestByRefIdJob = scope.launch {
                longTermLoansRepository.getLoansByLoanRequestRefId(
                    token = token,
                    loanRequestRefId = loanRequestRefId
                ).onSuccess { response ->
                    dispatch(Msg.LoanByLoanRequestRefIdLoaded(response))
                    println(":::::::::LoanRequestByRequestRefId")
                    println(response)

                }.onFailure { e ->
                    dispatch(Msg.LongTermLoansFailed(e.message))
                }

                dispatch(Msg.LongTermLoansLoading(false))
            }
        }

        private var requestZohoSIgnUrlJob: Job? = null
        private fun requestZohoSignUrl(
            token: String,
            loanRequestRefId: String,
            actorRefId: String,
            actorType: ActorType,
        ) {
            if (requestZohoSIgnUrlJob?.isActive == true) return
            dispatch(Msg.LongTermLoansLoading())
            requestZohoSIgnUrlJob = scope.launch {
                longTermLoansRepository.getZohoSignUrl(
                    token,
                    loanRequestRefId,
                    actorRefId,
                    actorType

                ).onSuccess { response ->
                    dispatch(Msg.ZohoSignUrlLoaded(response))
                    println("Zoho sign url loaded ")
                }.onFailure { e ->
                    dispatch(Msg.LongTermLoansFailed(e.message))
                }
                dispatch(Msg.LongTermLoansLoading(false))
            }
        }

        private var getprestaLongTermLoansRequestsListJob: Job? = null

        private fun getprestaLongTermLoansRequestsList(
            token: String,
            memberRefId: String
        ) {
            if (getprestaLongTermLoansRequestsListJob?.isActive == true) return

            dispatch(Msg.LongTermLoansLoading())

            getprestaLongTermLoansRequestsListJob = scope.launch {
                longTermLoansRepository.getLongTermLoansRequestsList(
                    token = token,
                    memberRefId = memberRefId
                ).onSuccess { response ->
                    dispatch(Msg.LongTermLoansRequestsListLoaded(response))

                }.onFailure { e ->
                    dispatch(Msg.LongTermLoansFailed(e.message))
                }

                dispatch(Msg.LongTermLoansLoading(false))
            }
        }

        private var getprestaLongTermLoansRequestsFilteredListJob: Job? = null
        private fun getprestaLongTermLoansRequestsFilteredList(
            token: String,
            memberRefId: String
        ) {
            if (getprestaLongTermLoansRequestsFilteredListJob?.isActive == true) return

            dispatch(Msg.LongTermLoansLoading())

            getprestaLongTermLoansRequestsFilteredListJob = scope.launch {
                longTermLoansRepository.getLongTermLoansRequestsFilteredList(
                    token = token,
                    memberRefId = memberRefId
                ).onSuccess { response ->
                    dispatch(Msg.LongTermLoansRequestsFilteredListLoaded(response))

                }.onFailure { e ->
                    dispatch(Msg.LongTermLoansFailed(e.message))
                }

                dispatch(Msg.LongTermLoansLoading(false))
            }
        }

        private var getprestaLongTermLoansRequestsSpecificProductJob: Job? = null
        private fun getprestaLongTermLoansRequestsSpecificProduct(
            token: String,
            productRefId: String,
            memberRefId: String
        ) {
            if (getprestaLongTermLoansRequestsSpecificProductJob?.isActive == true) return
            dispatch(Msg.LongTermLoansLoading())

            getprestaLongTermLoansRequestsSpecificProductJob = scope.launch {
                longTermLoansRepository.getLongTermLoansRequestSpecificProduct(
                    token = token,
                    productRefId = productRefId,
                    memberRefId = memberRefId

                ).onSuccess { response ->
                    dispatch(Msg.LongTermLoansRequestsSpecificProductLoaded(response))

                }.onFailure { e ->
                    dispatch(Msg.LongTermLoansFailed(e.message))
                }

                dispatch(Msg.LongTermLoansLoading(false))
            }
        }

        private var replaceGuarantorJob: Job? = null
        private fun replaceLoanGuarantor(
            token: String,
            loanRequestRefId: String,
            guarantorRefId: String,//old guarantor---replace the old guarantor with the new guarantor
            memberRefId: String,
        ) {
            if (replaceGuarantorJob?.isActive == true) return
            dispatch(Msg.LongTermLoansLoading())
            replaceGuarantorJob = scope.launch {
                longTermLoansRepository.updateLoanGuarantor(
                    token,
                    loanRequestRefId,
                    guarantorRefId,
                    memberRefId
                ).onSuccess { response ->
                    dispatch(Msg.UpdatedGuarantorLoaded(response))
                    println("GuarantorUpdated SuccessFully")
                }.onFailure { e ->
                    dispatch(Msg.LongTermLoansFailed(e.message))
                }
                dispatch(Msg.LongTermLoansLoading(false))
            }
        }

        private var getprestawitnessRequestsJob: Job? = null

        private fun getprestaWitnessRequests(
            token: String,
            memberRefId: String
        ) {
            if (getprestawitnessRequestsJob?.isActive == true) return

            dispatch(Msg.LongTermLoansLoading())

            getprestawitnessRequestsJob = scope.launch {
                longTermLoansRepository.getWitnessRequests(
                    token = token,
                    memberRefId = memberRefId
                ).onSuccess { response ->
                    dispatch(Msg.WitnessRequestsLoaded(response))

                }.onFailure { e ->
                    dispatch(Msg.LongTermLoansFailed(e.message))
                }

                dispatch(Msg.LongTermLoansLoading(false))
            }
        }

        private var getprestaWitnessAcceptanceStatusJob: Job? = null
        private fun getprestaWitnessAcceptanceStatus(
            token: String,
            loanRequestRefId: String,
            isAccepted: Boolean
        ) {
            if (getprestaWitnessAcceptanceStatusJob?.isActive == true) return

            dispatch(Msg.LongTermLoansLoading())
            getprestaWitnessAcceptanceStatusJob = scope.launch {
                longTermLoansRepository.getWitnessAcceptanceStatus(
                    token = token,
                    loanRequestRefId = loanRequestRefId,
                    isAccepted = isAccepted
                ).onSuccess { response ->
                    dispatch(Msg.WitnessAcceptanceStatusLoaded(response))

                }.onFailure { e ->
                    dispatch(Msg.LongTermLoansFailed(e.message))
                }

                dispatch(Msg.LongTermLoansLoading(false))
            }
        }

        private var getprestaFavouriteGuarantorJob: Job? = null
        private fun getprestaFavouriteGuarantor(
            token: String,
            memberRefId: String
        ) {
            if (getprestaFavouriteGuarantorJob?.isActive == true) return

            dispatch(Msg.LongTermLoansLoading())

            getprestaFavouriteGuarantorJob = scope.launch {
                longTermLoansRepository.getFavouriteGuarantor(
                    token = token,
                    memberRefId = memberRefId
                ).onSuccess { response ->
                    dispatch(Msg.FavouriteGuarantorLoaded(response))

                }.onFailure { e ->
                    dispatch(Msg.LongTermLoansFailed(e.message))
                }

                dispatch(Msg.LongTermLoansLoading(false))
            }
        }

        private var getAddedprestaFavouriteGuarantorJob: Job? = null
        private fun getAddedprestaFavouriteGuarantor(
            token: String,
            memberRefId: String,
            guarantorRefId: String,
        ) {
            if (getAddedprestaFavouriteGuarantorJob?.isActive == true) return

            dispatch(Msg.LongTermLoansLoading())
            getAddedprestaFavouriteGuarantorJob = scope.launch {
                longTermLoansRepository.addFavouriteGuarantor(
                    token = token,
                    memberRefId = memberRefId,
                    guarantorRefId = guarantorRefId
                ).onSuccess { response ->
                    dispatch(Msg.AddedFavouriteGuarantorLoaded(response))

                }.onFailure { e ->
                    dispatch(Msg.LongTermLoansFailed(e.message))
                }

                dispatch(Msg.LongTermLoansLoading(false))
            }
        }

        private var deleteFavouriteGuarantorJob: Job? = null
        private fun deleteFavouriteGuarantor(
            token: String,
            refId: String
        ) {
            if (deleteFavouriteGuarantorJob?.isActive == true) return
            dispatch(Msg.LongTermLoansLoading())
            deleteFavouriteGuarantorJob = scope.launch {
                longTermLoansRepository.deleteFavouriteGuarantor(
                    token,
                    refId
                ).onSuccess { response ->
                    dispatch(Msg.FavouriteGuarantorDeleted(response))
                }.onFailure { e ->
                    dispatch(Msg.LongTermLoansFailed(e.message))
                }
                dispatch(Msg.LongTermLoansLoading(false))
            }
        }

        private var deleteLoanRequestJob: Job? = null
        private fun deleteLoanRequest(
            token: String,
            loanRequestNumber: String
        ) {
            if (deleteLoanRequestJob?.isActive == true) return
            dispatch(Msg.LongTermLoansLoading())
            deleteLoanRequestJob = scope.launch {
                longTermLoansRepository.deleteLoanRequest(
                    token,
                    loanRequestNumber
                ).onSuccess { response ->
                    dispatch(Msg.LoanRequestDeleted(response))
                }.onFailure { e ->
                    dispatch(Msg.LongTermLoansFailed(e.message))
                }
                dispatch(Msg.LongTermLoansLoading(false))
            }
        }

        private var loadTenantByPhoneNumberJob: Job? = null
        private fun loadPrestaTenantByPhoneNumber(
            token: String,
            phoneNumber: String
        ) {
            if (loadTenantByPhoneNumberJob?.isActive == true) return
            dispatch(Msg.LongTermLoansLoading())
            loadTenantByPhoneNumberJob = scope.launch {
                longTermLoansRepository.loadTenantByPhoneNumber(
                    token = token,
                    phoneNumber = phoneNumber
                ).onSuccess { response ->
                    dispatch(Msg.LoadedSignTenantByPhoneNumber(response))
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
                is Msg.GuarontorshipRequestsLoaded -> copy(prestaGuarontorshipRequests = msg.guarantorShipRequestsLoaded)
                is Msg.LongTermLoanRequestByRefIdLoaded -> copy(prestaLongTermLoanrequestBYRefId = msg.longTermLoanRequestByRefIdResponse)
                is Msg.GuarantorAcceptanceResponseLoaded -> copy(prestaGuarontorAcceptanceStatus = msg.guarantorAcceptanceResponse)
                is Msg.LoanByLoanRequestRefIdLoaded -> copy(prestaLoanByLoanRequestRefId = msg.loanByLoanRequestRefId)
                is Msg.ZohoSignUrlLoaded -> copy(prestaZohoSignUrl = msg.zohoSignUrlResponse)
                is Msg.LongTermLoansRequestsListLoaded -> copy(prestaLongTermLoansRequestsList = msg.longTermLoansRequestsListResponse)
                is Msg.LongTermLoansRequestsFilteredListLoaded -> copy(
                    prestaLongTermLoansRequestsFilteredList = msg.longTermLoansRequestsFilteredListResponse
                )

                is Msg.LongTermLoansRequestsSpecificProductLoaded -> copy(
                    prestaLongTermLoansRequestsSpecificProduct = msg.longTermLoansRequestsSpecificProduct
                )

                is Msg.UpdatedGuarantorLoaded -> copy(prestaReplaceLoanGuarantor = msg.replacedGuarantorResponse)
                is Msg.WitnessRequestsLoaded -> copy(prestaWitnessRequests = msg.witnessRequestsLoaded)
                is Msg.FavouriteGuarantorLoaded -> copy(prestaFavouriteGuarantor = msg.favouriteGuarantorLoaded)
                is Msg.AddedFavouriteGuarantorLoaded -> copy(prestaAdedFavouriteGuarantor = msg.addedfavouriteGuarantorLoaded)
                is Msg.FavouriteGuarantorDeleted -> copy(deleteFavouriteGuarantorResponse = msg.favouriteGuarantorDeletedResponse)
                is Msg.LoanRequestDeleted -> copy(deleteLoanRequestResponse = msg.loanRequestDeleted)
                is Msg.LoadedSignTenantByPhoneNumber -> copy(prestaLoadTenantByPhoneNumber = msg.signTenantByPhoneNumberResponse)
                is Msg.WitnessAcceptanceStatusLoaded -> copy(prestaWitnessAcceptanceStatus = msg.witnessAcceptanceResponseLoaded)
                is Msg.ClearSignUrl -> copy(prestaZohoSignUrl = null)
                is Msg.LoanRequestEligibility -> copy(loanRequestEligibility = msg.loanRequestEligibility)
                is Msg.SetExistingLoanRequest -> copy(loanRefId = msg.loanRefId)
                is Msg.ClearExisting -> copy(loanRefId = "")
                is Msg.ClearExistingError -> copy(error = null)
            }
    }

}