package com.presta.customer.network.onBoarding.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
enum class RegistrationFeeStatus {
    PAID,NOT_PAID,NOT_APPLICABLE,PARTIALLY_PAID
}

@Serializable
enum class SelfRegistrationStatus {
    ENABLED,DISABLED
}

@Serializable
enum class AuthenticationStatus {
    ACTIVE,
    INACTIVE,
    INCOMPLETE,
    UNDEFINED
}

@Serializable
enum class PinStatus {
    SET,NOT_SET,TEMPORARY
}

@Serializable
enum class InvitationStatus {
    PENDING,
    ACCEPTED,
    DECLINED
}

@Serializable
data class AuthenticationInfoData @OptIn(ExperimentalSerializationApi::class) constructor(
    // TODO: CHECK FOR STATUS UNDEFINED
    val status: AuthenticationStatus,
    @EncodeDefault val pinStatus: PinStatus? = null,
    @EncodeDefault val invitationStatus: InvitationStatus? = null
)
@Serializable
data class AccountInfoData(
    val accountCode: String,
    val accountName: String,
    val selfRegistrationStatus: SelfRegistrationStatus,
)
@Serializable
data class RegistrationFeeInfo(
    val registrationFees: Double,
    val registrationFeeStatus: RegistrationFeeStatus,
)

@Serializable
data class PrestaOnBoardingResponse @OptIn(ExperimentalSerializationApi::class) constructor(
    @EncodeDefault val refId: String? = null,
    val authenticationInfo: AuthenticationInfoData,
    val accountInfo: AccountInfoData,
    @EncodeDefault val registrationFeeInfo: RegistrationFeeInfo? = null
)

@Serializable
data class PrestaUpdateMemberResponse (
    val message: String
)