package com.presta.customer.ui.components.signAppHome.store

import androidx.compose.ui.text.input.TextFieldValue
import com.arkivanov.mvikotlin.core.store.Store
import com.presta.customer.network.longTermLoans.model.ClientSettingsResponse
import com.presta.customer.network.signHome.model.PrestaSignUserDetailsResponse
import com.presta.customer.ui.components.registration.store.InputFields
import com.presta.customer.ui.composables.InputTypes

data class InputMethodConfirmation(
    val inputLabel: String,
    val fieldType: InputFields,
    val required: Boolean,
    val inputTypes: InputTypes,
    var value: TextFieldValue,
    val errorMessage: String,
    val enabled: Boolean,
    val enumOptions: List<String> = emptyList()
)

data class Kyc(
    val inputLabel: String,
    val fieldType: KycInputs,
    val required: Boolean,
    val inputTypes: InputTypes,
    var value: TextFieldValue,
    val errorMessage: String,
    val enabled: Boolean,
    var enumOptions: MutableList<String> = mutableListOf()
)

enum class KycInputs {
    CITY,
    POBOX,
    POSTALCODE,
    TELEPHONE,
    EMPLOYER,
    EMPLOYMENTNUMBER,
    EMPLOYMENTTERMS,
    WITNESSPAYROLLNO,
    DISBURSEMENT,
    REPAYMENT,
    DESIGNATION,
    DEPARTMENT,
    POSTALADDRESS,
    GROSSSALARY,
    NETSALARY,
    KRAPIN,
    BUSINESSLOCATION,
    DOB,
    BUSINESSTYPE,
    GURANTOR1FOSA,
    GURANTOR2FOSA
}


interface SignHomeStore : Store<SignHomeStore.Intent, SignHomeStore.State, Nothing> {
    sealed class Intent {
        data class GetPrestaTenantByPhoneNumber(val token: String, val phoneNumber: String) :
            Intent()

        data class GetPrestaTenantByMemberNumber(val token: String, val memberNumber: String) :
            Intent()

        data class UpdatePrestaTenantDetails(
            val token: String,
            val memberRefId: String,
            val details: MutableMap<String, String>
        ) : Intent()

        data class UpdatePrestaTenantPersonalInfo(
            val token: String,
            val memberRefId: String,
            val firstName: String,
            val lastName: String,
            val phoneNumber: String,
            val idNumber: String,
            val email: String,
            val callback: () -> Unit = {}
        ) : Intent()

        data class UpdateInputValue(
            val inputField: InputFields,
            val value: TextFieldValue
        ) : Intent()

        data object ClearError : Intent()
        data class UpdateKycValues(
            val inputField: KycInputs,
            val value: TextFieldValue,
            val enumOptions: MutableList<String> = mutableListOf()
        ) : Intent()

        data object ClearKYCErrors : Intent()
        data class GetClientSettings(val token: String) : Intent()
    }

    data class State(
        val isLoading: Boolean = false,
        val error: String? = null,
        val prestaTenantByPhoneNumber: PrestaSignUserDetailsResponse? = null,
        var prestaTenantByMemberNumber: PrestaSignUserDetailsResponse? = null,
        val updatePrestaTenantDetails: PrestaSignUserDetailsResponse? = null,
        val updatePrestaTenantPersonalInfo: PrestaSignUserDetailsResponse? = null,
        val prestaClientSettings: ClientSettingsResponse? = null,
        val firstName: InputMethodConfirmation = InputMethodConfirmation(
            inputLabel = "First Name*",
            fieldType = InputFields.FIRST_NAME,
            inputTypes = InputTypes.STRING,
            required = true,
            value = TextFieldValue(),
            errorMessage = "",
            enabled = true
        ),
        var lastName: InputMethodConfirmation = InputMethodConfirmation(
            inputLabel = "Last Name*",
            fieldType = InputFields.LAST_NAME,
            inputTypes = InputTypes.STRING,
            required = true,
            value = TextFieldValue(),
            errorMessage = "",
            enabled = true
        ),
        val idNumber: InputMethodConfirmation = InputMethodConfirmation(
            inputLabel = "ID Number*",
            fieldType = InputFields.ID_NUMBER,
            inputTypes = InputTypes.NUMBER,
            required = true,
            value = TextFieldValue(),
            errorMessage = "",
            enabled = true
        ),
        val guarantor1FosaAccount: Kyc = Kyc(
            inputLabel = "Guarantor 1 FOSA Account*",
            fieldType = KycInputs.GURANTOR1FOSA,
            inputTypes = InputTypes.NUMBER,
            required = true,
            value = TextFieldValue(),
            errorMessage = "",
            enabled = true
        ),
        val guarantor2FosaAccount: Kyc = Kyc(
            inputLabel = "Guarantor 2 FOSA Account*",
            fieldType = KycInputs.GURANTOR2FOSA,
            inputTypes = InputTypes.NUMBER,
            required = true,
            value = TextFieldValue(),
            errorMessage = "",
            enabled = true
        ),
        val introducer: InputMethodConfirmation = InputMethodConfirmation(
            inputLabel = "Phone Number.",
            fieldType = InputFields.INTRODUCER,
            inputTypes = InputTypes.NUMBER,
            required = false,
            value = TextFieldValue(),
            errorMessage = "",
            enabled = false
        ),
        val dob: Kyc = Kyc(
            inputLabel = "Date of Birth*",
            fieldType = KycInputs.DOB,
            inputTypes = InputTypes.STRING,
            required = true,
            value = TextFieldValue(),
            errorMessage = "",
            enabled = true
        ),
        val email: InputMethodConfirmation = InputMethodConfirmation(
            inputLabel = "Email*",
            fieldType = InputFields.EMAIL,
            inputTypes = InputTypes.STRING,
            required = true,
            value = TextFieldValue(),
            errorMessage = "",
            enabled = true
        ),
        //new update
        val employer: Kyc = Kyc(
            inputLabel = "Employer*",
            fieldType = KycInputs.EMPLOYER,
            inputTypes = InputTypes.STRING,
            required = true,
            value = TextFieldValue(),
            errorMessage = "",
            enabled = true
        ),
        val department: Kyc = Kyc(
            inputLabel = "Department*",
            fieldType = KycInputs.DEPARTMENT,
            inputTypes = InputTypes.STRING,
            required = true,
            value = TextFieldValue(),
            errorMessage = "",
            enabled = true
        ),
        val postalAddress: Kyc = Kyc(
            inputLabel = "Postal Address*",
            fieldType = KycInputs.POSTALADDRESS,
            inputTypes = InputTypes.STRING,
            required = true,
            value = TextFieldValue(),
            errorMessage = "",
            enabled = true
        ),
        val witnessPayrollNo: Kyc = Kyc(
            inputLabel = "Witness Payroll Number*",
            fieldType = KycInputs.WITNESSPAYROLLNO,
            inputTypes = InputTypes.STRING,
            required = true,
            value = TextFieldValue(),
            errorMessage = "",
            enabled = true
        ),
        val employmentNumber: Kyc = Kyc(
            inputLabel = "Employment Number*",
            fieldType = KycInputs.EMPLOYMENTNUMBER,
            inputTypes = InputTypes.STRING,
            required = true,
            value = TextFieldValue(),
            errorMessage = "",
            enabled = true
        ),
        val employmentTerms: Kyc = Kyc(
            inputLabel = "Employment Terms*",
            fieldType = KycInputs.EMPLOYMENTTERMS,
            inputTypes = InputTypes.ENUM,
            required = true,
            value = TextFieldValue(),
            errorMessage = "",
            enabled = true
        ),
        val disbursementModes: Kyc = Kyc(
            inputLabel = "Disbursement Mode*",
            fieldType = KycInputs.DISBURSEMENT,
            inputTypes = InputTypes.ENUM,
            required = true,
            value = TextFieldValue(),
            errorMessage = "",
            enabled = true
        ),
        val repaymentModes: Kyc = Kyc(
            inputLabel = "Repayment Mode*",
            fieldType = KycInputs.REPAYMENT,
            inputTypes = InputTypes.ENUM,
            required = true,
            value = TextFieldValue(),
            errorMessage = "",
            enabled = true
        ),
        val designation: Kyc = Kyc(
            inputLabel = "Designation*",
            fieldType = KycInputs.DESIGNATION,
            inputTypes = InputTypes.STRING,
            required = true,
            value = TextFieldValue(),
            errorMessage = "",
            enabled = true
        ),
        val grossSalary: Kyc = Kyc(
            inputLabel = "Gross Salary*",
            fieldType = KycInputs.GROSSSALARY,
            inputTypes = InputTypes.NUMBER,
            required = true,
            value = TextFieldValue(),
            errorMessage = "",
            enabled = true
        ),
        val netSalary: Kyc = Kyc(
            inputLabel = "Net Salary*",
            fieldType = KycInputs.NETSALARY,
            inputTypes = InputTypes.NUMBER,
            required = true,
            value = TextFieldValue(),
            errorMessage = "",
            enabled = true
        ),
        val kraPin: Kyc = Kyc(
            inputLabel = "Kra Pin*",
            fieldType = KycInputs.KRAPIN,
            inputTypes = InputTypes.STRING,
            required = true,
            value = TextFieldValue(),
            errorMessage = "",
            enabled = true
        ),
        val city: Kyc = Kyc(
            inputLabel = "City *",
            fieldType = KycInputs.CITY,
            inputTypes = InputTypes.STRING,
            required = true,
            value = TextFieldValue(),
            errorMessage = "",
            enabled = true
        ),
        val po_box: Kyc = Kyc(
            inputLabel = "P.O BOX *",
            fieldType = KycInputs.POBOX,
            inputTypes = InputTypes.STRING,
            required = true,
            value = TextFieldValue(),
            errorMessage = "",
            enabled = true
        ),
        val postal_code: Kyc = Kyc(
            inputLabel = "Postal Code*",
            fieldType = KycInputs.POSTALCODE,
            inputTypes = InputTypes.STRING,
            required = true,
            value = TextFieldValue(),
            errorMessage = "",
            enabled = true
        ),
        val telephone_number: Kyc = Kyc(
            inputLabel = "Telephone No.*",
            fieldType = KycInputs.TELEPHONE,
            inputTypes = InputTypes.STRING,
            required = false,
            value = TextFieldValue(),
            errorMessage = "",
            enabled = true
        ),
        val businessLocation: Kyc = Kyc(
            inputLabel = "Business Location*",
            fieldType = KycInputs.BUSINESSLOCATION,
            inputTypes = InputTypes.STRING,
            required = true,
            value = TextFieldValue(),
            errorMessage = "",
            enabled = true
        ),
        val businessType: Kyc = Kyc(
            inputLabel = "Business Type*",
            fieldType = KycInputs.BUSINESSTYPE,
            inputTypes = InputTypes.STRING,
            required = true,
            value = TextFieldValue(),
            errorMessage = "",
            enabled = true
        ),
    )
}