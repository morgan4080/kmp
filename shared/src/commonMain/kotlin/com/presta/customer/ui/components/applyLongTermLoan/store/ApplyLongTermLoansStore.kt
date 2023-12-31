package com.presta.customer.ui.components.applyLongTermLoan.store

import com.arkivanov.mvikotlin.core.store.Store
import com.presta.customer.network.longTermLoans.client.DetailsData
import com.presta.customer.network.longTermLoans.model.ActorType
import com.presta.customer.network.longTermLoans.model.ClientSettingsResponse
import com.presta.customer.network.longTermLoans.model.Guarantor
import com.presta.customer.network.longTermLoans.model.LongTermLoanRequestResponse
import com.presta.customer.network.longTermLoans.model.LongTermLoanResponse
import com.presta.customer.network.longTermLoans.model.PrestaLoanByRefIdResponse
import com.presta.customer.network.longTermLoans.model.PrestaLoanRequestByRequestRefId
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoanCategoriesResponse
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoanSubCategories
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoanSubCategoriesChildren
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoansProductResponse
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoansRequestsListResponse
import com.presta.customer.network.longTermLoans.model.PrestaZohoSignUrlResponse
import com.presta.customer.network.longTermLoans.model.guarantorResponse.PrestaGuarantorResponse
import com.presta.customer.network.longTermLoans.model.PrestaGuarantorAcceptanceResponse
import com.presta.customer.network.longTermLoans.model.PrestaLoanRequestEligibility
import com.presta.customer.network.longTermLoans.model.favouriteGuarantor.PrestaFavouriteGuarantorResponse
import com.presta.customer.network.longTermLoans.model.witnessRequests.PrestaWitnessRequestResponse
import com.presta.customer.network.signHome.model.PrestaSignUserDetailsResponse

interface ApplyLongTermLoansStore :
    Store<ApplyLongTermLoansStore.Intent, ApplyLongTermLoansStore.State, Nothing> {
    sealed class Intent {
        data class GetLongTermLoansProducts(val token: String) : Intent()
        data class GetPrestaLongTermProductById(val token: String, val loanRefId: String) : Intent()
        data class GetLongTermLoansProductsCategories(val token: String) : Intent()
        data class GetLongTermLoansProductsSubCategories(val token: String, val parent: String) :
            Intent()

        data class GetLongTermLoansProductsSubCategoriesChildren(
            val token: String,
            val parent: String,
            val child: String
        ) : Intent()

        data class GetClientSettings(val token: String) : Intent()
        data class RequestLongTermLoan(
            val token: String,
            val details: DetailsData,
            val loanProductName: String,
            val loanProductRefId: String,
            val selfCommitment: Double,
            val loanAmount: Double,
            val memberRefId: String,
            val memberNumber: String,
            val witnessRefId: String?,
            val guarantorList: ArrayList<Guarantor>,
        ) : Intent()

        data class GetPrestaGuarantorshipRequests(
            val token: String,
            val memberRefId: String
        ) : Intent()

        //Todo----To be replaced with --GetPrestaLoanByLoanRequestRefId
        data class GetPrestaLongTermLoanRequestByRefId(
            val token: String,
            val loanRequestRefId: String
        ) : Intent()

        data class GetGuarantorAcceptanceStatus(
            val token: String,
            val guarantorshipRequestRefId: String,
            val isAccepted: Boolean
        ) : Intent()

        data class GetPrestaLoanByLoanRequestRefId(
            val token: String,
            val loanRequestRefId: String
        ) : Intent()

        data class GetZohoSignUrl(
            val token: String,
            val loanRequestRefId: String,
            val actorRefId: String,
            val actorType: ActorType
        ) : Intent()

        data class GetPrestaLongTermLoansRequestsList(
            val token: String,
            val memberRefId: String
        ) : Intent()

        data class GetPrestaLongTermLoansRequestsFilteredList(
            val token: String,
            val memberRefId: String
        ) : Intent()

        data class GetPrestaLongTermLoansRequestsSpecificProduct(
            val token: String,
            val productRefId: String,
            val memberRefId: String
        ) : Intent()

        data class ReplaceLoanGuarantor(
            val token: String,
            val loanRequestRefId: String,
            val guarantorRefId: String,
            val memberRefId: String,
        ) : Intent()

        data class GetPrestaWitnessRequests(
            val token: String,
            val memberRefId: String
        ) : Intent()

        data class GetWitnessAcceptanceStatus(
            val token: String,
            val loanRequestRefId: String,
            val isAccepted: Boolean
        ) : Intent()

        data class GetPrestaFavouriteGuarantor(
            val token: String,
            val memberRefId: String
        ) : Intent()

        data class AddFavouriteGuarantor(
            val token: String,
            val memberRefId: String,
            val guarantorRefId: String,
        ) : Intent()

        data class DeleteFavouriteGuarantor(
            val token: String,
            val refId: String
        ) : Intent()

        data class DeleteLoanRequest(
            val token: String,
            val loanRequestNumber: String
        ) : Intent()

        data class LoadTenantByPhoneNumber(
            val token: String,
            val phoneNumber: String
        ) : Intent()
        data class CheckLoanRequestEligibility(
            val token: String,
            val memberRefId: String
        ) : Intent()

        data object CloseWebView : Intent()
        data object ClearExisting : Intent()
        data object ClearExistingError : Intent()
        data object ClearEligibilityResponse : Intent()

    }

    data class State(
        val isLoading: Boolean = false,
        val passedLoanRefId: String? = null,
        val error: String? = null,
        val prestaLongTermLoanProducts: PrestaLongTermLoansProductResponse? = null,
        val prestaLongTermLoanProductById: LongTermLoanResponse? = null,
        val prestaLongTermLoanProductsCategories: List<PrestaLongTermLoanCategoriesResponse> = emptyList(),
        val prestaLongTermLoanProductsSubCategories: List<PrestaLongTermLoanSubCategories> = emptyList(),
        val prestaLongTermLoanProductsSubCategoriesChildren: List<PrestaLongTermLoanSubCategoriesChildren> = emptyList(),
        val prestaClientSettings: ClientSettingsResponse? = null,
        val prestaLongTermLoanRequestData: LongTermLoanRequestResponse? = null,
        val prestaGuarontorshipRequests: List<PrestaGuarantorResponse> = emptyList(),
        val prestaLongTermLoanrequestBYRefId: PrestaLoanByRefIdResponse? = null,
        val prestaGuarontorAcceptanceStatus: PrestaGuarantorAcceptanceResponse? = null,
        val prestaLoanByLoanRequestRefId: PrestaLoanRequestByRequestRefId? = null,
        val prestaZohoSignUrl: PrestaZohoSignUrlResponse? = null,
        val prestaLongTermLoansRequestsList: PrestaLongTermLoansRequestsListResponse? = null,
        val prestaLongTermLoansRequestsFilteredList: PrestaLongTermLoansRequestsListResponse? = null,
        val prestaLongTermLoansRequestsSpecificProduct: PrestaLongTermLoansRequestsListResponse? = null,
        val prestaReplaceLoanGuarantor: String? = null,
        val prestaWitnessRequests: List<PrestaWitnessRequestResponse> = emptyList(),
        val prestaWitnessAcceptanceStatus: PrestaWitnessRequestResponse? = null,
        val prestaFavouriteGuarantor: List<PrestaFavouriteGuarantorResponse> = emptyList(),
        val prestaAdedFavouriteGuarantor: PrestaFavouriteGuarantorResponse? = null,
        val deleteFavouriteGuarantorResponse: String? = null,
        val deleteLoanRequestResponse: String? = null,
        var prestaLoadTenantByPhoneNumber: PrestaSignUserDetailsResponse? = null,
        val memberNo: String = "By Member No",
        val phoneNo: String = "By Phone No",
        val selfGuarantee: String = "Self Guarantee",
        val loanRequestEligibility: PrestaLoanRequestEligibility? = null,
        val loanRefId: String = "",
        val replacedGuarantor: String = "",
    )
}