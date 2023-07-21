package com.presta.customer.network.longTermLoans.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable


//
//"list":[{"refId":"1O3MQqw42QjwVHZp",
//    "name":"Normal  Loan",
//    "interestRate":12.00,"
//    requiredGuarantors":2,"
//    roleActions":[{"role":"Applicant","
//    action_id":"155276000001822022","action_type":
//    "SIGN","is_embedded":"false","private_notes":"","recipient_name
//    ":"","recipient_email":"",
//    "verify_recipient":"false",
//    "recipient_phonenumber":""},{"role":"Guarantor1",
//    "action_id":"155276000001822024","action_type":"SIGN",
//    "is_embedded":"false","private_notes":"","recipient_name":"","recipient_email
//    ":"","verify_recipient":"false","recipient_phonenumber":""},{"role":"Guarantor2",
//    "action_id":"155276000001822026","action_type":"SIGN","is_embedded":"false","private_notes"
//    :"","recipient_name":"","recipient_email":"","verify_recipient":"false","recipient_phonenumber":""},{"role":"Guarantor3","action_id":"155276000001822028","action_type":"SIGN","is_embedded":"false","private_notes":"","recipient_name":"","recipient_email":"","verify_recipient":"false","recipient_phonenumber":""},{"role":"Guarantor4","action_id":"155276000001822030","action_type":"SIGN","is_embedded":"false","private_notes":"","recipient_name":"","recipient_email":"","verify_recipient":"false","recipient_phonenumber":""},{"role":"Guarantor5","action_id":"155276000001822032","action_type":"SIGN","is_embedded":"false","private_notes":"","recipient_name":"","recipient_email":"","verify_recipient":"false","recipient_phonenumber":""},{"role":"Guarantor6","action_id":"155276000001822034","action_type":"SIGN","is_embedded":"false","private_notes":"","recipient_name":"","recipient_email":"","verify_recipient":"false","recipient_phonenumber":""},{"role":"Guarantor7","action_id":"155276000001822036","action_type":"SIGN","is_embedded":"false","private_notes":"","recipient_name":"","recipient_email":"","verify_recipient":"false","recipient_phonenumber":""},{"role":"Guarantor8","action_id":"155276000001822038","action_type":"SIGN","is_embedded":"false","private_notes":"","recipient_name":"","recipient_email":"","verify_recipient":"false","recipient_phonenumber":""},{"role":"Guarantor9","action_id":"155276000001822040","action_type":"SIGN","is_embedded":"false","private_notes":"","recipient_name":"","recipient_email":"","verify_recipient":"false","recipient_phonenumber":""},{"role":"Guarantor10","action_id":"155276000001822042","action_type":"SIGN","is_embedded":"false","private_notes":"","recipient_name":"","recipient_email":"","verify_recipient":"false","recipient_phonenumber":""}],"templateId":"155276000001822001","templateName":"LOAN APPLICATION FORM-2023","details":{}}

@Serializable
data class PrestaLongTermLoansProductResponse @OptIn(ExperimentalSerializationApi::class) constructor(
    val name: String,
    val interestRate: Double,
    val requiredGuarantors: Int,
    val refId: String,
    @EncodeDefault val maxPeriod: String?=null,
    @EncodeDefault val details: Details?=null,
)
