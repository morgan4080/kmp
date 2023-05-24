package com.presta.customer.network

import com.presta.customer.organisation.OrganisationModel

object NetworkConstants {
    const val guarantorBaseUrl = "https://eguarantorship-api.presta.co.ke/api/v1/"
    const val baseUrlKeycloak = "https://iam.presta.co.ke/auth/realms/"
    const val accountsUrlKeycloak = "https://accounts.presta.co.ke/users-admin/api/v1/"

    object PrestaAuthenticateClient {
        val route = baseUrlKeycloak + OrganisationModel.organisation.tenant_id + "/protocol/openid-connect/token"
    }

    object PrestaAuthenticateUser{
        val route = baseUrlKeycloak + OrganisationModel.organisation.tenant_id + "/protocol/openid-connect/token"
    }

    object PrestaCheckAuthUser {
        val route = "https://accounts.presta.co.ke/authentication"
    }

    object PrestaOnBoardingClient {
        const val route = guarantorBaseUrl + "core-banking/member-details"
    }

    object PrestaUpdatePinTermsClient {
        const val route = guarantorBaseUrl + "members/update-terms-pin"
    }

    object PrestaOtpRequestClient {
        const val route = guarantorBaseUrl + "members/send-otp"
    }

    object PrestaOtpVerifyClient {
        const val route = guarantorBaseUrl + "members/validate-otp"
    }

    object PrestaCheckPinClient {
        const val route = accountsUrlKeycloak + "auth/ussd/has-pin"
    }
}