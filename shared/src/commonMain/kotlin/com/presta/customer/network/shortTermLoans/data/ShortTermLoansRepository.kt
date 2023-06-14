package com.presta.customer.network.shortTermLoans.data

import com.presta.customer.network.shortTermLoans.model.PrestaLoanEligibilityResponse
import com.presta.customer.network.shortTermLoans.model.PrestaShortTermProductsListResponse
import com.presta.customer.network.shortTermLoans.model.PrestaShortTermTopUpListResponse

interface ShortTermLoansRepository {
    suspend fun getShortTermProductListData(memberRefId:String,token:String): Result<List<PrestaShortTermProductsListResponse>>
    suspend fun getShortTermProductLoanById(loanId:String,token:String): Result<PrestaShortTermProductsListResponse>

    suspend fun getShortTermTopUpListData(session_id:String,memberRefId:String,token:String):Result<PrestaShortTermTopUpListResponse>
    suspend fun checkLoanEligibility(token: String, session_id: String, customerRefId: String, ): Result<PrestaLoanEligibilityResponse>

}