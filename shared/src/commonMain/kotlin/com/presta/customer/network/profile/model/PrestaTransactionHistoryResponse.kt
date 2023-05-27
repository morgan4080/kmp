package com.presta.customer.network.profile.model

 data class PrestaTransactionHistoryResponse (
     val transactionId:String,
     val purpose: String,
     val amount:Double,
     val transactionDate: String
     )
