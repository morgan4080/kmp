package readContacts

import android.app.Activity
import android.content.Intent
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CompletableDeferred

class ContactsPickerFragment : Fragment() {
    var phoneNumberResult: CompletableDeferred<String?>? = null
    fun pickPhoneNumber() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
        startActivityForResult(intent, CONTACT_PICK_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CONTACT_PICK_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val phoneNumber = handleActivityResult(data)
            phoneNumberResult?.complete(phoneNumber)
        }
    }

    private fun handleActivityResult(data: Intent?): String? {
        if (data != null) {
            val cursor = data.data?.let {
                requireContext().contentResolver.query(it, null, null, null, null)
            }

            cursor?.use {
                if (it.moveToFirst()) {
                    val phoneNumberColumnIndex =
                        it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    return it.getString(phoneNumberColumnIndex)
                }
            }
        }

        return null
    }

    companion object {
        const val CONTACT_PICK_REQUEST_CODE = 123 // Choose an appropriate request code
    }
}