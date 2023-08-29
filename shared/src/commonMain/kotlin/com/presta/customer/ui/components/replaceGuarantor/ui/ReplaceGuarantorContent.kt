package com.presta.customer.ui.components.replaceGuarantor.ui

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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
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
import com.presta.customer.MR
import com.presta.customer.ui.components.addGuarantors.ui.SelectGuarantorsView
import com.presta.customer.ui.components.addGuarantors.ui.SnackbarVisualsWithError
import com.presta.customer.ui.components.addWitness.FavouriteGuarantorDetails
import com.presta.customer.ui.components.addWitness.WitnessDetailsView
import com.presta.customer.ui.components.applyLongTermLoan.store.ApplyLongTermLoansStore
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.replaceGuarantor.ReplaceGuarantorComponent
import com.presta.customer.ui.components.signAppHome.store.SignHomeStore
import com.presta.customer.ui.composables.ActionButton
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.theme.actionButtonColor
import com.presta.customer.ui.theme.backArrowColor
import com.presta.customer.ui.theme.primaryColor
import dev.icerock.moko.resources.compose.fontFamilyResource
import kotlinx.coroutines.launch
import androidx.compose.ui.window.Popup

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReplaceGuarantorContent(
    component: ReplaceGuarantorComponent,
    applyLongTermLoanState: ApplyLongTermLoansStore.State,
    authState: AuthStore.State,
    onEvent: (ApplyLongTermLoansStore.Intent) -> Unit,
    signHomeState: SignHomeStore.State,
    onProfileEvent: (SignHomeStore.Intent) -> Unit,
) {
    var launchPopUp by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    var memberNumber by remember { mutableStateOf("") }
    var guarantorOption by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf(TextFieldValue()) }
    val emptyTextContainer by remember { mutableStateOf(TextFieldValue()) }
    var selectedIndex by remember { mutableStateOf(-1) }
    var searchGuarantorByMemberNumber by remember { mutableStateOf(false) }
    var searchGuarantorByPhoneNumber by remember { mutableStateOf(false) }
    var conditionChecked by remember { mutableStateOf(false) }
    var allowedToUpdateGuarantor by remember { mutableStateOf(false) }
    var memberRefId by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val snackBarScope = rememberCoroutineScope()
    var confirmGuarantorReplacementPopUp by remember { mutableStateOf(false) }
    var witnessDataListed by remember { mutableStateOf(emptySet<FavouriteGuarantorDetails>()) }
    var clickCount by remember { mutableStateOf(0) }
    if (signHomeState.prestaTenantByPhoneNumber?.refId != null) {
        memberRefId = signHomeState.prestaTenantByPhoneNumber.refId
    }
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
    //Todo check if the selected guarantor is already the loan guarantor
    if (component.loanRequestRefId != "") {
        LaunchedEffect(
            authState.cachedMemberData,
            component.loanRequestRefId

        ) {
            authState.cachedMemberData?.let {
                ApplyLongTermLoansStore.Intent.GetPrestaLoanByLoanRequestRefId(
                    token = it.accessToken,
                    loanRequestRefId = component.loanRequestRefId
                )
            }?.let {
                onEvent(
                    it
                )
            }
        }
    }
    val clearItemClicked: (FavouriteGuarantorDetails) -> Unit = { item ->
        witnessDataListed -= item
    }
    if (memberNumber != "") {
        LaunchedEffect(
            witnessDataListed,
            memberNumber,
        ) {
            if (signHomeState.prestaTenantByMemberNumber?.refId != null || applyLongTermLoanState.prestaLoadTenantByPhoneNumber?.refId !=null ) {
                witnessDataListed.map { datas ->
                    applyLongTermLoanState.prestaLoanByLoanRequestRefId?.guarantorList?.map { existingGuarantor ->
                        if (datas.memberNumber == signHomeState.prestaTenantByPhoneNumber?.memberNumber) {
                            clearItemClicked(datas)
                            conditionChecked = false
                            snackBarScope.launch {
                                snackbarHostState.showSnackbar(
                                    SnackbarVisualsWithError(
                                        "Self Guarantee not Allowed",
                                        isError = true
                                    )
                                )
                            }
                        } else if (datas.refId == existingGuarantor.memberRefId) {
                            clearItemClicked(datas)
                            conditionChecked = false
                            snackBarScope.launch {
                                snackbarHostState.showSnackbar(
                                    SnackbarVisualsWithError(
                                        "Member is already a guarantor",
                                        isError = true
                                    )
                                )
                            }
                        } else {
                            conditionChecked = datas.memberNumber != signHomeState.prestaTenantByPhoneNumber?.memberNumber && datas.refId != existingGuarantor.memberRefId
                                    && witnessDataListed.size == 1
                        }
                    }
                }
            }
        }
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
            NavigateBackTopBar("Replace Guarantor ", onClickContainer = {
                component.onBackNavClicked()
            })
        },
        content = { innerPadding ->
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
                                            text = if (guarantorOption == applyLongTermLoanState.memberNo) "Search Member Number" else "Search PhoneBook",
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
                                            text = if (guarantorOption == applyLongTermLoanState.memberNo) "Search Member Number" else "Search PhoneBook",
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
                                        .clip(CircleShape)
                                        .background(Color(0xFFE5F1F5))
                                        .size(25.dp),
                                    onClick = {
                                        //Todo----open Contacts Library
                                        confirmGuarantorReplacementPopUp = true

                                        //open contacts Library
//                                        component.platform.getContact().map { contact ->
//                                            println("Test Contact")
//                                            println(contact.phoneNumber)
//                                        }
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
                                            witnessDataListed = emptySet()
                                        },
                                        selected = true,
                                        phoneNumber = item.memberPhoneNumber,
                                        memberNumber = item.memberNumber,
                                    )
                                }
                            }
                        }
                    } else {
                        item {
                            Text(
                                "Add Guarantors using phone number or member number on the above text input",
                                fontSize = 12.sp,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.regular),
                                modifier = Modifier
                                    .padding(start = 10.dp, top = 10.dp)
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 30.dp)
                ) {
                    ActionButton(
                        label = if (witnessDataListed.size != 1) "Search" else "Add  Guarantor",
                        onClickContainer = {
                            witnessDataListed = emptySet()
                            if ( searchGuarantorByMemberNumber && signHomeState.prestaTenantByMemberNumber?.refId != null) {
                                if (witnessDataListed.size != 1) {
                                    val apiResponse = listOf(
                                        FavouriteGuarantorDetails(
                                            refId = signHomeState.prestaTenantByMemberNumber.refId,
                                            memberFirstName = signHomeState.prestaTenantByMemberNumber.firstName,
                                            memberNumber = signHomeState.prestaTenantByMemberNumber.memberNumber,
                                            memberLastName = signHomeState.prestaTenantByMemberNumber.lastName,
                                            memberPhoneNumber = signHomeState.prestaTenantByMemberNumber.phoneNumber
                                        )
                                    )
                                    val existingItems = witnessDataListed.toSet()
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
                                        witnessDataListed = witnessDataListed.toMutableSet().apply {
                                            addAll(apiResponse)
                                        }
                                    }
                                } else {
                                    snackBarScope.launch {
                                        snackbarHostState.showSnackbar(
                                            SnackbarVisualsWithError(
                                                "Has Data",
                                                isError = true
                                            )
                                        )
                                    }
                                }
                            } else {
                                if (searchGuarantorByMemberNumber && signHomeState.prestaTenantByMemberNumber?.refId == null){
                                    snackBarScope.launch {
                                        snackbarHostState.showSnackbar(
                                            SnackbarVisualsWithError(
                                                "Error loading Member $memberNumber",
                                                isError = true
                                            )
                                        )
                                    }
                                }
                            }
                            if ( searchGuarantorByPhoneNumber && applyLongTermLoanState.prestaLoadTenantByPhoneNumber?.refId != null) {
                                if (witnessDataListed.size != 1) {
                                    val apiResponse = listOf(
                                        FavouriteGuarantorDetails(
                                            refId = applyLongTermLoanState.prestaLoadTenantByPhoneNumber.refId,
                                            memberFirstName = applyLongTermLoanState.prestaLoadTenantByPhoneNumber.firstName,
                                            memberNumber = applyLongTermLoanState.prestaLoadTenantByPhoneNumber.memberNumber,
                                            memberLastName = applyLongTermLoanState.prestaLoadTenantByPhoneNumber.lastName,
                                            memberPhoneNumber = applyLongTermLoanState.prestaLoadTenantByPhoneNumber.phoneNumber
                                        )
                                    )
                                    val existingItems = witnessDataListed.toSet()
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
                                        witnessDataListed = witnessDataListed.toMutableSet().apply {
                                            addAll(apiResponse)
                                        }
                                    }
                                } else {
                                    snackBarScope.launch {
                                        snackbarHostState.showSnackbar(
                                            SnackbarVisualsWithError(
                                                "Guarantor Selected",
                                                isError = true
                                            )
                                        )
                                    }
                                }
                            } else {
                                if (searchGuarantorByPhoneNumber &&  applyLongTermLoanState.prestaLoadTenantByPhoneNumber == null){
                                    snackBarScope.launch {
                                        snackbarHostState.showSnackbar(
                                            SnackbarVisualsWithError(
                                                "Error loading Member by Phone Number  $memberNumber",
                                                isError = true
                                            )
                                        )
                                    }
                                }
                            }
                            if (conditionChecked) {
                                confirmGuarantorReplacementPopUp = true
                            }
                        },
                        enabled = true,
                        loading = applyLongTermLoanState.isLoading
                    )
                }
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
                                            guarantorList.add(applyLongTermLoanState.memberNo)
                                            guarantorList.add(applyLongTermLoanState.phoneNo)
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
                                                                searchGuarantorByMemberNumber = guarantorOption == applyLongTermLoanState.memberNo
                                                                searchGuarantorByPhoneNumber = guarantorOption ==  applyLongTermLoanState.phoneNo
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
                                        allowedToUpdateGuarantor=false
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
                                        //Execute Replace Guarantor
                                        //set to add guarantor
                                        allowedToUpdateGuarantor=true
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
    )
    //confirm replacing Guaranotor pop up
    if (confirmGuarantorReplacementPopUp) {
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
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Confirm you want to replace " + component.guarantorFirstName + " " + component.guarantorLastName,
                                modifier = Modifier.padding(top = 10.dp),
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.regular),
                                fontSize = 12.sp
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 20.dp,
                                bottom = 10.dp,
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        OutlinedButton(
                            border = BorderStroke(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary
                            ),
                            onClick = {
                                confirmGuarantorReplacementPopUp = false
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
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = actionButtonColor),
                            border = BorderStroke(
                                width = 0.dp,
                                color = actionButtonColor
                            ),
                            onClick = {
                                confirmGuarantorReplacementPopUp = false
                                if (allowedToUpdateGuarantor) {
                                    //confirmGuarantorReplacementPopUp =false
                                    witnessDataListed.map {newguarantor->
                                        authState.cachedMemberData?.let {
                                            ApplyLongTermLoansStore.Intent.ReplaceLoanGuarantor(
                                                token = it.accessToken,
                                                loanRequestRefId = component.loanRequestRefId,
                                                guarantorRefId = component.guarantorRefId,
                                                memberRefId = newguarantor.refId
                                            )
                                        }?.let {
                                            onEvent(
                                                it
                                            )
                                        }
                                    }
                                    snackBarScope.launch {
                                        snackbarHostState.showSnackbar(
                                            SnackbarVisualsWithError(
                                                "Guarantor Replaced successfully",
                                                isError = true
                                            )
                                        )
                                    }
                                }
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





