package com.presta.customer.network.longTermLoans.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

enum class NonEligibilityReasons {
    MEMBER_NOT_FOUND, LOAN_INPROGRESS
}

@Serializable
data class EligibilityMetaData(
    val existingLoanRequestRefId: String
)
@Serializable
data class PrestaLoanRequestEligibility @OptIn(ExperimentalSerializationApi::class) constructor(
    val isEligible: Boolean,
    val nonEligibilityReason: NonEligibilityReasons,
    @EncodeDefault val description: String? = null,
    @EncodeDefault val metadata: EligibilityMetaData? = null
)