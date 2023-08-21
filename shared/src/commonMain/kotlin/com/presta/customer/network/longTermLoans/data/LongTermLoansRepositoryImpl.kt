package com.presta.customer.network.longTermLoans.data

import com.presta.customer.network.longTermLoans.client.DetailsData
import com.presta.customer.network.longTermLoans.client.GuarantorPayLoad
import com.presta.customer.network.longTermLoans.client.PrestaLongTermLoansClient
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
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LongTermLoansRepositoryImpl : LongTermLoansRepository, KoinComponent {
    private val prestaLongTermLoansClient by inject<PrestaLongTermLoansClient>()
    override suspend fun getLonTermLoansData(
        token: String
    ): Result<PrestaLongTermLoansProductResponse> {
        return try {
            val response = prestaLongTermLoansClient.getLongTermLoansData(
                token = token
            )
            Result.success(response)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }

    }

    override suspend fun getLongTermProductLoanById(
        token: String,
        loanRefId: String
    ): Result<LongTermLoanResponse> {
        return try {
            val response = prestaLongTermLoansClient.getLongTermProductLoanById(
                token = token,
                loanRefId = loanRefId
            )
            Result.success(response)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun getLongTermLoansCategoriesData(
        token: String
    ): Result<List<PrestaLongTermLoanCategoriesResponse>> {
        return try {
            val response = prestaLongTermLoansClient.getLoanCategories(
                token = token
            )
            Result.success(response)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun getLongTermLoanSubCategoriesData(
        token: String,
        parent: String
    ): Result<List<PrestaLongTermLoanSubCategories>> {
        return try {
            val response = prestaLongTermLoansClient.getLoanSubCategories(
                token = token,
                parent = parent
            )
            Result.success(response)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun getLongTermLoanSubCategoriesChildrenData(
        token: String,
        parent: String,
        child: String
    ): Result<List<PrestaLongTermLoanSubCategoriesChildren>> {
        return try {
            val response = prestaLongTermLoansClient.getLoanSubCategoriesChildren(
                token = token,
                parent = parent,
                child = child
            )
            Result.success(response)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun getClientSettingsData(
        token: String
    ): Result<ClientSettingsResponse> {
        return try {
            val response = prestaLongTermLoansClient.getClientSettings(
                token = token
            )
            Result.success(response)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }


    override suspend fun requestLongTermLoan(
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
    ): Result<LongTermLoanRequestResponse> {
        return try {
            val response = prestaLongTermLoansClient.sendLongTermLoanRequest(
                token = token,
                details = details,
                loanProductName = loanProductName,
                loanProductRefId = loanProductRefId,
                selfCommitment = selfCommitment,
                loanAmount = loanAmount,
                memberRefId = memberRefId,
                memberNumber = memberNumber,
                witnessRefId = witnessRefId,
                guarantorList = guarantorList,
            )
            Result.success(response)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun getGuarantorshipRequests(
        token: String,
        memberRefId: String
    ): Result<List<PrestaGuarantorResponse>> {
        return try {
            val response = prestaLongTermLoansClient.getGuarantorshipRequests(
                token = token,
                memberRefId = memberRefId
            )
            Result.success(response)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun getLongTermProductLoanRequestByRefId(
        token: String,
        loanRequestRefId: String
    ): Result<PrestaLoanByRefIdResponse> {
        return try {
            val response = prestaLongTermLoansClient.getLoanRequestByRefId(
                token = token,
                loanRequestRefId = loanRequestRefId
            )
            Result.success(response)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun getGuarantorAcceptanceStatus(
        token: String,
        guarantorshipRequestRefId: String,
        isAccepted: Boolean
    ): Result<PrestaGuarantorAcceptanceResponse> {
        return try {
            val response = prestaLongTermLoansClient.sendGuarantorAcceptanceStatus(
                token = token,
                guarantorshipRequestRefId = guarantorshipRequestRefId,
                isAccepted = isAccepted

            )
            Result.success(response)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun getLoansByLoanRequestRefId(
        token: String,
        loanRequestRefId: String
    ): Result<PrestaLoanRequestByRequestRefId> {
        return try {
            val response = prestaLongTermLoansClient.getLoanByLoanRequestRefId(
                token = token,
                loanRequestRefId = loanRequestRefId
            )
            Result.success(response)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun getZohoSignUrl(
        token: String,
        loanRequestRefId: String,
        actorRefId: String,
        actorType: ActorType
    ): Result<PrestaZohoSignUrlResponse> {
        return try {
            val response = prestaLongTermLoansClient.sendZohoSignUrlPayload(
                token = token,
                loanRequestRefId = loanRequestRefId,
                actorRefId = actorRefId,
                actorType = actorType
            )
            Result.success(response)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun getLongTermLoansRequestsList(
        token: String,
        memberRefId: String
    ): Result<PrestaLongTermLoansRequestsListResponse> {
        return try {
            val response = prestaLongTermLoansClient.getLongTermLoanRequestsList(
                token = token,
                memberRefId = memberRefId
            )
            Result.success(response)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun getLongTermLoansRequestsFilteredList(
        token: String,
        memberRefId: String
    ): Result<PrestaLongTermLoansRequestsListResponse> {
        return try {
            val response = prestaLongTermLoansClient.getLongTermLoanRequestsFilteredList(
                token = token,
                memberRefId = memberRefId
            )
            Result.success(response)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun updateLoanGuarantor(
        token: String,
        loanRequestRefId: String,
        guarantorRefId: String,//old guarantor---replace the old guarantor with the new guarantor
        memberRefId: String,
    ): Result<LongTermLoanRequestResponse> {
        return try {
            val response = prestaLongTermLoansClient.upDateGuarantor(
                token = token,
                loanRequestRefId = loanRequestRefId,
                guarantorRefId = guarantorRefId,
                memberRefId = memberRefId
            )
            Result.success(response)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun getWitnessRequests(
        token: String,
        memberRefId: String
    ): Result<List<PrestaWitnessRequestResponse>> {
        return try {
            val response = prestaLongTermLoansClient.getWitnessRequests(
                token = token,
                memberRefId = memberRefId
            )
            Result.success(response)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun getFavouriteGuarantor(
        token: String,
        memberRefId: String
    ): Result<List<PrestaFavouriteGuarantorResponse>> {
        return try {
            val response = prestaLongTermLoansClient.getFavouriteGuarantor(
                token = token,
                memberRefId = memberRefId
            )
            Result.success(response)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun addFavouriteGuarantor(
        token: String,
        memberRefId: String,
        guarantorRefId: String
    ): Result<PrestaFavouriteGuarantorResponse> {
        return try {
            val response = prestaLongTermLoansClient.addFavouriteGuarantor(
                token = token,
                memberRefId = memberRefId,
                guarantorRefId = guarantorRefId
            )
            Result.success(response)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun deleteFavouriteGuarantor(
        token: String,
        refId: String
    ): Result<String> {
        return try {
            val response = prestaLongTermLoansClient.deleteFavouriteGuarantor(
                token = token,
                refId = refId
            )
            Result.success(response)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
    override suspend fun deleteLoanRequest(
        token: String,
        loanRequestNumber: String
    ): Result<String> {
        return try {
            val response = prestaLongTermLoansClient.deleteLoanRequest(
                token = token,
                loanRequestNumber= loanRequestNumber
            )
            Result.success(response)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}