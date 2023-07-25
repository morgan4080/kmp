package com.presta.customer.ui.components.signAppSettings.ui

import ShimmerBrush
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.presta.customer.MR
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.signAppHome.store.SignHomeStore
import com.presta.customer.ui.components.signAppSettings.SignSettingsComponent
import com.presta.customer.ui.composables.ActionButton
import com.presta.customer.ui.composables.LiveTextContainer
import com.presta.customer.ui.helpers.LocalSafeArea
import com.presta.customer.ui.theme.actionButtonColor
import dev.icerock.moko.resources.compose.fontFamilyResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignSettingsContent(
    component: SignSettingsComponent,
    state: SignHomeStore.State,
    authState: AuthStore.State,
    onEvent: (SignHomeStore.Intent) -> Unit,
) {
    val dataTest = state.prestaTenantById?.firstName
    var firstName by remember { mutableStateOf(TextFieldValue()) }
    var lastName by remember { mutableStateOf(TextFieldValue("Mutugi")) }
    var phoneNumber by remember { mutableStateOf(TextFieldValue("0796387377")) }
    var idNumber by remember { mutableStateOf(TextFieldValue("29955745 ")) }
    var email by remember { mutableStateOf(TextFieldValue("morganmurungi@live.com")) }
    val focusRequester = remember { FocusRequester() }
    val emptyTextContainer by remember { mutableStateOf(TextFieldValue()) }
    var checked by remember { mutableStateOf(false) }
    val name = state.prestaTenantById?.firstName
    if (name != null) {
        firstName = TextFieldValue(name.toString())
    }
    Scaffold(
        modifier = Modifier.padding(LocalSafeArea.current),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(start = 9.dp),
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                title = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    modifier = Modifier,
                                    text = "Settings",
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 18.sp
                                )
                            }
                            androidx.compose.material3.IconButton(
                                modifier = Modifier.absoluteOffset(x = 6.dp).zIndex(1f),
                                onClick = {
                                    //scopeDrawer.launch { drawerState.open() }
                                },
                                content = {
                                    Icon(
                                        imageVector = Icons.Outlined.Logout,
                                        modifier = Modifier.size(25.dp),
                                        contentDescription = "Menu pending",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            )
                        }
                    }
                }
            )
        },
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
            ) {

                if (state.prestaTenantById?.firstName == null) {
                    items(6) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding( bottom = 10.dp, start = 16.dp, end = 16.dp)
                                    .background(color = MaterialTheme.colorScheme.background),
                            ) {
                                ElevatedCard(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(color = MaterialTheme.colorScheme.background),
                                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .defaultMinSize(40.dp, 40.dp)
                                            .background(
                                                ShimmerBrush(
                                                    targetValue = 1300f,
                                                    showShimmer = true
                                                )
                                            )
                                            .fillMaxWidth()
                                    ) {
                                    }
                                }
                            }

                    }
                } else {

                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp)
                        ) {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {

                                LiveTextContainer(
                                    userInput = state.prestaTenantById.firstName,
                                    label = "First Name"
                                ) {
                                    val inputValue: String = TextFieldValue(it).text
                                    if (inputValue != "") {
                                        if (TextFieldValue(it).text !== "") {
                                            firstName = TextFieldValue(it)

                                        } else {

                                        }
                                    }
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                LiveTextContainer(
                                    userInput = state.prestaTenantById.lastName,
                                    label = "last Name "
                                ) {
                                    val inputValue: String = TextFieldValue(it).text
                                    if (inputValue != "") {
                                        if (TextFieldValue(it).text !== "") {
                                            firstName = TextFieldValue(it)

                                        } else {

                                        }
                                    }
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                LiveTextContainer(
                                    userInput = state.prestaTenantById.phoneNumber,
                                    label = "Phone Number"
                                ) {
                                    val inputValue: String = TextFieldValue(it).text
                                    if (inputValue != "") {
                                        if (TextFieldValue(it).text !== "") {
                                            firstName = TextFieldValue(it)

                                        } else {

                                        }
                                    }
                                }
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                LiveTextContainer(
                                    userInput = state.prestaTenantById.idNumber,
                                    label = "ID Number"
                                ) {
                                    val inputValue: String = TextFieldValue(it).text
                                    if (inputValue != "") {
                                        if (TextFieldValue(it).text !== "") {
                                            firstName = TextFieldValue(it)

                                        } else {

                                        }
                                    }
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                LiveTextContainer(
                                    userInput = state.prestaTenantById.email,
                                    label = "Email"
                                ) {
                                    val inputValue: String = TextFieldValue(it).text
                                    if (inputValue != "") {
                                        if (TextFieldValue(it).text !== "") {
                                            firstName = TextFieldValue(it)

                                        } else {

                                        }
                                    }
                                }
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Enable Finger Print ",
                                    fontSize = 13.sp,
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                                )
                                val icon: (@Composable () -> Unit)? = if (checked) {
                                    {
                                        Icon(
                                            imageVector = Icons.Filled.Check,
                                            contentDescription = null,
                                            modifier = Modifier.size(SwitchDefaults.IconSize),
                                            tint = actionButtonColor
                                        )
                                    }
                                } else {
                                    null
                                }
                                Switch(
                                    modifier = Modifier
                                        .semantics { contentDescription = "Demo with icon" },
                                    checked = checked,
                                    onCheckedChange = { checked = it },
                                    thumbContent = icon
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 20.dp)
                            ) {
                                ActionButton(label = "SUBMIT", onClickContainer = {
                                    //post  The Edfited  Profile


                                })
                            }
                        }
                    }
                }
                item {
                    Spacer(
                        modifier = Modifier
                            .padding(bottom = 300.dp)
                    )
                }
            }
        })

}