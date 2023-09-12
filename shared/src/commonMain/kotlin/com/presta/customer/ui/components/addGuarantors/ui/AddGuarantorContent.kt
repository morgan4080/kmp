package com.presta.customer.ui.components.addGuarantors.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PersonRemove
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.presta.customer.MR
import com.presta.customer.Platform
import com.presta.customer.network.longTermLoans.model.GuarantorDataListing
import com.presta.customer.ui.components.addGuarantors.AddGuarantorsComponent
import com.presta.customer.ui.components.applyLongTermLoan.store.ApplyLongTermLoansStore
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.signAppHome.store.SignHomeStore
import com.presta.customer.ui.composables.ActionButton
import com.presta.customer.ui.composables.EmployedDetails
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.composables.SelfEmployedDetails
import com.presta.customer.ui.helpers.LocalSafeArea
import com.presta.customer.ui.helpers.formatMoney
import com.presta.customer.ui.theme.actionButtonColor
import com.presta.customer.ui.theme.backArrowColor
import com.presta.customer.ui.theme.primaryColor
import dev.icerock.moko.resources.compose.fontFamilyResource
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonNull.content

class SnackbarVisualsWithError(
    override val message: String,
    val isError: Boolean
) : SnackbarVisuals {
    override val actionLabel: String
        get() = if (isError) "Error" else "OK"
    override val withDismissAction: Boolean
        get() = false
    override val duration: SnackbarDuration
        get() = SnackbarDuration.Short
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddGuarantorContent(
    component: AddGuarantorsComponent,
    state: ApplyLongTermLoansStore.State,
    authState: AuthStore.State,
    onEvent: (ApplyLongTermLoansStore.Intent) -> Unit,
    signHomeState: SignHomeStore.State,
    onProfileEvent: (SignHomeStore.Intent) -> Unit,
) {
    var launchPopUp by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    var firstName by remember { mutableStateOf(TextFieldValue()) }
    val emptyTextContainer by remember { mutableStateOf(TextFieldValue()) }
    var selectedIndex by remember { mutableStateOf(-1) }
    val skipHalfExpanded by remember { mutableStateOf(true) }
    var guarantorOption by remember { mutableStateOf("") }
    var launchCheckSelfAndEmPloyedPopUp by remember { mutableStateOf(false) }
    var allConditionsChecked by remember { mutableStateOf(false) }
    var searchGuarantorByMemberNumber by remember { mutableStateOf(false) }
    var searchGuarantorByPhoneNumber by remember { mutableStateOf(false) }
    var selfGuarantee by remember { mutableStateOf(false) }
    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = skipHalfExpanded
    )
    var memberNumber by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val snackBarScope = rememberCoroutineScope()
    var launchAddAmountToGuarantee by remember { mutableStateOf(false) }
    var employer by remember { mutableStateOf("") }
    var employerkey by remember { mutableStateOf("") }
    var grossSalary by remember { mutableStateOf("") }
    var netSalary by remember { mutableStateOf("") }
    var kraPin by remember { mutableStateOf("") }
    var employmentNumber by remember { mutableStateOf("") }
    var businessLocation by remember { mutableStateOf("") }
    var businessType by remember { mutableStateOf("") }
    var amountToGuarantee by remember { mutableStateOf(TextFieldValue()) }
    var launchGuarantorListing by remember { mutableStateOf(false) }
    val pattern = remember { Regex("^\\d+\$") }
    var userInputs by remember { mutableStateOf(TextFieldValue()) }
    if (memberNumber != "") {
        LaunchedEffect(
            authState.cachedMemberData,
            memberNumber

        ) {
            authState.cachedMemberData?.let {
                SignHomeStore.Intent.GetPrestaTenantByMemberNumber(
                    token = it.accessToken,
                    memberNumber = memberNumber
                )
            }?.let {
                onProfileEvent(
                    it
                )
            }
        }
    }
    //Get Tenant by phone Number
    if (memberNumber != "") {
        LaunchedEffect(
            authState.cachedMemberData,
            memberNumber

        ) {
            authState.cachedMemberData?.let {
                ApplyLongTermLoansStore.Intent.LoadTenantByPhoneNumber(
                    token = it.accessToken,
                    phoneNumber = memberNumber
                )
            }?.let {
                onEvent(
                    it
                )
            }
        }
    }
    signHomeState.prestaTenantByPhoneNumber?.details?.map { it ->
        if (it.key.contains("employer")) {
            employer = it.value.value.toString()
            employerkey = it.key
        }
        if (it.key.contains("gross")) {
            grossSalary = it.value.value.toString()
        }
        if (it.key.contains("net")) {
            netSalary = it.value.value.toString()
        }
        if (it.key.contains("kra")) {
            kraPin = it.value.value.toString()
        }
        if (it.key.contains("employment")) {
            employmentNumber = it.value.value.toString()
        }
        if (it.key.contains("businessL")) {
            businessLocation = it.value.value.toString()
        }
        if (it.key.contains("businessT")) {
            businessType = it.value.value.toString()
        }
    }
    val scope = rememberCoroutineScope()
    var guarantorDataListed by remember { mutableStateOf(emptySet<GuarantorDataListing>()) }
    //Call-back to delete the selected  gaurantor from the list
    val clearItemClicked: (GuarantorDataListing) -> Unit = { item ->
        guarantorDataListed -= item
    }
    //Todo------handle Witness selection when required to add a witness
    var violateSelfGuarantee by remember { mutableStateOf(false) }
    LaunchedEffect(
        guarantorDataListed
    ) {
        guarantorDataListed.map { datas ->
            if (state.prestaClientSettings?.response?.allowSelfGuarantee == false) {
                if (datas.phoneNumber == signHomeState.prestaTenantByPhoneNumber?.phoneNumber) {
                    violateSelfGuarantee = true
                    launchAddAmountToGuarantee = false
                    clearItemClicked(datas)
                    snackBarScope.launch {
                        snackbarHostState.showSnackbar(
                            SnackbarVisualsWithError(
                                "self guarantee not allowed $memberNumber",
                                isError = true
                            )
                        )
                    }
                } else {
                    violateSelfGuarantee = false
                }
            }
        }
    }
    LaunchedEffect(
        selfGuarantee
    ) {
        println("Test data:::::::::::;;" + signHomeState.prestaTenantByPhoneNumber?.firstName)
        if (selfGuarantee && signHomeState.prestaTenantByPhoneNumber?.firstName != null) {
            val apiResponse = listOf(
                GuarantorDataListing(
                    signHomeState.prestaTenantByPhoneNumber.firstName,
                    signHomeState.prestaTenantByPhoneNumber.lastName,
                    signHomeState.prestaTenantByPhoneNumber.phoneNumber,
                    signHomeState.prestaTenantByPhoneNumber.memberNumber,
                    amountToGuarantee.text,
                    signHomeState.prestaTenantByPhoneNumber.refId,
                )
            )
            val existingItems = guarantorDataListed.toSet()
            val duplicateItems = apiResponse.filter { it in existingItems }
            if (duplicateItems.isNotEmpty()) {
                snackBarScope.launch {
                    snackbarHostState.showSnackbar(
                        SnackbarVisualsWithError(
                            "Duplicate Entries not  allowed",
                            isError = true
                        )
                    )
                }
            } else {
                guarantorDataListed =
                    guarantorDataListed.toMutableSet().apply {
                        addAll(apiResponse)
                    }
                launchGuarantorListing = true
            }
        }
    }
    var amountDesired by remember { mutableStateOf(0) }
    var amountaken by remember { mutableStateOf(0) }
    var diferences by remember { mutableStateOf(0) }
    var launchContacts by remember { mutableStateOf(false) }
    val contactsScope = rememberCoroutineScope()
    val numberPattern = remember { Regex("^\\d+\$") }
    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            if (launchCheckSelfAndEmPloyedPopUp) {
                val tabs = listOf("Employed", "Business/Self Employed")
                var tabIndex by remember { mutableStateOf(0) }
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                        .fillMaxHeight()
                ) {
                    TabRow(
                        selectedTabIndex = tabIndex,
                        modifier = Modifier,
                    ) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                text = {
                                    Text(
                                        text = title,
                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                                        fontSize = 12.sp
                                    )
                                },
                                selected = tabIndex == index,
                                onClick = {
                                    tabIndex = index
                                },
                                modifier = Modifier,
                                selectedContentColor = MaterialTheme.colorScheme.primary,
                                unselectedContentColor = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                    when (tabIndex) {
                        0 -> EmployedDetails(
                            state,
                            authState,
                            onEvent,
                            signHomeState,
                            onProfileEvent,
                            //Delegated onclick Button functions
                            onClickSubmit = {
                                allConditionsChecked = true
                                scope.launch { modalBottomSheetState.hide() }
                            },
                            component
                        )

                        1 -> SelfEmployedDetails(
                            signHomeState,
                            authState,
                            onProfileEvent
                        )
                    }
                }
            } else if (launchAddAmountToGuarantee && (signHomeState.prestaTenantByMemberNumber?.firstName != null || state.prestaLoadTenantByPhoneNumber?.firstName != null)) {
                Column(
                    modifier = Modifier
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        //Todo----Dismiss focus requester when the modal bottomsheet is dismissed
                        Row(
                            modifier = Modifier
                                .padding(top = 5.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Icon(
                                Icons.Filled.Cancel,
                                contentDescription = "Cancel  Arrow",
                                tint = backArrowColor,
                                modifier = Modifier.clickable {
                                    scope.launch { modalBottomSheetState.hide() }
                                }
                            )
                        }
                        Row(
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Guarantor: " + if (searchGuarantorByMemberNumber) signHomeState.prestaTenantByMemberNumber?.firstName else state.prestaLoadTenantByPhoneNumber?.firstName,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                fontSize = 12.sp,
                            )
                        }
                        Row(
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .fillMaxWidth()
                        ) {
                            amountDesired = component.desiredAmount.toInt()
                            amountaken = guarantorDataListed.sumOf { it.amount.toInt() }
                            diferences = amountDesired - amountaken
                            Text(
                                text = "Remaining Amount : ${formatMoney(diferences.toDouble())}",
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                fontSize = 12.sp
                            )
                        }
                        //modified Text container  to support dark mode
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(0.5.dp, RoundedCornerShape(10.dp))
                                    .background(
                                        color = MaterialTheme.colorScheme.background,
                                        shape = RoundedCornerShape(10.dp)
                                    ),
                            ) {
                                BasicTextField(
                                    modifier = Modifier
                                        .focusRequester(focusRequester)
                                        .height(65.dp)
                                        .padding(
                                            top = 20.dp,
                                            bottom = 16.dp,
                                            start = 16.dp,
                                            end = 16.dp
                                        )
                                        .absoluteOffset(y = 2.dp),
                                    enabled = true,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number
                                    ),
                                    value = amountToGuarantee,
                                    onValueChange = { textValue ->
                                        if (textValue.text.isEmpty() || (textValue.text.matches(
                                                pattern
                                            ) &&
                                                    (textValue.text.toInt() in (1..diferences)))
                                        ) {
                                            amountToGuarantee = textValue
                                        }
                                    },
                                    singleLine = true,
                                    textStyle = TextStyle(
                                        color = MaterialTheme.colorScheme.onBackground,
                                        fontWeight = MaterialTheme.typography.bodySmall.fontWeight,
                                        fontSize = 13.sp,
                                        fontStyle = MaterialTheme.typography.bodySmall.fontStyle,
                                        letterSpacing = MaterialTheme.typography.bodySmall.letterSpacing,
                                        lineHeight = MaterialTheme.typography.bodySmall.lineHeight,
                                        fontFamily = MaterialTheme.typography.bodySmall.fontFamily
                                    ),
                                    decorationBox = { innerTextField ->
                                        if (amountToGuarantee.text.isEmpty()
                                        ) {
                                            Text(
                                                modifier = Modifier.alpha(.3f),
                                                text = "Amount to Guarantee",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.outline
                                            )
                                        }

                                        AnimatedVisibility(
                                            visible = amountToGuarantee.text.isNotEmpty(),
                                            modifier = Modifier.absoluteOffset(y = -(16).dp),
                                            enter = fadeIn() + expandVertically(),
                                            exit = fadeOut() + shrinkVertically(),
                                        ) {
                                            Text(
                                                text = "Amount to Guarantee",
                                                color = primaryColor,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontSize = 11.sp
                                            )
                                        }

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                            ) {

                                                innerTextField()
                                            }

                                            AnimatedVisibility(
                                                visible = amountToGuarantee.text.isNotEmpty(),
                                                enter = fadeIn() + expandVertically(),
                                                exit = fadeOut() + shrinkVertically(),
                                            ) {
                                                IconButton(
                                                    modifier = Modifier.size(18.dp),
                                                    onClick = { userInputs = emptyTextContainer },
                                                    content = {
                                                        Icon(
                                                            modifier = Modifier.alpha(0.4f),
                                                            imageVector = Icons.Filled.Cancel,
                                                            contentDescription = null,
                                                            tint = actionButtonColor
                                                        )
                                                    }
                                                )
                                            }
                                        }
                                    }
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .padding(top = 50.dp, bottom = 50.dp)
                        ) {
                            ActionButton(
                                "SUBMIT",
                                onClickContainer = {
                                    if (guarantorDataListed.size != component.requiredGuarantors && searchGuarantorByMemberNumber && signHomeState.prestaTenantByMemberNumber?.firstName != null) {
                                        //Todo-- handle self guarantee case
                                        val apiResponse = listOf(
                                            GuarantorDataListing(
                                                signHomeState.prestaTenantByMemberNumber.firstName,
                                                signHomeState.prestaTenantByMemberNumber.lastName,
                                                signHomeState.prestaTenantByMemberNumber.phoneNumber,
                                                signHomeState.prestaTenantByMemberNumber.memberNumber,
                                                amountToGuarantee.text,
                                                signHomeState.prestaTenantByMemberNumber.refId,
                                            )
                                        )
                                        val existingItems = guarantorDataListed.toSet()
                                        val duplicateItems =
                                            apiResponse.filter { it in existingItems }
                                        if (duplicateItems.isNotEmpty()) {
                                            snackBarScope.launch {
                                                snackbarHostState.showSnackbar(
                                                    SnackbarVisualsWithError(
                                                        "Duplicate Entries not  allowed",
                                                        isError = true
                                                    )
                                                )
                                            }
                                        } else {
                                            guarantorDataListed =
                                                guarantorDataListed.toMutableSet().apply {
                                                    addAll(apiResponse)
                                                }
                                        }
                                    } else {
                                        if (guarantorDataListed.size != component.requiredGuarantors && searchGuarantorByPhoneNumber && state.prestaLoadTenantByPhoneNumber?.phoneNumber != null) {
                                            val apiResponse = listOf(
                                                GuarantorDataListing(
                                                    state.prestaLoadTenantByPhoneNumber.firstName,
                                                    state.prestaLoadTenantByPhoneNumber.lastName,
                                                    state.prestaLoadTenantByPhoneNumber.phoneNumber,
                                                    state.prestaLoadTenantByPhoneNumber.memberNumber,
                                                    amountToGuarantee.text,
                                                    state.prestaLoadTenantByPhoneNumber.refId,
                                                )

                                            )
                                            val existingItems = guarantorDataListed.toSet()
                                            val duplicateItems =
                                                apiResponse.filter { it in existingItems }
                                            if (duplicateItems.isNotEmpty()) {
                                                snackBarScope.launch {
                                                    snackbarHostState.showSnackbar(
                                                        SnackbarVisualsWithError(
                                                            "Duplicate Entries not  allowed",
                                                            isError = true
                                                        )
                                                    )
                                                }
                                            } else {
                                                guarantorDataListed =
                                                    guarantorDataListed.toMutableSet().apply {
                                                        addAll(apiResponse)
                                                    }
                                            }
                                        }
                                    }
                                    scope.launch { modalBottomSheetState.hide() }
                                    launchGuarantorListing = true

                                },
                                enabled = amountToGuarantee.text != "" && diferences > 0 && amountToGuarantee.text.toInt() > 0 && amountToGuarantee.text.toInt() <= diferences
                            )
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(modifier = Modifier.padding(LocalSafeArea.current),
            snackbarHost = {
                SnackbarHost(snackbarHostState) { data ->
                    val isError = (data.visuals as? SnackbarVisualsWithError)?.isError ?: false
                    val buttonColor = if (isError) {
                        ButtonDefaults.textButtonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    } else {
                        ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.inversePrimary
                        )
                    }
                    Snackbar(
                        modifier = Modifier.padding(bottom = 20.dp, start = 16.dp, end = 16.dp),
                        action = {
                            IconButton(
                                onClick = { if (isError) data.dismiss() else data.performAction() },
                            ) {
                                Icon(
                                    Icons.Filled.Cancel,
                                    contentDescription = "Cancel  Arrow",
                                    tint = backArrowColor,
                                    modifier = Modifier.clickable {
                                        if (isError) data.dismiss() else data.performAction()
                                    }
                                )
                            }
                        }
                    ) {
                        Text(data.visuals.message)
                    }
                }
            },
            topBar = {
                NavigateBackTopBar("Add Guarantors", onClickContainer = {
                    component.onBackNavClicked()

                })
            }, content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = MaterialTheme.colorScheme.background)
                            .padding(innerPadding),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ElevatedCard(
                            onClick = {
                                //launch pop up
                                launchPopUp = true

                            },
                            modifier = Modifier
                                .background(color = MaterialTheme.colorScheme.background)
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(color = MaterialTheme.colorScheme.background)
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    androidx.compose.material3.IconButton(
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .background(Color(0xFFE5F1F5))
                                            .size(25.dp),
                                        onClick = {
                                            launchPopUp = true
                                        },
                                        content = {
                                            Icon(
                                                imageVector = Icons.Filled.KeyboardArrowDown,
                                                modifier = if (launchPopUp) Modifier.size(25.dp)
                                                    .rotate(180F) else Modifier.size(25.dp),
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    )
                                }
                            }
                        }
                        Row(modifier = Modifier) {
                            Column(
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .padding(start = 7.dp)
                                    .shadow(0.5.dp, RoundedCornerShape(10.dp))
                                    .background(
                                        color = MaterialTheme.colorScheme.inverseOnSurface,
                                        shape = RoundedCornerShape(10.dp)
                                    ),
                            ) {
                                BasicTextField(
                                    modifier = Modifier
                                        .focusRequester(focusRequester)
                                        .height(50.dp)
                                        .wrapContentWidth()
                                        .padding(
                                            top = 16.dp,
                                            bottom = 16.dp,
                                            start = 10.dp,
                                            end = 10.dp
                                        ),
                                    enabled = true,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Text
                                    ),
                                    value = memberNumber,
                                    onValueChange = {
                                        memberNumber = it
                                    },
                                    singleLine = true,
                                    textStyle = TextStyle(
                                        color = MaterialTheme.colorScheme.onBackground,
                                        fontWeight = MaterialTheme.typography.bodySmall.fontWeight,
                                        fontSize = 13.sp,
                                        fontStyle = MaterialTheme.typography.bodySmall.fontStyle,
                                        letterSpacing = MaterialTheme.typography.bodySmall.letterSpacing,
                                        lineHeight = MaterialTheme.typography.bodySmall.lineHeight,
                                        fontFamily = MaterialTheme.typography.bodySmall.fontFamily
                                    ),
                                    decorationBox = { innerTextField ->
                                        if (memberNumber == "") {
                                            Text(
                                                modifier = Modifier.alpha(.3f),
                                                text = if (guarantorOption == state.memberNo) "Search Member Number" else "Search PhoneBook",
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                        AnimatedVisibility(
                                            visible = memberNumber != "",
                                            modifier = Modifier.absoluteOffset(y = -(16).dp),
                                            enter = fadeIn() + expandVertically(),
                                            exit = fadeOut() + shrinkVertically(),
                                        ) {
                                            Text(
                                                text = if (guarantorOption == state.memberNo) "Search Member Number" else "Search PhoneBook",
                                                color = primaryColor,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontSize = 11.sp
                                            )
                                        }

                                        Row(
                                            modifier = Modifier.fillMaxWidth(0.8f),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                            ) {

                                                innerTextField()
                                            }

                                            AnimatedVisibility(
                                                visible = firstName.text.isNotEmpty(),
                                                enter = fadeIn() + expandVertically(),
                                                exit = fadeOut() + shrinkVertically(),
                                            ) {

                                                IconButton(
                                                    modifier = Modifier.size(18.dp),
                                                    onClick = { firstName = emptyTextContainer },
                                                    content = {
                                                        Icon(
                                                            modifier = Modifier.alpha(0.4f),
                                                            imageVector = Icons.Filled.Cancel,
                                                            contentDescription = null,
                                                            tint = actionButtonColor
                                                        )
                                                    }
                                                )
                                            }
                                        }
                                    }
                                )
                            }
                        }
                        ElevatedCard(
                            onClick = {
                            },
                            modifier = Modifier
                                .background(color = MaterialTheme.colorScheme.background)
                                .padding(start = 7.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(color = Color(0xFFE5F1F5))
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    androidx.compose.material3.IconButton(
                                        modifier = Modifier
                                            .size(25.dp)
                                            .clip(shape = CircleShape)
                                            .background(Color(0xFFE5F1F5)),
                                        onClick = {
                                            launchContacts = true
                                            //Todo----open  the contacts library and take the selected contact
                                            if (launchContacts) {
                                                contactsScope.launch {
                                                    val content =
                                                        component.platform.getContact(421, "KE")
                                                    content.collect { contactData ->
                                                        contactData.map { item ->
                                                            if (item.key == "E_FAILED_TO_SHOW_PICKER") {
                                                                println("GETTING KEY FAILED")
                                                                println(item.value)
                                                                this.cancel()
                                                            }
                                                            if (item.key == "CONTACT_PICKER_FAILED") {
                                                                println("GETTING KEY FAILED")
                                                                println(item.value)
                                                                this.cancel()
                                                            }
                                                            if (item.key == "ACTIVITY_STARTED") {
                                                                println("GETTING CONTACT")
                                                                println("Selected data:::::::" + item.value)
                                                            }
                                                            if (item.value.matches(numberPattern)) {
                                                                memberNumber = item.value
                                                            }

                                                        }
                                                    }
                                                }
                                                launchContacts = false
                                            }

                                        },
                                        content = {
                                            Icon(
                                                imageVector = Icons.Outlined.Person,
                                                modifier = Modifier,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp)
                    ) {
                        Text(
                            "SELECTED GUARANTORS " + "(REQUIRES " + component.requiredGuarantors + " GUARANTOR)",
                            fontSize = 12.sp
                        )
                    }
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.8f)
                    ) {
                        item {
                            Spacer(modifier = Modifier.padding(top = 30.dp))
                        }
                        if (guarantorDataListed.isEmpty()) {
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
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
                        if (launchGuarantorListing) {
                            if (guarantorDataListed.isNotEmpty()) {
                                guarantorDataListed.forEach { item ->
                                    item {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(bottom = 10.dp)
                                                .background(MaterialTheme.colorScheme.background)
                                        ) {
                                            GuarantorsDetailsView(
                                                label = item.guarantorFirstName + " " + item.guarantorLastName,
                                                onClick = {
                                                    //call back executed
                                                    clearItemClicked(item)
                                                },
                                                selected = true,
                                                phoneNumber = item.phoneNumber,
                                                memberNumber = item.memberNumber,
                                                amount = if (item.amount != "") formatMoney(item.amount.toDouble()) else component.desiredAmount.toString(),
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Row(
                        modifier = Modifier
                            .padding(top = 30.dp)
                            .fillMaxWidth()
                    ) {
                        ActionButton(
                            label = if (guarantorDataListed.size != component.requiredGuarantors) "Search" else "Continue",
                            onClickContainer = {
                                authState.cachedMemberData?.let {
                                    ApplyLongTermLoansStore.Intent.LoadTenantByPhoneNumber(
                                        token = it.accessToken,
                                        phoneNumber = memberNumber
                                    )
                                }?.let {
                                    onEvent(
                                        it
                                    )
                                }
                                if (guarantorDataListed.size != component.requiredGuarantors) {
                                    launchCheckSelfAndEmPloyedPopUp = false
                                    launchAddAmountToGuarantee = true
                                    scope.launch { modalBottomSheetState.show() }
                                } else if (guarantorDataListed.size == component.requiredGuarantors) {
                                    scope.launch { modalBottomSheetState.show() }
                                    launchAddAmountToGuarantee = false
                                    launchCheckSelfAndEmPloyedPopUp = true
                                    if (allConditionsChecked) {
                                        //Todo--check if witness is required and navigate
                                        if (state.prestaClientSettings?.response?.requireWitness == true) {
                                            //navigate  to add witness
                                            signHomeState.prestaTenantByPhoneNumber?.refId?.let {
                                                component.onNavigateToAddWitness(
                                                    component.loanRefId,
                                                    component.loanType,
                                                    component.desiredAmount,
                                                    component.loanPeriod,
                                                    component.requiredGuarantors,
                                                    component.loanCategory,
                                                    component.loanPurpose,
                                                    component.loanPurposeCategory,
                                                    businessType = businessType,
                                                    businessLocation = businessLocation,
                                                    kraPin = kraPin,
                                                    employer = employer,
                                                    employmentNumber = employmentNumber,
                                                    grossSalary = if (grossSalary != "") grossSalary.toDouble() else 0.0,
                                                    netSalary = if (netSalary != "") netSalary.toDouble() else 0.0,
                                                    memberRefId = it,
                                                    guarantorList = guarantorDataListed,
                                                    loanPurposeCategoryCode = component.loanPurposeCategoryCode,
                                                    witnessRefId = ""
                                                )
                                            }
                                            launchCheckSelfAndEmPloyedPopUp = false
                                            scope.launch { modalBottomSheetState.hide() }

                                        } else if (state.prestaClientSettings?.response?.requireWitness == false) {
                                            signHomeState.prestaTenantByPhoneNumber?.refId?.let {
                                                component.onContinueSelected(
                                                    component.loanRefId,
                                                    component.loanType,
                                                    component.desiredAmount,
                                                    component.loanPeriod,
                                                    component.requiredGuarantors,
                                                    component.loanCategory,
                                                    component.loanPurpose,
                                                    component.loanPurposeCategory,
                                                    businessType = businessType,
                                                    businessLocation = businessLocation,
                                                    kraPin = kraPin,
                                                    employer = employer,
                                                    employmentNumber = employmentNumber,
                                                    grossSalary = if (grossSalary != "") grossSalary.toDouble() else 0.0,
                                                    netSalary = if (netSalary != "") netSalary.toDouble() else 0.0,
                                                    memberRefId = it,
                                                    guarantorList = guarantorDataListed,
                                                    loanPurposeCategoryCode = component.loanPurposeCategoryCode,
                                                    witnessRefId = ""
                                                )
                                            }
                                            launchCheckSelfAndEmPloyedPopUp = false
                                            scope.launch { modalBottomSheetState.hide() }
                                        }

                                    }
                                }
                                if (memberNumber != "" && signHomeState.prestaTenantByMemberNumber?.phoneNumber == null && searchGuarantorByMemberNumber) {
                                    launchAddAmountToGuarantee = false
                                    snackBarScope.launch {
                                        snackbarHostState.showSnackbar(
                                            SnackbarVisualsWithError(
                                                "Error loading Member $memberNumber",
                                                isError = true
                                            )
                                        )
                                    }
                                }
                                if (memberNumber != "" && state.prestaLoadTenantByPhoneNumber?.phoneNumber == null && searchGuarantorByPhoneNumber) {
                                    launchAddAmountToGuarantee = false
                                    snackBarScope.launch {
                                        snackbarHostState.showSnackbar(
                                            SnackbarVisualsWithError(
                                                "Error loading Member by phoneNumber  $memberNumber",
                                                isError = true
                                            )
                                        )
                                    }
                                }
                                if (violateSelfGuarantee && guarantorDataListed.isNotEmpty()) {
                                    launchAddAmountToGuarantee = false
                                    snackBarScope.launch {
                                        snackbarHostState.showSnackbar(
                                            SnackbarVisualsWithError(
                                                "self guarantee not allowed $memberNumber",
                                                isError = true
                                            )
                                        )
                                    }

                                }
                            },
                            enabled = true
                        )
                    }
                    //Select Guarantor Option Pop UP
                    if (launchPopUp) {
                        Popup {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                                    .background(color = Color.Black.copy(alpha = 0.7f)),
                                verticalArrangement = Arrangement.Center
                            ) {
                                ElevatedCard(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(0.7f)
                                        .padding(
                                            start = 26.dp,
                                            end = 26.dp,
                                            top = 40.dp,
                                            bottom = 90.dp
                                        ),
                                    colors = CardDefaults
                                        .elevatedCardColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface)
                                ) {

                                    Column(
                                        modifier = Modifier
                                            .padding(start = 16.dp, end = 16.dp)
                                    ) {

                                        Text(
                                            "OPTIONS",
                                            modifier = Modifier
                                                .padding(start = 16.dp, top = 17.dp),
                                            fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                                            fontSize = 14.sp,
                                        )
                                        Text(
                                            "Select guarantor option",
                                            modifier = Modifier
                                                .padding(start = 16.dp),
                                            fontSize = 10.sp,
                                            fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                                        )
                                        Column(
                                            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.7f)
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                            ) {
                                                LazyColumn(
                                                    modifier = Modifier
                                                        .wrapContentHeight()
                                                ) {
                                                    val guarantorList = arrayListOf<String>()
                                                    guarantorList.add(state.memberNo)
                                                    guarantorList.add(state.phoneNo)
                                                    if (state.prestaClientSettings?.response?.allowSelfGuarantee == true) {
                                                        guarantorList.add(state.selfGuarantee)
                                                    }
                                                    guarantorList.mapIndexed { guarantorIndex, guarantorOptions ->
                                                        item {
                                                            Row(
                                                                modifier = Modifier
                                                                    .fillMaxWidth()
                                                                    .background(color = MaterialTheme.colorScheme.inverseOnSurface)
                                                                    .padding(
                                                                        top = 10.dp,
                                                                        start = 16.dp,
                                                                        end = 16.dp
                                                                    )
                                                            ) {
                                                                SelectGuarantorsView(
                                                                    Index = guarantorIndex,
                                                                    selected = selectedIndex == guarantorIndex,
                                                                    onClick = { index: Int ->
                                                                        selectedIndex =
                                                                            if (selectedIndex == index) -1 else index
                                                                        if (selectedIndex > -1) {
                                                                            guarantorOption =
                                                                                guarantorList[selectedIndex]
                                                                        }
                                                                        searchGuarantorByMemberNumber =
                                                                            guarantorOption == state.memberNo
                                                                        searchGuarantorByPhoneNumber =
                                                                            guarantorOption == state.phoneNo
                                                                        selfGuarantee =
                                                                            guarantorOption == state.selfGuarantee
                                                                    },
                                                                    label = guarantorOptions
                                                                )
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                top = 20.dp,
                                                bottom = 20.dp,
                                                start = 16.dp,
                                                end = 16.dp
                                            ),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {

                                        OutlinedButton(
                                            border = BorderStroke(
                                                width = 1.dp,
                                                color = MaterialTheme.colorScheme.primary
                                            ),
                                            onClick = {
                                                launchPopUp = false
                                            },
                                            modifier = Modifier
                                                .padding(start = 16.dp)
                                                .height(30.dp),
                                        ) {

                                            Text(
                                                text = "Dismiss",
                                                fontSize = 11.sp,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.align(Alignment.CenterVertically),
                                                fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                                            )

                                        }
                                        OutlinedButton(
                                            colors = ButtonDefaults.outlinedButtonColors(
                                                containerColor = actionButtonColor
                                            ),
                                            border = BorderStroke(
                                                width = 0.dp,
                                                color = actionButtonColor
                                            ),
                                            onClick = {
                                                launchPopUp = false
                                            },
                                            modifier = Modifier
                                                .padding(end = 16.dp)
                                                .height(30.dp),
                                        ) {

                                            Text(
                                                text = "Proceed",
                                                color = Color.White,
                                                fontSize = 11.sp,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.align(Alignment.CenterVertically),
                                                fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            })
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectGuarantorsView(
    Index: Int,
    selected: Boolean,
    onClick: (Int) -> Unit,
    label: String,
    description: String? = null,
) {
    ElevatedCard(
        onClick = {
            onClick.invoke(Index)
        },

        modifier = Modifier.fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.inverseOnSurface)

    ) {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            Row(
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {

                    Text(
                        text = label,
                        modifier = Modifier
                            .padding(start = 15.dp),
                        fontSize = 12.sp,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                    )

                    if (description != null) {
                        Text(
                            text = description,
                            modifier = Modifier.padding(start = 15.dp),
                            fontSize = 12.sp,
                        )
                    }
                }
                Row {
                    //Created a Custom checkBox
                    Spacer(modifier = Modifier.weight(1f))
                    Card(
                        modifier = Modifier
                            .background(color = MaterialTheme.colorScheme.background)
                            .clip(shape = CircleShape),
                        elevation = CardDefaults.cardElevation(0.dp),
                        border = BorderStroke(
                            1.dp,
                            color = if (selected) actionButtonColor else Color.Gray
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .size(19.dp)
                                .background(if (selected) actionButtonColor else MaterialTheme.colorScheme.background)
                                .clickable {
                                    //onClickContainer(mode)
                                    onClick.invoke(Index)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (selected)
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = "Check Box",
                                    tint = Color.White,
                                    modifier = Modifier.padding(1.dp)
                                )
                        }
                    }
                    Spacer(modifier = Modifier.padding(end = 15.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuarantorsDetailsView(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    phoneNumber: String,
    memberNumber: String,
    amount: String,
) {
    ElevatedCard(
        onClick = {

        },
        modifier = Modifier.fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            Row(
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.inverseOnSurface)
                        .padding(start = 10.dp)
                        .clip(shape = CircleShape),
                    elevation = CardDefaults.cardElevation(0.dp),
                    border = BorderStroke(
                        1.dp,
                        color = if (selected) actionButtonColor else Color.Gray
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .size(19.dp)
                            .background(if (selected) actionButtonColor else MaterialTheme.colorScheme.inverseOnSurface)
                            .clickable {
                                //onClickContainer(mode)
                                // onClick.invoke(Index)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (selected)
                            Icon(
                                Icons.Default.Check,
                                contentDescription = "Check Box",
                                tint = Color.White,
                                modifier = Modifier.padding(1.dp)
                            )
                    }
                }
                Column {

                    Text(
                        text = label,
                        modifier = Modifier
                            .padding(start = 15.dp),
                        fontSize = 12.sp,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.bold)
                    )
                    Row(
                        modifier = Modifier.width(IntrinsicSize.Max),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = phoneNumber,
                            modifier = Modifier
                                .padding(start = 10.dp),
                            fontSize = 12.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                        )
                        Divider(
                            modifier = Modifier
                                .height(10.dp)
                                .padding(start = 2.dp)
                                .width(1.dp)
                        )
                        Text(
                            text = memberNumber,
                            modifier = Modifier
                                .padding(start = 10.dp),
                            fontSize = 12.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                        )
                        Divider(
                            modifier = Modifier
                                .height(10.dp)
                                .padding(start = 2.dp)
                                .width(1.dp)
                        )
                        Text(
                            text = amount,
                            modifier = Modifier
                                .padding(start = 10.dp),
                            fontSize = 12.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                androidx.compose.material3.IconButton(
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.inverseOnSurface),
                    onClick = {
                        //open contacts Library
                        onClick()

                    },
                    content = {
                        Icon(
                            imageVector = Icons.Outlined.PersonRemove,
                            modifier = Modifier
                                .size(30.dp),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                )
                Spacer(modifier = Modifier.padding(end = 15.dp))
            }
        }
    }
}