package helpers


val pattern = Regex("^(\\d{1,2}?|)\\d{3}?\\d{3}?\\d{4}\$")

fun isPhoneNumber(phoneNumber: String) : Boolean {
    return phoneNumber.matches(pattern)
}