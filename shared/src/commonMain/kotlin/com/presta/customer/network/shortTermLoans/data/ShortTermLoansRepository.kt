package com.presta.customer.network.shortTermLoans.data

import com.presta.customer.network.shortTermLoans.model.PrestaShortTermProductsListResponse
import com.presta.customer.network.shortTermLoans.model.PrestaShortTermTopUpListResponse

interface ShortTermLoansRepository {
    suspend fun getShortTermProductListData(memberRefId:String,token:String): Result<List<PrestaShortTermProductsListResponse>>
    suspend fun getShortTermTopUpListData(session_id:String,memberRefId:String,token:String):Result<PrestaShortTermTopUpListResponse>
}