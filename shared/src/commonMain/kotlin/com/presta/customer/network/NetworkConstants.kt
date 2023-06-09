package com.presta.customer.network

object NetworkConstants {
    const val applicationsBaseUrl = "https://staging-lending.presta.co.ke/applications/api/v2/"
    const val applicationsBaseUrlV1 = "https://staging-lending.presta.co.ke/applications/api/v1/"
    const val accountsBaseUrl = "https://staging-accounts.presta.co.ke/"
    const val applicationV1BaseUrl="https://staging-lending.presta.co.ke/applications/api/v1/"


    object PrestaAuthenticateUser {
        const val route = applicationsBaseUrl + "authentication"
    }

    object PrestaRefreshToken {
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

    object PrestaGetSavingsBalance {
        const val route = applicationsBaseUrl + "balances/savings"
    }

    object PrestaGetLoansBalance {
        const val route = applicationsBaseUrl + "balances/loans"
    }

    object PrestaMakePayment {
        const val route = applicationsBaseUrl + "app-payments"
    }

    object PrestaPollPaymentStatus {
        const val route = applicationsBaseUrl + "app-payments/poll-status"
    }

    object PrestaGetTransactionsHistory {
        const  val route = applicationsBaseUrl + "transactions"
    }

    object PrestaGetTShortTermProductsList {
        const  val route = applicationsBaseUrl + "loanproduct"
    }
    object  PrestaGetShortTermToUpList{
        const val  route= applicationV1BaseUrl + "query/topup-loans"
    }
    object PrestaLoanRequest{
        const val route= applicationV1BaseUrl + "loanrequests"

    }
}