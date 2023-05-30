package com.presta.customer.network.shortTermLoans.data

import com.presta.customer.network.shortTermLoans.model.PrestaShortTermProductsListResponse

interface ShortTermLoansRepository {
    suspend fun getShortTermProductListData(memberRefId:String,token:String): Result<PrestaShortTermProductsListResponse>

}