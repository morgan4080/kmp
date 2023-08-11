package com.presta.customer.network

object NetworkConstants {
    const val applicationV2BaseUrl = "https://lending.presta.co.ke/applications/api/v2/"
    const val accountsBaseUrl = "https://accounts.presta.co.ke/"
    const val applicationV1BaseUrl = "https://lending.presta.co.ke/applications/api/v1/"

    //E-Guarantorship APIs
    const val EguarantorshipApplicationV1BaseUrl = "https://eguarantorship-api.presta.co.ke/api/v1/"
    const val EguarantorshipApplicationV2BaseUrl = "https://eguarantorship-api.presta.co.ke/api/v2/"

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
        const val route = applicationV2BaseUrl + "transactions"
    }

    object PrestaGetTShortTermProductsList {
        const val route = applicationV2BaseUrl + "loanproduct"
    }

    object PrestaGetShortTermToUpList {
        const val route = applicationV1BaseUrl + "query/topup-loans"
    }

    object PrestaLoanRequest {
        const val route = applicationV1BaseUrl + "loanrequests"

    }

    object PrestaLoanEligibility {
        const val route = applicationV1BaseUrl + "query/eligibility"

    }

    object PrestaLoanQuotation {
        const val route = applicationV1BaseUrl + "loanrequests/quotation"

    }

    object PrestaGetPendingApplications {
        const val route = applicationV2BaseUrl + "loans"
    }

    object PrestaGetBanks {
        const val route = applicationV1BaseUrl + "banks"
    }

    object PrestaGetBankAccounts {
        const val route = applicationV1BaseUrl + "banks/accounts"
    }

    object PrestaGetLoanById {
        const val route = applicationV1BaseUrl + "loans"
    }

    object PrestaGetTenantById {
        const val route = applicationV2BaseUrl + "tenants-query/search"
    }

    //E-Guarantorship Apis
    object PrestaGetTenantByPhoneNumber {
        const val route = EguarantorshipApplicationV1BaseUrl + "members/search/by-phone"
    }

    object PrestaGetLongTermLoansProducts {
        const val route = EguarantorshipApplicationV1BaseUrl + "loans-products"
    }

    object PrestaGetTenantByMemberNumber {
        const val route = EguarantorshipApplicationV1BaseUrl + "members/member"
    }

    object PrestaGetLoanCategories {
        const val route = EguarantorshipApplicationV1BaseUrl + "core-banking/sasra-code"
    }

    object PrestaGetClientSettings {
        const val route = EguarantorshipApplicationV1BaseUrl + "clientSettings"
    }
    object PrestaLongTermLoanRequest {
        const val route =  EguarantorshipApplicationV2BaseUrl + "loan-request"

    }
    object PrestaUpdateMemberDetails {
        const val route =  EguarantorshipApplicationV1BaseUrl + "members"
    }
    object PrestaGetGuarantorshipRequests {
        const val route =  EguarantorshipApplicationV1BaseUrl + "guarantorship-request"
    }
    object PrestaLongTermLoanRequestByRefId {
        const val route = EguarantorshipApplicationV1BaseUrl+ "loan-request"
    }
    object PrestaGetLoanByLoanRequestId {
        const val route =  EguarantorshipApplicationV1BaseUrl + "loan-request"
    }
    object PrestaGetZohoSignUrl {
        const val route =  EguarantorshipApplicationV1BaseUrl + "zoho/get-sign-url"
    }


}