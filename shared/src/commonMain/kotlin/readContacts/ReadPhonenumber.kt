package readContacts

import androidx.compose.runtime.Composable
import com.presta.customer.AppContext

expect class ReadPhonenumber {
    @Composable
    fun RegisterPicker(onNumberPicked: (String) -> Unit)

    @Composable
    fun picknumber(context: AppContext)
}