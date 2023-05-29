package com.presta.customer.network

import com.presta.customer.organisation.OrganisationModel

object NetworkConstants {
    const val applicationsBaseUrl = "https://staging-lending.presta.co.ke/applications/api/v2/"
    const val accountsBaseUrl = "https://staging-accounts.presta.co.ke/"
    const val baseUrlKeycloak = "https://iam.presta.co.ke/auth/realms/"
    const val accountsUrlKeycloak = "https://accounts.presta.co.ke/users-admin/api/v1/"
    const  val savingsBaseUrl="https://staging-lending.presta.co.ke/applications/api/v2/"
    const val transactionsHistoryBaseUrl="https://staging-lending.presta.co.ke/applications/api/v2/"

    object PrestaAuthenticateClient {
        val route = baseUrlKeycloak + OrganisationModel.organisation.tenant_id + "/protocol/openid-connect/token"
    }

    object PrestaAuthenticateUser{
        const val route = applicationsBaseUrl + "authentication"
    }

    object PrestaRefreshToken{
        const val route = applicationsBaseUrl + "authentication/renew-token"
    }

    object PrestaCheckAuthUser {
        const val route = accountsBaseUrl + "authentication"
    }

    object PrestaOnBoardingClient {
        const val route = applicationsBaseUrl + "account-config"
    }

    object PrestaUpdatePinTermsClient {
        const val route = applicationsBaseUrl + "pin-and-terms-acceptance"
    }

    object PrestaSelfRegistration {
        const val route = applicationsBaseUrl + "customers/self-registration"
    }

    object PrestaOtpRequestClient {
        const val route = applicationsBaseUrl + "otp"
    }

    object PrestaOtpVerifyClient {
        const val route = applicationsBaseUrl + "otp/validation"
    }

    object PrestaCheckPinClient {
        const val route = accountsUrlKeycloak + "auth/ussd/has-pin"
    }

    object PrestaGetSavingsBalance {
        const val route = savingsBaseUrl + "balances/savings"
    }

    object PrestaGetTransactionsHistory{
        const  val route =transactionsHistoryBaseUrl +"transactions"

    }


}