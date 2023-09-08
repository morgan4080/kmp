package readContacts

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.presta.customer.AppContext

actual class ReadPhonenumber(
    private val activity: ComponentActivity
) {
    private lateinit var getContent: ActivityResultLauncher<String>

    @Composable
    actual fun RegisterPicker(onNumberPicked: (String) -> Unit) {
        getContent = rememberLauncherForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->
            uri?.let {
                activity.contentResolver.openInputStream(uri)?.use {
                    onNumberPicked(it.toString())
                }
            }
        }
    }
@Composable
    actual fun picknumber(context:AppContext) {
//        val intent = Intent(Intent.ACTION_PICK)
//        intent.type = ContactsContract.Contacts.CONTENT_TYPE
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//        context.startActivity(intent)
        //getContent.launch("image/*")
        val context2 = LocalContext.current
        val pickContact = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickContact()
        ) { uri ->
            if (uri != null) {
                val intent = Intent(Intent.ACTION_VIEW, uri)
                context2.startActivity(intent)
            }
        }
    }
}