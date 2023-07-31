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
import androidx.compose.material.icons.outlined.Contacts
import androidx.compose.material.icons.outlined.Error
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
import com.presta.customer.ui.components.addGuarantors.AddGuarantorsComponent
import com.presta.customer.ui.components.applyLongTermLoan.store.ApplyLongTermLoansStore
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.signAppHome.store.SignHomeStore
import com.presta.customer.ui.composables.ActionButton
import com.presta.customer.ui.composables.EmployedDetails
import com.presta.customer.ui.composables.InputTypes
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.composables.SelfEmployedDetails
import com.presta.customer.ui.composables.TextInputContainer
import com.presta.customer.ui.helpers.LocalSafeArea
import com.presta.customer.ui.theme.actionButtonColor
import com.presta.customer.ui.theme.backArrowColor
import com.presta.customer.ui.theme.primaryColor
import dev.icerock.moko.resources.compose.fontFamilyResource
import kotlinx.coroutines.launch

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

data class GuarantorData(val name: String, val memberNum: String)

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
    var skipHalfExpanded by remember { mutableStateOf(true) }
    var guarantorOption by remember { mutableStateOf("") }
    var launchCheckSelfAndEmPloyedPopUp by remember { mutableStateOf(false) }
    var allConditionsChecked by remember { mutableStateOf(false) }
    var searchGuarantorByMemberNumber by remember { mutableStateOf(false) }
    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = skipHalfExpanded
    )
    var memberNumber by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val snackBarScope = rememberCoroutineScope()
    var launchAddAmountToGuarantee by remember { mutableStateOf(false) }
    var guarantor by remember { mutableStateOf("") }
    var listing1 by remember { mutableStateOf("") }
    var listing2 by remember { mutableStateOf("") }

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
        LaunchedEffect(memberNumber) {
            guarantor = signHomeState.prestaTenantByMemberNumber?.firstName.toString()

        }
    }
    val scope = rememberCoroutineScope()

    //Todo--Some  loans needs more than one guarantor
    //get loan by The refid and check the  number of guarantors

    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            if (launchCheckSelfAndEmPloyedPopUp && !searchGuarantorByMemberNumber) {
                val tabs = listOf("Employed", "Business/Self Employed")
                var tabIndex by remember { mutableStateOf(0) }
                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
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
                                selectedContentColor = Color.Black,
                                unselectedContentColor = Color.DarkGray
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
                            onClickContainer = {
                                allConditionsChecked = true
                                scope.launch { modalBottomSheetState.hide() }
                            }
                        )

                        1 -> SelfEmployedDetails()
                    }

                }
            } else if (launchAddAmountToGuarantee && signHomeState.prestaTenantByMemberNumber?.firstName != null) {
                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(top = 5.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
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
                            //Guarantor  Morgan
                            Text(
                                text = "Guarantor: " + signHomeState.prestaTenantByMemberNumber.firstName,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                fontSize = 12.sp
                            )
                        }
                        Row(
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Remaining Amount: 10.00",
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                fontSize = 12.sp
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp)
                        ) {
                            TextInputContainer(
                                "Amount to  guarantee",
                                "",
                                inputType = InputTypes.NUMBER
                            ) {
                                val inputValue: Double? = TextFieldValue(it).text.toDoubleOrNull()

                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .padding(top = 50.dp, bottom = 50.dp)
                        ) {
                            ActionButton("SUBMIT", onClickContainer = {

                                scope.launch { modalBottomSheetState.hide() }

                            }, enabled = true)
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
                                                text = "Search From Phonebook",
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
                                                text = "Search From Phonebook",
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
                                            snackBarScope.launch {
                                                snackbarHostState.showSnackbar(
                                                    SnackbarVisualsWithError(
                                                        "Error loading Member",
                                                        isError = true
                                                    )
                                                )
                                            }
//                                            snackBarScope.launch {
//                                                snackbarHostState.showSnackbar(
//                                                    "member data"
//                                                )
//                                            }

                                            //open contacts Library
//                                        component.platform.getContact().map { contact ->
//                                            println("Test Contact")
//                                            println(contact.phoneNumber)
//                                        }
                                        },
                                        content = {
                                            Icon(
                                                imageVector = Icons.Outlined.Contacts,
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
                            "SELECTED GUARANTORS" + "(REQUIRES" + component.requiredGuarantors+ "GUARANTOR)",
                            fontSize = 12.sp
                        )
                    }
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.8f)
                    ) {

                        if (guarantorOption == "") {
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
                        } else {
                            if (guarantorOption == state.selfGuarantee || guarantorOption == state.memberNo) {

                                if(signHomeState.prestaTenantByMemberNumber?.firstName!=null){
                                    listing1=signHomeState.prestaTenantByMemberNumber.firstName
                                    listing2=signHomeState.prestaTenantByMemberNumber.memberNumber
                                }

                                //mutable value of
                                val guarantorListing = listOf(
                                    signHomeState.prestaTenantByPhoneNumber?.firstName?.let {
                                        signHomeState.prestaTenantByPhoneNumber.memberNumber.let { it1 ->
                                            GuarantorData(
                                                it, it1
                                            )
                                        }
                                    },
                                    GuarantorData(listing1,listing2)
                                )

                                guarantorListing.map { person ->
                                    if (person != null) {
                                        if (person.name!="" &&  person.memberNum!=""){
                                            item {
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(top = 30.dp)
                                                        .background(MaterialTheme.colorScheme.background)
                                                ) {
                                                    //mutable value  of the text label
                                                    //The Details are either user details or the selected user
                                                    //some loans may require more than one guarantor
                                                    GuarantorsDetailsView(
                                                        label = "Details",
                                                        onClick = {
                                                        },
                                                        selected = true,
                                                        phoneNumber = person.name,
                                                        memberNumber = person.memberNum,
                                                        amount = 20.0
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    //Action Button
                    Row(
                        modifier = Modifier
                            .padding(top = 30.dp)
                            .fillMaxWidth()
                    ) {
                        ActionButton(
                            label = if (searchGuarantorByMemberNumber) "Search" else "Continue",
                            onClickContainer = {
                                println("Test Guarantor")
                                println(guarantor)
                                if (searchGuarantorByMemberNumber && signHomeState.prestaTenantByMemberNumber?.firstName != null) {
                                    launchCheckSelfAndEmPloyedPopUp = false
                                    launchAddAmountToGuarantee = true
                                    scope.launch { modalBottomSheetState.show() }
                                } else {
                                    scope.launch { modalBottomSheetState.show() }
                                    launchAddAmountToGuarantee = false
                                    launchCheckSelfAndEmPloyedPopUp = true
                                    if (allConditionsChecked) {
                                        //Transfer Relevant Data to Confirm
                                        component.onContinueSelected(
                                            component.loanRefId,
                                            component.loanType,
                                            component.desiredAmount,
                                            component.loanPeriod,
                                            component.requiredGuarantors,
                                            component.loanCategory,
                                            component.loanPurpose,
                                            component.loanPurposeCategory
                                        )
                                        launchCheckSelfAndEmPloyedPopUp = false
                                        scope.launch { modalBottomSheetState.hide() }
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
                                            "0PTIONS",
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
                                                    guarantorList.add(state.selfGuarantee)
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
                                                                            guarantorOption == state.memberNo || guarantorOption == state.phoneNo
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
    onClick: (Int) -> Unit,
    label: String,
    phoneNumber: String,
    memberNumber: String,
    amount: Double
) {
    ElevatedCard(
        onClick = {
            //onClick.invoke(Index)
        },
        modifier = Modifier.fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(defaultElevation = 20.dp)
    ) {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            Row(
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
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
                            .background(if (selected) actionButtonColor else MaterialTheme.colorScheme.background)
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
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
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
                                .height(20.dp)
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
                                .height(20.dp)
                                .padding(start = 2.dp)
                                .width(1.dp)
                        )
                        Text(
                            text = amount.toString(),
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
                        .clip(CircleShape)
                        .background(Color(0xFFE5F1F5))
                        .size(25.dp),
                    onClick = {
                        //open contacts Library

                    },
                    content = {
                        Icon(
                            imageVector = Icons.Outlined.Contacts,
                            modifier = Modifier,
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