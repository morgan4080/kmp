package network.onBoarding.model

import kotlinx.serialization.Serializable

@Serializable
data class PrestaOnBoardingResponse (
    val phoneNumber: String
)
@Serializable
data class PrestaUpdateMemberResponse (
    val phoneNumber: String
)