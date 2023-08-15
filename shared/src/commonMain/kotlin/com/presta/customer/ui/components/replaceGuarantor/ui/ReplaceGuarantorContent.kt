package com.presta.customer.ui.components.replaceGuarantor.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.ui.components.applyLongTermLoan.store.ApplyLongTermLoansStore
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.replaceGuarantor.ReplaceGuarantorComponent
import com.presta.customer.ui.components.signAppHome.store.SignHomeStore
import com.presta.customer.ui.composables.ActionButton
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.helpers.LocalSafeArea
import dev.icerock.moko.resources.compose.fontFamilyResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReplaceGuarantorContent(
    component: ReplaceGuarantorComponent,
    state: ApplyLongTermLoansStore.State,
    authState: AuthStore.State,
    onEvent: (ApplyLongTermLoansStore.Intent) -> Unit,
    signHomeState: SignHomeStore.State,
    onProfileEvent: (SignHomeStore.Intent) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    var memberNumber by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf(TextFieldValue()) }
    val emptyTextContainer by remember { mutableStateOf(TextFieldValue()) }
    Scaffold(modifier = Modifier.padding(LocalSafeArea.current), topBar = {
        NavigateBackTopBar("Replace Guarantor", onClickContainer = {
            component.onBackNavClicked()
        })
    }, content = { innerPadding ->
        Column(
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(innerPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    LiveSearch()
                }
            }
            //Guarontor Listing
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
            ) {
                //list the  selected  Guarantor
               if (memberNumber==""){
                   item {
                       Row(
                           modifier = Modifier
                               .fillMaxWidth()
                               .padding(top = 30.dp),
                           verticalAlignment = Alignment.CenterVertically
                       ) {
                           Icon(
                               imageVector = Icons.Outlined.Error,
                               modifier = Modifier,
                               contentDescription = null,
                               tint = MaterialTheme.colorScheme.primary
                           )
                           Text(
                               "Add Guarantors using phone number or member number on the above text input",
                               fontSize = 12.sp,
                               fontFamily = fontFamilyResource(MR.fonts.Poppins.regular),
                               modifier = Modifier.padding(start = 10.dp)
                           )
                       }
                   }

               }

            }
            ActionButton(
                label = "Set Guarantor", onClickContainer = {

                },
                enabled = true
            )
        }
    })
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveSearch() {
    val (value, onValueChange) = remember { mutableStateOf("") }
    TextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(fontSize = 17.sp),
        singleLine = true,
        leadingIcon = {
            Icon(
                Icons.Filled.Search,
                null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(20.dp))
            .background(
                color = MaterialTheme.colorScheme.inverseOnSurface,
                RoundedCornerShape(30.dp)
            ),
        placeholder = {
            Text(
                text = "Guarantor Mobile or Member No",
                fontSize = 12.sp,
                fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Color.DarkGray,
            containerColor = MaterialTheme.colorScheme.inverseOnSurface
        ),
        trailingIcon = {
            Row() {
                Divider(
                    modifier = Modifier
                        .height(30.dp)
                        .padding(start = 2.dp)
                        .width(1.dp)
                )
                Icon(
                    Icons.Filled.Contacts,
                    null,
                    tint = MaterialTheme.colorScheme.outline
                )
            }
        }
    )
}