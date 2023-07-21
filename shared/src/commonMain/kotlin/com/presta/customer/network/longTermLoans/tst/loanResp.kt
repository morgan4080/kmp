package com.presta.customer.network.longTermLoans.tst

data class loanResp(
    val interestRate: Double,
    val name: String,
    val refId: String,
    val requiredGuarantors: Int,
    val roleActions: List<RoleAction>,
    val templateId: String,
    val templateName: String
)
