package com.presta.customer.network.longTermLoans.data

import com.presta.customer.network.longTermLoans.client.DetailsData
import com.presta.customer.network.longTermLoans.client.GuarantorPayLoad
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
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoansRequestsListResponse
import com.presta.customer.network.longTermLoans.model.PrestaLongTermLoansProductResponse
import com.presta.customer.network.longTermLoans.model.PrestaZohoSignUrlResponse
import com.presta.customer.network.longTermLoans.model.guarantorResponse.PrestaGuarantorResponse
import com.presta.customer.network.longTermLoans.model.PrestaGuarantorAcceptanceResponse
import com.presta.customer.network.longTermLoans.model.favouriteGuarantor.PrestaFavouriteGuarantorResponse
import com.presta.customer.network.longTermLoans.model.witnessRequests.PrestaWitnessRequestResponse

interface LongTermLoansRepository {
    suspend fun getLonTermLoansData(
        token: String
    ): Result<PrestaLongTermLoansProductResponse>

    suspend fun getLongTermProductLoanById(
        token: String,
        loanRefId: String,
    ): Result<LongTermLoanResponse>

    suspend fun getLongTermLoansCategoriesData(
        token: String
    ): Result<List<PrestaLongTermLoanCategoriesResponse>>

    suspend fun getLongTermLoanSubCategoriesData(
        token: String,
        parent: String
    ): Result<List<PrestaLongTermLoanSubCategories>>

    suspend fun getLongTermLoanSubCategoriesChildrenData(
        token: String,
        parent: String,
        child: String
    ): Result<List<PrestaLongTermLoanSubCategoriesChildren>>

    suspend fun getClientSettingsData(
        token: String
    ): Result<ClientSettingsResponse>

    suspend fun requestLongTermLoan(
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
    ): Result<LongTermLoanRequestResponse>

    suspend fun getGuarantorshipRequests(
        token: String,
        memberRefId: String,
    ): Result<List<PrestaGuarantorResponse>>

    suspend fun getLongTermProductLoanRequestByRefId(
        token: String,
        loanRequestRefId: String,
    ): Result<PrestaLoanByRefIdResponse>

    suspend fun getGuarantorAcceptanceStatus(
        token: String,
        guarantorshipRequestRefId: String,
        isAccepted: Boolean
    ): Result<PrestaGuarantorAcceptanceResponse>

    suspend fun getLoansByLoanRequestRefId(
        token: String,
        loanRequestRefId: String,
    ): Result<PrestaLoanRequestByRequestRefId>

    suspend fun getZohoSignUrl(
        token: String,
        loanRequestRefId: String,
        actorRefId: String,
        actorType: ActorType
    ): Result<PrestaZohoSignUrlResponse>

    suspend fun getLongTermLoansRequestsList(
        token: String,
        memberRefId: String,
    ): Result<PrestaLongTermLoansRequestsListResponse>

    suspend fun getLongTermLoansRequestsFilteredList(
        token: String,
        memberRefId: String,
    ): Result<PrestaLongTermLoansRequestsListResponse>
    suspend fun getLongTermLoansRequestSpecificProduct(
        token: String,
        productRefId: String,
        memberRefId: String,
    ): Result<PrestaLongTermLoansRequestsListResponse>

    suspend fun updateLoanGuarantor(
        token: String,
        loanRequestRefId: String,
        guarantorRefId: String,
        memberRefId: String,
    ): Result<LongTermLoanRequestResponse>

    suspend fun getWitnessRequests(
        token: String,
        memberRefId: String,
    ): Result<List<PrestaWitnessRequestResponse>>

    suspend fun getFavouriteGuarantor(
        token: String,
        memberRefId: String,
    ): Result<List<PrestaFavouriteGuarantorResponse>>

    suspend fun addFavouriteGuarantor(
        token: String,
        memberRefId: String,
        guarantorRefId: String,
    ): Result<PrestaFavouriteGuarantorResponse>
    suspend fun deleteFavouriteGuarantor(
        token: String,
        refId: String
    ): Result<String>
    suspend fun deleteLoanRequest(
        token: String,
        loanRequestNumber: String
    ): Result<String>
}