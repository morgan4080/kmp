package com.presta.customer.network.loanRequest.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable



enum class LoanRequestStatus{
    NEWAPPLICATION, INITIATED, INPROGRESS, COMPLETED, FAILED
}

@Serializable
data class PrestaLoanPollingResponse @OptIn(ExperimentalSerializationApi::class) constructor(
    @EncodeDefault val applicationStatus: LoanRequestStatus? = null
)

@Serializable
enum class DisbursementMethod {
    MOBILEMONEY,
    BANK
}


@Serializable
enum class LoanType {
    _NORMAL_LOAN,
    _TOP_UP
}

// {
// "requestId":"C-26FA7-06F3",
// "loan":{
// "applicationStatus":"INITIATED",
// "loanRequest":{
// "requestId":"C-26FA7-06F3",
// "sessionId":"1de79c2a-982e-40f3-b5ac-164fcd2de5d9",
// "loanType":"_NORMAL_LOAN",
// "customerRefId":"A6htWirm1IZy9FoK",
// "productRefId":"l3y0N8V6vFSQNCIz",
// "amount":15,
// "loanPeriod":1,
// "metadata":{},
// "currentTerm":false,
// "disbursementMethod":"MOBILEMONEY",
// "disbursementAccountReference":"254796387377"
// }}}

/*
{
  "requestId": "string",
  "loan": {
    "tenantId": "string",
    "created": "2023-06-16T15:56:34.716Z",
    "updated": "2023-06-16T15:56:34.716Z",
    "createdBy": "string",
    "updatedBy": "string",
    "id": "string",
    "loanRefId": "string",
    "loanNo": "string",
    "loanDesc": "string",
    "loanDate": "2023-06-16T15:56:34.716Z",
    "applicationStatus": "NEWAPPLICATION",
    "loanRequest": {
      "requestId": "string",
      "sessionId": "string",
      "loanType": "_TOP_UP",
      "customerRefId": "string",
      "productRefId": "string",
      "amount": 0,
      "loanPeriod": 0,
      "metadata": {
        "additionalProp1": "string",
        "additionalProp2": "string",
        "additionalProp3": "string"
      },
      "referencedLoanRefId": "string",
      "currentTerm": true,
      "disbursementMethod": "MOBILEMONEY",
      "disbursementAccountReference": "string"
    },
    "offer": {
      "quotationRefId": "string",
      "principal": 0,
      "interestRate": 0,
      "interestAmount": 0,
      "deductedInterest": 0,
      "totalAmount": 0,
      "upfrontFees": 0,
      "deductedFees": 0,
      "installmentFees": 0,
      "totalFees": 0,
      "disbursementAmount": 0,
      "maturityDate": "2023-06-16T15:56:34.716Z",
      "installmentCount": 0,
      "monthlyInstallment": 0,
      "firstInstallment": "2023-06-16T15:56:34.716Z",
      "lastInstallment": "2023-06-16T15:56:34.716Z",
      "loanFees": [
        {
          "name": "string",
          "accountNumber": "string",
          "deferredAccountNumber": "string",
          "feeType": "RATE",
          "feeValue": 0,
          "amount": 0,
          "balance": 0,
          "deductionRule": "DEDUCT_FROM_DISBURSEMENT",
          "allocationMethod": "SPREAD",
          "zohoLedgerId": "string",
          "accountingStatus": "NOTACCOUNTED",
          "paymentStatus": "PAID"
        }
      ]
    },
    "customer": {
      "uuid": "string",
      "refId": "string",
      "fullName": "string",
      "phoneNumber": "string",
      "email": "string",
      "idNumber": "string",
      "memberNo": "string",
      "creditData": {
        "canIntroduceBeneficiary": true,
        "approvedLimit": 0,
        "currentBalance": 0,
        "sharesBalance": 0,
        "depositBalance": 0,
        "membershipBalance": 0,
        "dividendsBalance": 0,
        "approvedForLoan": true
      },
      "loanSummary": {
        "activeLoanCount": 0,
        "inprogressLoanCount": 0
      },
      "customerMetadata": {
        "branchRefId": "string",
        "branch": "string",
        "regionRefId": "string",
        "region": "string",
        "relationshipManagerRefId": "string",
        "relationshipManager": "string"
      },
      "appraisalStatus": "PENDING"
    },
    "loanProduct": {
      "refId": "string",
      "name": "string",
      "interestRate": 0,
      "interestPeriodCycle": "DAILY",
      "graduatedInterest": [
        {
          "rate": 0,
          "min": 0,
          "max": 0
        }
      ],
      "daysAvailable": "string",
      "loanPeriodUnit": "DAYS",
      "minTerm": 0,
      "maxTerm": 0,
      "minAmount": 0,
      "maxAmount": 0,
      "interestGraduated": true
    },
    "appraisalRequest": {
      "refId": "string",
      "loanNo": "string",
      "tenantId": "string",
      "accountName": "string",
      "userRefId": "string",
      "userUUID": "string",
      "fullName": "string",
      "applicantPhoneNumber": "string",
      "email": "string",
      "memberNo": "string",
      "branchRefId": "string",
      "branch": "string",
      "regionRefId": "string",
      "region": "string",
      "relationshipManagerRefId": "string",
      "relationshipManager": "string",
      "loanDesc": "string",
      "loanDuration": "string",
      "loanTerm": "string",
      "callbackUrl": "string",
      "requestedAmount": 0,
      "loanType": "_TOP_UP",
      "loanOffer": {
        "quotationRefId": "string",
        "principal": 0,
        "interestRate": 0,
        "interestAmount": 0,
        "deductedInterest": 0,
        "totalAmount": 0,
        "upfrontFees": 0,
        "deductedFees": 0,
        "installmentFees": 0,
        "totalFees": 0,
        "disbursementAmount": 0,
        "maturityDate": "2023-06-16T15:56:34.716Z",
        "installmentCount": 0,
        "monthlyInstallment": 0,
        "firstInstallment": "2023-06-16T15:56:34.716Z",
        "lastInstallment": "2023-06-16T15:56:34.716Z",
        "loanFees": [
          {
            "name": "string",
            "accountNumber": "string",
            "deferredAccountNumber": "string",
            "feeType": "RATE",
            "feeValue": 0,
            "amount": 0,
            "balance": 0,
            "deductionRule": "DEDUCT_FROM_DISBURSEMENT",
            "allocationMethod": "SPREAD",
            "zohoLedgerId": "string",
            "accountingStatus": "NOTACCOUNTED",
            "paymentStatus": "PAID"
          }
        ]
      },
      "approvedLimit": 0,
      "savings": 0,
      "loanDate": "2023-06-16T15:56:34.716Z",
      "productName": "string",
      "productRefId": "string",
      "zohoRequestId": "string",
      "guarantors": [
        {
          "refId": "string",
          "fullName": "string",
          "phoneNumber": "string",
          "email": "string",
          "memberNo": "string"
        }
      ]
    },
    "appraisalResult": {
      "tenantId": "string",
      "sms": "string",
      "remarks": "string",
      "remarksForUser": "string",
      "denialReason": "APPROVED_LIMIT_EXCEEDED",
      "appraisalStatus": "PENDING",
      "appraisalInitiationDate": "2023-06-16T15:56:34.716Z",
      "approvalDate": "2023-06-16T15:56:34.716Z",
      "processed": true,
      "processing": true
    },
    "loanDisbursementRequest": {},
    "disbursementResult": {
      "disbursementStatus": "INITIATED",
      "correlationKey": "string",
      "transactionId": "string",
      "disbursementInitiatedDate": "2023-06-16T15:56:34.716Z",
      "disbursementDate": "2023-06-16T15:56:34.716Z",
      "errorMessage": "string",
      "disbursed": true,
      "processed": true,
      "processing": true
    },
    "accountingResult": {
      "loanRefId": "string",
      "loanNo": "string",
      "loanDesc": "string",
      "dateAccounted": "2023-06-16T15:56:34.716Z",
      "accountingStatus": "NOTACCOUNTED"
    },
    "error": {
      "errorCode": 0,
      "errorDescription": "string",
      "metadata": "string",
      "recoverable": true
    },
    "loaningConfig": {
      "isTopupEnabled": true,
      "isParallelLoansEnabled": true,
      "daysLimitActive": true,
      "daysLimitAfterDueDate": 0,
      "loanTypeLimitActive": true,
      "topupInterestProratingActive": true,
      "topupCalculationMethod": "TOPUP_LOAN_USE_TOTAL_BALANCE",
      "parallelLoansEnabled": true,
      "topupEnabled": true
    },
    "topupLoanMetadata": {
      "loanRefId": "string",
      "productRefId": "string",
      "loanNo": "string",
      "loanType": "_TOP_UP",
      "disbursementDate": "2023-06-16T15:56:34.716Z",
      "dueDate": "2023-06-16T15:56:34.716Z",
      "loanBalance": 0,
      "offsetAmount": 0,
      "writeOffAmount": 0,
      "loanBalances": {
        "principalBalance": 0,
        "interestBalance": 0,
        "feesBalance": 0,
        "penaltiesBalance": 0
      }
    },
    "loanDuration": "string"
  }
}*/

@Serializable
data class LoanRequestResponse (val requestId: String)