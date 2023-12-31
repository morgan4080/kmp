package com.presta.customer.ui.components.addWitness

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
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.consumeWindowInsets
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.ImportContacts
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.outlined.ArrowCircleDown
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.zIndex
import com.presta.customer.MR
import com.presta.customer.ui.components.addGuarantors.ui.SelectGuarantorsView
import com.presta.customer.ui.components.addGuarantors.ui.SnackbarVisualsWithError
import com.presta.customer.ui.components.applyLongTermLoan.store.ApplyLongTermLoansStore
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.signAppHome.store.SignHomeStore
import com.presta.customer.ui.composables.ActionButton
import com.presta.customer.ui.composables.InputTypes
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.theme.actionButtonColor
import com.presta.customer.ui.theme.backArrowColor
import com.presta.customer.ui.theme.primaryColor
import dev.icerock.moko.resources.compose.fontFamilyResource
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

enum class WitnessOptions {
    PHONENUMBER,
    MEMBERNUMBER
}

@Serializable
data class FavouriteGuarantorDetails(
    val refId: String,
    val memberFirstName: String,
    val memberLastName: String,
    val memberNumber: String,
    val memberPhoneNumber: String
)

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class,
    ExperimentalLayoutApi::class
)
@Composable
fun AddWitnessContent(
    component: AddWitnessComponent,
    state: ApplyLongTermLoansStore.State,
    authState: AuthStore.State,
    onEvent: (ApplyLongTermLoansStore.Intent) -> Unit,
    signHomeState: SignHomeStore.State,
    onProfileEvent: (SignHomeStore.Intent) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }
    var launchPopUp by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    var memberNumber by remember { mutableStateOf("") }
    var witnessOptions by remember { mutableStateOf(WitnessOptions.PHONENUMBER) }
    var firstName by remember { mutableStateOf(TextFieldValue()) }
    val emptyTextContainer by remember { mutableStateOf(TextFieldValue()) }
    var selectedIndex by remember { mutableStateOf(-1) }
    var conditionChecked by remember { mutableStateOf(false) }
    var memberRefId by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val snackBarScope = rememberCoroutineScope()
    var liveLoaded by remember { mutableStateOf("") }
    var witnessDataListed by remember { mutableStateOf(emptySet<FavouriteGuarantorDetails>()) }
    if (signHomeState.prestaTenantByPhoneNumber?.refId != null) {
        memberRefId = signHomeState.prestaTenantByPhoneNumber.refId
    }
    val clearItemClicked: (FavouriteGuarantorDetails) -> Unit = { item ->
        witnessDataListed -= item
    }
    val contactsScope = rememberCoroutineScope()
    val numberPattern = remember { Regex("^\\d+\$") }
    var refreshing by remember { mutableStateOf(false) }
    val refreshScope = rememberCoroutineScope()
    fun refresh() = refreshScope.launch {
        refreshing = true
        delay(1500)
        refreshing = false
    }

    if (state.isLoading) {
        refreshScope.launch {
            refreshing = true
            delay(1500)
            refreshing = false
        }
    }

    if (signHomeState.isLoading) {
        refreshScope.launch {
            refreshing = true
            delay(1500)
            refreshing = false
        }
    }

    val refreshState = rememberPullRefreshState(refreshing, ::refresh)

    //cleaned
    LaunchedEffect(
        state.prestaLoadTenantByPhoneNumber,
        witnessOptions,
        state.isLoading
    ) {
        //Get Tenant by phone Number
        if (witnessOptions === WitnessOptions.PHONENUMBER && witnessDataListed.size != 1) {
            var loadedValue = ""

            loadedValue = state.prestaLoadTenantByPhoneNumber?.refId ?: ""
            liveLoaded = if (loadedValue == "") {
                ""
            } else {
                loadedValue
            }
            if (liveLoaded.isNotEmpty()) {
                //automatically add  the list
                if (witnessDataListed.size != 1 && witnessOptions === WitnessOptions.PHONENUMBER && state.prestaLoadTenantByPhoneNumber?.phoneNumber != null) {
                    state.prestaLoadTenantByPhoneNumber?.let {
                        val apiResponse = listOf(
                            FavouriteGuarantorDetails(
                                refId = it.refId,
                                memberFirstName = it.firstName,
                                memberNumber = it.memberNumber,
                                memberLastName = it.lastName,
                                memberPhoneNumber = it.phoneNumber
                            )

                        )
                        val existingItems = witnessDataListed.toSet()
                        val duplicateItems =
                            apiResponse.filter {
                                it in existingItems || existingItems.any { listed ->
                                    println("listed.guarantorRefId:res.guarantorRefId")
                                    println("${listed.refId} : ${it.refId}")
                                    listed.refId == it.refId
                                }
                            }
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
                            witnessDataListed =
                                witnessDataListed.toMutableSet().apply {
                                    addAll(apiResponse)
                                }
                        }
                    }
                }
            } else if (!state.isLoading && memberNumber.isNotEmpty()) {
                snackbarHostState.showSnackbar(
                    SnackbarVisualsWithError(
                        "Error loading Member by phone no.  $memberNumber",
                        isError = true
                    )
                )
            }
        }
    }


    LaunchedEffect(
        signHomeState.prestaTenantByMemberNumber,
        witnessOptions,
        signHomeState.isLoading
    ) {
        //Get Tenant by phone Number
        if (witnessOptions === WitnessOptions.MEMBERNUMBER && witnessDataListed.size != 1) {
            var loadedValue = ""

            loadedValue = signHomeState.prestaTenantByMemberNumber?.refId ?: ""
            liveLoaded = if (loadedValue == "") {
                ""
            } else {
                loadedValue
            }
            if (liveLoaded.isNotEmpty()) {
                //automatically add  the list
                if (witnessDataListed.size != 1 && witnessOptions === WitnessOptions.MEMBERNUMBER && signHomeState.prestaTenantByMemberNumber != null) {
                    signHomeState.prestaTenantByMemberNumber?.let {
                        val apiResponse = listOf(
                            FavouriteGuarantorDetails(
                                refId = it.refId,
                                memberFirstName = it.firstName,
                                memberNumber = it.memberNumber,
                                memberLastName = it.lastName,
                                memberPhoneNumber = it.phoneNumber
                            )

                        )
                        val existingItems = witnessDataListed.toSet()
                        val duplicateItems =
                            apiResponse.filter {
                                it in existingItems || existingItems.any { listed ->
                                    println("listed.guarantorRefId:res.guarantorRefId")
                                    println("${listed.refId} : ${it.refId}")
                                    listed.refId == it.refId
                                }
                            }
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
                            witnessDataListed =
                                witnessDataListed.toMutableSet().apply {
                                    addAll(apiResponse)
                                }
                        }
                    }
                }
            } else if (!signHomeState.isLoading && memberNumber.isNotEmpty()) {
                snackbarHostState.showSnackbar(
                    SnackbarVisualsWithError(
                        "Error loading Member no.  $memberNumber",
                        isError = true
                    )
                )
            }
        }
    }
    LaunchedEffect(
        witnessDataListed
    ) {
        witnessDataListed.map { datas ->
            if (datas.memberNumber == signHomeState.prestaTenantByPhoneNumber?.memberNumber) {
                clearItemClicked(datas)
                snackBarScope.launch {
                    snackbarHostState.showSnackbar(
                        SnackbarVisualsWithError(
                            "Self witnessing not allowed",
                            isError = true
                        )
                    )
                }
            } else {
                conditionChecked = true
            }

        }
    }

    var hasError by remember { mutableStateOf(false) }

    val userDetailsMap = mutableMapOf<String, String>()

    if (showDialog) {
        AlertDialog(
            modifier = Modifier,
            onDismissRequest = {
                println("Dismissed")
            },
            content = {
                ElevatedCard(
                    modifier = Modifier
                        .clip(RoundedCornerShape(size = 12.dp))
                        .absolutePadding(left = 2.dp, right = 2.dp, top = 5.dp, bottom = 5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier.padding(
                                top = 23.dp,
                                start = 24.dp,
                                end = 19.5.dp,
                                bottom = 33.dp,
                            )
                        ) {
                            Row (
                                modifier = Modifier.padding(vertical = 10.dp),
                            ) {
                                Text(
                                    text = "Witness Information",
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 14.sp,
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                                )
                            }

                            Row {
                                listOf(signHomeState.witnessPayrollNo).map { inputMethod ->
                                    LaunchedEffect(inputMethod.errorMessage, inputMethod.value) {
                                        hasError = inputMethod.errorMessage != "" || inputMethod.value.text == ""
                                    }
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp)
                                            .shadow(0.5.dp, RoundedCornerShape(10.dp))
                                            .background(
                                                color =  MaterialTheme.colorScheme.inverseOnSurface,
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
                                            keyboardOptions = KeyboardOptions(
                                                keyboardType =
                                                when (inputMethod.inputTypes) {
                                                    InputTypes.NUMBER -> KeyboardType.Number
                                                    InputTypes.STRING -> KeyboardType.Text
                                                    InputTypes.PHONE -> KeyboardType.Phone
                                                    InputTypes.URI -> KeyboardType.Uri
                                                    InputTypes.EMAIL -> KeyboardType.Email
                                                    InputTypes.PASSWORD -> KeyboardType.Password
                                                    InputTypes.NUMBER_PASSWORD -> KeyboardType.NumberPassword
                                                    InputTypes.DECIMAL -> KeyboardType.Decimal
                                                    else -> KeyboardType.Text
                                                }
                                            ),
                                            value = inputMethod.value,
                                            enabled = inputMethod.enabled,
                                            onValueChange = {
                                                if (inputMethod.enabled) {
                                                    onProfileEvent(
                                                        SignHomeStore.Intent.UpdateKycValues(
                                                            inputMethod.fieldType,
                                                            it
                                                        )
                                                    )
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

                                                if (inputMethod.value.text.isEmpty()
                                                ) {
                                                    Text(
                                                        modifier = Modifier.alpha(.3f),
                                                        text = inputMethod.inputLabel,
                                                        style = MaterialTheme.typography.bodySmall
                                                    )
                                                }

                                                AnimatedVisibility(
                                                    visible = inputMethod.value.text.isNotEmpty(),
                                                    modifier = Modifier.absoluteOffset(y = -(16).dp),
                                                    enter = fadeIn() + expandVertically(),
                                                    exit = fadeOut() + shrinkVertically(),
                                                ) {
                                                    Text(
                                                        text = inputMethod.inputLabel,
                                                        color = if (hasError) MaterialTheme.colorScheme.error else primaryColor,
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
                                                        visible = inputMethod.value.text.isNotEmpty(),
                                                        enter = fadeIn() + expandVertically(),
                                                        exit = fadeOut() + shrinkVertically(),
                                                    ) {

                                                        IconButton(
                                                            modifier = Modifier.size(18.dp),
                                                            onClick = {
                                                                if (inputMethod.enabled) {
                                                                    onProfileEvent(
                                                                        SignHomeStore.Intent.UpdateKycValues(
                                                                            inputMethod.fieldType,
                                                                            TextFieldValue()
                                                                        )
                                                                    )
                                                                }
                                                            },
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
                            }

                            Row (
                                horizontalArrangement = Arrangement.End
                            ) {
                                Spacer(modifier = Modifier.weight(1f))
                                Button(
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.errorContainer,
                                        contentColor =MaterialTheme.colorScheme.error
                                    ),
                                    onClick = { showDialog = false },
                                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding
                                ) {
                                    Text("Cancel")
                                }
                                Spacer(
                                    modifier = Modifier.size(10.dp)
                                )
                                Button(
                                    enabled = !hasError,
                                    onClick = {
                                        if (conditionChecked) {
                                            witnessDataListed.map { witnessData ->
                                                //navigate to Loan Confirmation
                                                component.onAddWitnessSelected(
                                                    loanRefId = component.loanRefId,
                                                    loanType = component.loanType,
                                                    desiredAmount = component.desiredAmount,
                                                    loanPeriod = component.loanPeriod,
                                                    requiredGuarantors = component.requiredGuarantors,
                                                    loanCategory = component.loanCategory,
                                                    loanPurpose = component.loanPurpose,
                                                    loanPurposeCategory = component.loanPurposeCategory,
                                                    businessType = component.businessType,
                                                    businessLocation = component.businessLocation,
                                                    kraPin = component.kraPin,
                                                    employer = component.employer,
                                                    employmentNumber = component.employmentNumber,
                                                    grossSalary = component.grossSalary,
                                                    netSalary = component.netSalary,
                                                    memberRefId = component.memberRefId,
                                                    guarantorList = component.guarantorList,
                                                    loanPurposeCategoryCode = component.loanPurposeCategoryCode,
                                                    witnessRefId = witnessData.refId,
                                                    witnessName = witnessData.memberFirstName + " " + witnessData.memberLastName,
                                                    witnessPayrollNo = signHomeState.witnessPayrollNo.value.text
                                                )
                                            }
                                        }
                                    },
                                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding
                                ) {
                                    Text("Submit")
                                }
                            }
                        }
                    }
                }
            }
        )
    }


    Scaffold(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = MaterialTheme.colorScheme.background),
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
            NavigateBackTopBar("Add witness ", onClickContainer = {
                component.onBackNavClicked()
            })
        },
        content = { innerPadding ->
            Box(Modifier.consumeWindowInsets(innerPadding).pullRefresh(refreshState)) {
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(innerPadding)
                            .background(color = MaterialTheme.colorScheme.background),
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
                                    .background(color = MaterialTheme.colorScheme.inverseOnSurface)
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    androidx.compose.material3.IconButton(
                                        modifier = Modifier
                                            .size(25.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(
                                                MaterialTheme.colorScheme.primary.copy(
                                                    alpha = 0.5f
                                                )
                                            ),
                                        onClick = {
                                            launchPopUp = true
                                        },
                                        content = {
                                            Icon(
                                                imageVector = Icons.Outlined.ArrowCircleDown,
                                                modifier = if (launchPopUp) Modifier.size(25.dp)
                                                    .rotate(180F) else Modifier.size(25.dp),
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.onBackground.copy(
                                                    alpha = 0.7f
                                                )
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
                                        val txt = when (witnessOptions) {
                                            WitnessOptions.PHONENUMBER -> "Search Phone No."
                                            WitnessOptions.MEMBERNUMBER -> "Search Member No."
                                        }
                                        if (memberNumber == "") {
                                            Text(
                                                modifier = Modifier.alpha(.3f),
                                                text = txt,
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
                                                text = txt,
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
                                                    onClick = {
                                                        firstName = emptyTextContainer
                                                    },
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
                                    .background(color = MaterialTheme.colorScheme.inverseOnSurface)
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    androidx.compose.material3.IconButton(
                                        modifier = Modifier
                                            .size(25.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(
                                                MaterialTheme.colorScheme.primary.copy(
                                                    alpha = 0.5f
                                                )
                                            ),
                                        onClick = {
                                            memberNumber = ""
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
                                                            if (witnessDataListed.size != 1) {
                                                                witnessOptions =
                                                                    WitnessOptions.PHONENUMBER
                                                                memberNumber = item.value

                                                                authState.cachedMemberData?.let {
                                                                    ApplyLongTermLoansStore.Intent.LoadTenantByPhoneNumber(
                                                                        token = it.accessToken,
                                                                        phoneNumber = item.value
                                                                    )
                                                                }?.let {
                                                                    onEvent(
                                                                        it
                                                                    )
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        },
                                        content = {
                                            Icon(
                                                imageVector = Icons.Filled.ImportContacts,
                                                modifier = Modifier,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.onBackground.copy(
                                                    alpha = 0.7f
                                                )
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                    //list the guarantor
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.8f)
                    ) {
                        item {
                            Spacer(modifier = Modifier.padding(top = 10.dp))
                        }
                        if (witnessDataListed.isNotEmpty()) {
                            witnessDataListed.forEach { item ->
                                item {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 10.dp)
                                    ) {
                                        WitnessDetailsView(
                                            label = item.memberFirstName + " " + item.memberLastName,
                                            onClick = {
                                                //call back executed
                                                clearItemClicked(item)
                                                signHomeState.prestaTenantByMemberNumber = null
                                                state.prestaLoadTenantByPhoneNumber = null
                                            },
                                            selected = true,
                                            phoneNumber = item.memberPhoneNumber,
                                            memberNumber = item.memberNumber,
                                        )
                                    }
                                }
                            }
                        } else {
                            //Todo--message to show  how to add the witness
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
                                        "Add Witness  using phone number or member number on the above text input",
                                        color = MaterialTheme.colorScheme.onBackground,
                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.padding(horizontal = 20.dp)
                                    )
                                }
                            }
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 30.dp)
                    ) {
                        ActionButton(
                            label = if (witnessDataListed.size != 1) "Search" else "Add Witness",
                            onClickContainer = {
                                if (witnessDataListed.size != 1) {
                                    when (witnessOptions) {
                                        WitnessOptions.MEMBERNUMBER -> {
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

                                        WitnessOptions.PHONENUMBER -> {
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
                                } else {
                                    // show dialogue
                                    signHomeState.prestaClientSettings?.let {
                                        it.response.details?.let { details ->
                                            if (details.containsKey("witness_payroll_no")) {
                                                showDialog = true
                                            } else {
                                                if (conditionChecked) {
                                                    witnessDataListed.map { witnessData ->
                                                        //navigate to Loan Confirmation
                                                        component.onAddWitnessSelected(
                                                            loanRefId = component.loanRefId,
                                                            loanType = component.loanType,
                                                            desiredAmount = component.desiredAmount,
                                                            loanPeriod = component.loanPeriod,
                                                            requiredGuarantors = component.requiredGuarantors,
                                                            loanCategory = component.loanCategory,
                                                            loanPurpose = component.loanPurpose,
                                                            loanPurposeCategory = component.loanPurposeCategory,
                                                            businessType = component.businessType,
                                                            businessLocation = component.businessLocation,
                                                            kraPin = component.kraPin,
                                                            employer = component.employer,
                                                            employmentNumber = component.employmentNumber,
                                                            grossSalary = component.grossSalary,
                                                            netSalary = component.netSalary,
                                                            memberRefId = component.memberRefId,
                                                            guarantorList = component.guarantorList,
                                                            loanPurposeCategoryCode = component.loanPurposeCategoryCode,
                                                            witnessRefId = witnessData.refId,
                                                            witnessName = witnessData.memberFirstName + " " + witnessData.memberLastName
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            },
                            enabled = memberNumber != "",
                            loading = state.isLoading
                        )
                    }
                }
                PullRefreshIndicator(
                    refreshing, refreshState,
                    Modifier
                        .padding(innerPadding)
                        .align(Alignment.TopCenter).zIndex(1f),
                    contentColor = actionButtonColor
                )

            }

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
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
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
                                                            idx = guarantorIndex,
                                                            selected = selectedIndex == guarantorIndex,
                                                            onClick = { index: Int ->
                                                                selectedIndex =
                                                                    if (selectedIndex == index) -1 else index
                                                                if (selectedIndex > -1) {
                                                                    when (guarantorList[selectedIndex]) {
                                                                        state.memberNo -> {
                                                                            witnessOptions =
                                                                                WitnessOptions.MEMBERNUMBER
                                                                        }

                                                                        state.phoneNo -> {
                                                                            witnessOptions =
                                                                                WitnessOptions.PHONENUMBER
                                                                        }

                                                                    }

                                                                }
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
                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
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
                                        memberNumber = ""
                                        signHomeState.prestaTenantByMemberNumber = null
                                        state.prestaLoadTenantByPhoneNumber = null
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
                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WitnessDetailsView(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    phoneNumber: String,
    memberNumber: String,
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
                                Icons.Default.RadioButtonChecked,
                                contentDescription = "Check Box",
                                modifier = Modifier.padding(1.dp).size(35.dp),
                                tint = MaterialTheme.colorScheme.background
                            )
                    }
                }
                Column {

                    Text(
                        text = label.uppercase(),
                        modifier = Modifier
                            .padding(start = 10.dp),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Row(
                        modifier = Modifier.width(IntrinsicSize.Max),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = phoneNumber,
                            modifier = Modifier
                                .padding(start = 10.dp, end = 2.dp),
                            color = MaterialTheme.colorScheme.onBackground,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                            style = MaterialTheme.typography.bodySmall
                        )
                        Divider(
                            modifier = Modifier
                                .height(10.dp)
                                .padding(horizontal = 2.dp)
                                .width(1.dp),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = memberNumber.uppercase(),
                            modifier = Modifier
                                .padding(start = 5.dp, end = 2.dp),
                            color = MaterialTheme.colorScheme.onBackground,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.inverseOnSurface),
                    onClick = {
                        onClick()

                    },
                    content = {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            modifier = Modifier
                                .size(30.dp),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                )
                Spacer(modifier = Modifier.padding(end = 10.dp))
            }
        }
    }
}



