package network.onBoarding.model

import kotlinx.serialization.Serializable

@Serializable
data class PrestaOnBoardingResponse (
    val refId: String,
    val created: String,
    val createdBy: String,
    val updated: String,
    val updatedBy: String,
    val isActive: Boolean,
    val id: Int,
    val firstName: String,
    val fullName: String,
    val lastName: String,
    val idNumber: String,
    val memberNumber: String,
    val phoneNumber: String,
    val email: String,
    val totalShares: Double,
    val totalDeposits: Double,
    val committedAmount: Double,
    val availableAmount: Double,
    val isTermsAccepted: Boolean,
    val memberStatus: String,
    val details: Map<String,Map<String, String>>,
    val loansGuaranteedByMe: List<Map<String, String>>,
    val loansGuaranteedToMe: List<Map<String, String>>,
    val activeLoans: List<Map<String, String>>,
    val lastModified: String,
)
@Serializable
data class PrestaUpdateMemberResponse (
    val phoneNumber: String
)