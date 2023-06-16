package com.presta.customer.network

object NetworkConstants {
    const val applicationV2BaseUrl = "https://lending.presta.co.ke/applications/api/v2/"
    const val accountsBaseUrl = "https://accounts.presta.co.ke/"
    const val applicationV1BaseUrl="https://lending.presta.co.ke/applications/api/v1/"


    object PrestaAuthenticateUser {
        const val route = applicationV2BaseUrl + "authentication"
    }

    object PrestaRefreshToken {
        const val route = applicationV2BaseUrl + "authentication/renew-token"
    }

    object PrestaCheckAuthUser {
        const val route = accountsBaseUrl + "authentication"
    }

    object PrestaOnBoardingClient {
        const val route = applicationV2BaseUrl + "account-config"
    }

    object PrestaUpdatePinTermsClient {
        const val route = applicationV2BaseUrl + "pin-and-terms-acceptance"
    }

    object PrestaSelfRegistration {
        const val route = applicationV2BaseUrl + "customers/self-registration"
    }

    object PrestaOtpRequestClient {
        const val route = applicationV2BaseUrl + "otp"
    }

    object PrestaOtpVerifyClient {
        const val route = applicationV2BaseUrl + "otp/validation"
    }

    object PrestaGetPendingApplications {
        const val route = applicationV2BaseUrl + "loans"
    }

    object PrestaGetSavingsBalance {
        const val route = applicationV2BaseUrl + "balances/savings"
    }

    object PrestaGetLoansBalance {
        const val route = applicationV2BaseUrl + "balances/loans"
    }

    object PrestaMakePayment {
        const val route = applicationV2BaseUrl + "app-payments"
    }

    object PrestaPollPaymentStatus {
        const val route = applicationV2BaseUrl + "app-payments/poll-status"
    }

    object PrestaGetTransactionsHistory {
        const  val route = applicationV2BaseUrl + "transactions"
    }

    object PrestaGetTShortTermProductsList {
        const  val route = applicationV2BaseUrl + "loanproduct"
    }
    object  PrestaGetShortTermToUpList{
        const val  route= applicationV1BaseUrl + "query/topup-loans"
    }
    object PrestaLoanRequest{
        const val route= applicationV1BaseUrl + "loanrequests"

    }
    object PrestaLoanEligibility{
        const val route= applicationV1BaseUrl + "query/eligibility"

    }

}