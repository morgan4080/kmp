package network.onBoarding.model

import kotlinx.serialization.Serializable

@Serializable
data class PrestaOnBoardingResponse (
    val phoneNumber: String
)