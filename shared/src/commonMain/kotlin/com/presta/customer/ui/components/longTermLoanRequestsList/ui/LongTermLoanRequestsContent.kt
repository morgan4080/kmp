package com.presta.customer.ui.components.longTermLoanRequestsList.ui

import ShimmerBrush
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.ui.components.addGuarantors.ui.SnackbarVisualsWithError
import com.presta.customer.ui.components.applyLongTermLoan.store.ApplyLongTermLoansStore
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.longTermLoanRequestsList.LongTermLoanRequestsComponent
import com.presta.customer.ui.components.signAppHome.store.SignHomeStore
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.helpers.LocalSafeArea
import com.presta.customer.ui.helpers.formatMoney
import com.presta.customer.ui.theme.backArrowColor
import dev.icerock.moko.resources.compose.fontFamilyResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LongTermLoanRequestsContent(
    component: LongTermLoanRequestsComponent,
    state: ApplyLongTermLoansStore.State,
    authState: AuthStore.State,
    onEvent: (ApplyLongTermLoansStore.Intent) -> Unit,
    signHomeState: SignHomeStore.State
) {
    val skipHalfExpanded by remember { mutableStateOf(true) }
    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = skipHalfExpanded
    )
    var memBerRefId by remember { mutableStateOf("") }
    var loanRequestRefId by remember { mutableStateOf("") }
    var loanAmount by remember { mutableStateOf("") }
    var loanNumber by remember { mutableStateOf("") }
    var loanRequestNumber by remember { mutableStateOf("") }
    var selectedIndex by remember { mutableStateOf(-1) }
    var guarantorFirstName by remember { mutableStateOf("") }
    var guarantorLastName by remember { mutableStateOf("") }
    var guarantorRefId by remember { mutableStateOf("") }
    var appliCantSigned by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val snackBarScope = rememberCoroutineScope()
    val coroutineScope = CoroutineScope(Dispatchers.Main)
    if (signHomeState.prestaTenantByPhoneNumber?.refId != null) {
        memBerRefId = signHomeState.prestaTenantByPhoneNumber.refId
    }

    val scope = rememberCoroutineScope()
    if (signHomeState.prestaTenantByPhoneNumber?.refId != null) {
        LaunchedEffect(
            authState.cachedMemberData,
            memBerRefId

        ) {
            authState.cachedMemberData?.let {
                ApplyLongTermLoansStore.Intent.GetPrestaLongTermLoansRequestsList(
                    token = it.accessToken,
                    memberRefId = memBerRefId
                )

            }?.let {
                onEvent(
                    it
                )
            }
        }
    }
    if (loanRequestRefId != "") {
        LaunchedEffect(
            authState.cachedMemberData,
            loanRequestRefId

        ) {
            authState.cachedMemberData?.let {
                ApplyLongTermLoansStore.Intent.GetPrestaLoanByLoanRequestRefId(
                    token = it.accessToken,
                    loanRequestRefId = loanRequestRefId
                )
            }?.let {
                onEvent(
                    it
                )
            }
        }
    }
    ModalBottomSheetLayout(
        sheetState = modalBottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContentColor = MaterialTheme.colorScheme.background,
        sheetBackgroundColor = MaterialTheme.colorScheme.background,
        sheetContent = {

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.background)
            ) {
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

                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = if (state.prestaLoanByLoanRequestRefId?.loanProductName != null) state.prestaLoanByLoanRequestRefId.loanProductName.uppercase() + " " + formatMoney(
                            state.prestaLoanByLoanRequestRefId.loanAmount
                        ) else "",
                        fontSize = 16.sp,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.bold)
                    )

                }
                Row(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = if (state.prestaLoanByLoanRequestRefId?.loanRequestProgress != null) state.prestaLoanByLoanRequestRefId.loanRequestProgress.toString() + "% DONE" else "",
                        fontSize = 12.sp,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                    )

                }
                if (state.prestaLoanByLoanRequestRefId?.applicantSigned == false) {
                    Row(
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .fillMaxWidth()
                    ) {
                        OutlinedButton(
                            onClick = {
                                //sign the application form
                                //navigate to sign
                                scope.launch { modalBottomSheetState.hide() }
                                if (loanAmount != "") {
                                    signHomeState.prestaTenantByPhoneNumber?.let {
                                        component.navigateToSignLoanForm(
                                            loanNumber = loanNumber,
                                            amount = loanAmount.toDouble(),
                                            loanRequestRefId = loanRequestRefId,
                                            memberRefId = it.refId,
                                        )
                                    }
                                }
                            },
                            modifier = Modifier.padding(top = 16.dp),
                            shape = CircleShape,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text(
                                modifier = Modifier,
                                text = "Sign Form",
                                color = MaterialTheme.colorScheme.background,
                                fontSize = 12.sp,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                            )
                        }
                        OutlinedButton(
                            onClick = {
                                //Todo---delete loan Request---delay checking condition to wait for execution
                                //Delete the loan request
                                if (loanRequestNumber != "") {
                                    authState.cachedMemberData?.let {
                                        ApplyLongTermLoansStore.Intent.DeleteLoanRequest(
                                            token = it.accessToken,
                                            loanRequestNumber = loanRequestNumber
                                        )

                                    }?.let {
                                        onEvent(
                                            it
                                        )
                                    }
                                }
                                coroutineScope.launch {
                                    delay(500)
                                    if (state.deleteLoanRequestResponse.toString() == "SUCCESS") {
                                        snackBarScope.launch {
                                            snackbarHostState.showSnackbar(
                                                SnackbarVisualsWithError(
                                                    "Loan Deleted successfully",
                                                    isError = true
                                                )
                                            )
                                        }
                                    }
                                }
                                scope.launch { modalBottomSheetState.hide() }
                            },
                            modifier = Modifier
                                .padding(
                                    top = 16.dp,
                                    start = 10.dp
                                ),
                            shape = CircleShape,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text(
                                modifier = Modifier,
                                text = "Void request",
                                color = MaterialTheme.colorScheme.background,
                                fontSize = 12.sp,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        "GUARANTORS  STATUS",
                        fontSize = 13.sp,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.regular),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                //guarantor Listing
                //Todo---- list Guarantor Card
                state.prestaLoanByLoanRequestRefId?.guarantorList?.mapIndexed { index, guarantorDataResponse ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(bottom = 10.dp)
                    ) {
                        GuarantorDataCard(
                            loanProductName = guarantorDataResponse.firstName + " " + guarantorDataResponse.lastName,
                            loanAmount = "Kes 200",
                            loanApplicationDate = "12/3/44",
                            loanProgress = "",
                            loanApplicationProgress = 2.4f,
                            onClick = { indexed: Int ->
                                selectedIndex = if (selectedIndex == index) -1 else indexed
                                guarantorFirstName = guarantorDataResponse.firstName
                                guarantorLastName = guarantorDataResponse.lastName
                                guarantorRefId = guarantorDataResponse.refId

                            },
                            expandContent = selectedIndex == index,
                            index = index,
                            dataListingColumn = {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .padding(top = 20.dp)
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            "Eligibility Status",
                                            fontSize = 12.sp,
                                            fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                                        )
                                        Text(
                                            guarantorDataResponse.eligibilityMessage,
                                            fontSize = 12.sp,
                                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                                        )
                                    }
                                    Row(
                                        modifier = Modifier
                                            .padding(top = 20.dp)
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            "Committed Amount",
                                            fontSize = 12.sp,
                                            fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                                        )
                                        Text(
                                            text = formatMoney(guarantorDataResponse.committedAmount),
                                            fontSize = 12.sp,
                                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                                        )
                                    }
                                    Row(
                                        modifier = Modifier
                                            .padding(top = 20.dp)
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            "Guarantorship Status",
                                            fontSize = 12.sp,
                                            fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                                        )
                                        Text(
                                            text = if (guarantorDataResponse.isActive) "Active" else "Not Active",
                                            fontSize = 12.sp,
                                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                                        )
                                    }
                                    Row(
                                        modifier = Modifier
                                            .padding(top = 20.dp)
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            "Acceptance  Status",
                                            fontSize = 12.sp,
                                            fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                                        )
                                        Text(
                                            text = if (guarantorDataResponse.isAccepted) "Accepted " else "Pending",
                                            fontSize = 12.sp,
                                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                                        )
                                    }
                                    Row(
                                        modifier = Modifier
                                            .padding(top = 20.dp)
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            "Date Accepted",
                                            fontSize = 12.sp,
                                            fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                                        )
                                        Text(
                                            text = "",
                                            fontSize = 12.sp,
                                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                                        )
                                    }
                                    Row(
                                        modifier = Modifier
                                            .padding(top = 20.dp)
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            "Signature Status",
                                            fontSize = 12.sp,
                                            fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                                        )
                                        Text(
                                            text = if (guarantorDataResponse.isSigned) "Signed" else "Pending",
                                            fontSize = 12.sp,
                                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                                        )
                                    }
                                    Row(
                                        modifier = Modifier
                                            .padding(top = 20.dp)
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            "Approval Status",
                                            fontSize = 12.sp,
                                            fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                                        )
                                        Text(
                                            text = "",
                                            fontSize = 12.sp,
                                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                                        )
                                    }
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        OutlinedButton(
                                            onClick = {
                                                //Todo--proceed to replace  guaarantor


                                            },
                                            modifier = Modifier.padding(top = 16.dp),
                                            shape = CircleShape,
                                            border = BorderStroke(
                                                1.dp,
                                                MaterialTheme.colorScheme.onBackground
                                            ),
                                            colors = ButtonDefaults.outlinedButtonColors(
                                                containerColor = MaterialTheme.colorScheme.onBackground
                                            )
                                        ) {
                                            Text(
                                                modifier = Modifier,
                                                text = "Replace  Guarantor",
                                                color = MaterialTheme.colorScheme.background,
                                                fontSize = 12.sp
                                            )
                                        }
                                    }
                                }
                            }
                        )
                    }
                }


//                        state.prestaLoanByLoanRequestRefId?.guarantorList?.mapIndexed { index, guarantorDataResponse ->
//                            GuarantorListingContainer(
//                                guarantorName = guarantorDataResponse.firstName.uppercase() + " " + guarantorDataResponse.lastName.uppercase(),
//                                index = index,
//                                onClick = { indexed: Int ->
//                                    selectedIndex = if (selectedIndex == index) -1 else indexed
//                                    //take the clicked guarantor name
//                                    guarantorFirstName = guarantorDataResponse.firstName
//                                    guarantorLastName = guarantorDataResponse.lastName
//                                    guarantorRefId = guarantorDataResponse.refId
//                                },
//                                expandContent = selectedIndex == index,
//                                committedAmount = formatMoney(guarantorDataResponse.committedAmount),
//                                eligibilityStatus = guarantorDataResponse.eligibilityMessage,
//                                guarantorShipStatus = if (guarantorDataResponse.isActive) "Active" else "Not Active",
//                                signatureStatus = if (guarantorDataResponse.isSigned) "Signed" else "Pending",
//                                acceptanceStatus = if (guarantorDataResponse.isAccepted) "Accepted " else "Pending",
//                                onClickReplaceGuarantor = {
//                                    scope.launch { modalBottomSheetState.hide() }
//                                    component.navigateToReplaceGuarantor(
//                                        loanRequestRefId = loanRequestRefId,
//                                        guarantorRefId = guarantorRefId,
//                                        guarantorFirstname = guarantorFirstName,
//                                        guarantorLastName = guarantorLastName
//                                    )
//                                }
//                            )
//                        }
            }


        }
    ) {
        Scaffold(
            modifier = Modifier.padding(LocalSafeArea.current),
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
                        modifier = Modifier.padding(bottom = 80.dp, start = 16.dp, end = 16.dp),
                        action = {
                            androidx.compose.material.IconButton(
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
                NavigateBackTopBar(label = "Loan Requests", onClickContainer = {
                    component.navigateToHome()
                })
            },
            content = { innerPadding ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.95f)
                        .padding(start = 16.dp, end = 16.dp)
                ) {
                    //Requests List
                    item {
                        Spacer(modifier = Modifier.padding(innerPadding))
                    }
                    if (state.isLoading) {
                        items(6) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 10.dp, start = 16.dp, end = 16.dp)
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
                        if (state.prestaLongTermLoansRequestsList?.content.isNullOrEmpty()) {
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 70.dp),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Inventory2,
                                            modifier = Modifier
                                                .size(70.dp),
                                            contentDescription = "No data",
                                            tint = MaterialTheme.colorScheme.outline
                                        )
                                    }
                                    Text(
                                        "Whoops",
                                        fontSize = 13.sp,
                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                                    )
                                    Text(
                                        "No Data",
                                        fontSize = 10.sp,
                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                                    )
                                }
                            }
                        } else {
                            state.prestaLongTermLoansRequestsList?.content?.map { loanlistingData ->
                                item {
                                    Row(modifier = Modifier.padding(bottom = 20.dp)) {
                                        LoanApplicationProgressCard(
                                            loanProductName = loanlistingData.loanProductName,
                                            loanAmount = "Kes " + loanlistingData.loanAmount.toString(),
                                            loanApplicationDate = loanlistingData.loanDate,
                                            loanProgress = "${loanlistingData.loanRequestProgress}% ${loanlistingData.applicationStatus}",
                                            loanApplicationProgress = loanlistingData.loanRequestProgress.toFloat() / 100,
                                            onClickContainer = {
                                                loanRequestRefId = loanlistingData.refId
                                                loanAmount = loanlistingData.loanAmount.toString()
                                                loanNumber = loanlistingData.loanRequestNumber
                                                loanRequestNumber =
                                                    loanlistingData.loanRequestNumber
                                                scope.launch { modalBottomSheetState.show() }

                                            })
                                    }
                                }

                            }


//                            state.prestaLongTermLoansRequestsList?.content?.map { loanlistingData ->
//                                appliCantSigned = loanlistingData.applicantSigned
//                                item {
//                                    Column(
//                                        modifier = Modifier
//                                            .fillMaxWidth()
//                                            .padding(bottom = 10.dp)
//                                    ) {
//                                        LongTermLoanRequestsListContainer(
//                                            onClickContainer = {
//                                                //Todo--check the passed is
//                                                println("passed id is;;;;;;;;; ")
//                                                loanRequestRefId = loanlistingData.refId
//                                                loanAmount = loanlistingData.loanAmount.toString()
//                                                loanNumber = loanlistingData.loanRequestNumber
//                                                loanRequestNumber =
//                                                    loanlistingData.loanRequestNumber
//                                                scope.launch { modalBottomSheetState.show() }
//                                            },
//                                            loanProductName = loanlistingData.loanProductName,
//                                            applicationComplete = false,
//                                            loanAmount = "Ksh. ${formatMoney(loanlistingData.loanAmount)}",
//                                            loanApplicationProgress = loanlistingData.loanRequestProgress.toFloat() / 100,
//                                            applicantSigned = if (loanlistingData.applicantSigned) "APPLICANT SIGNED" else "APPLICANT SIGN PENDING",
//                                            applicantHasSigned = loanlistingData.applicantSigned
//                                        )
//                                    }
//
//                                }
//                            }

                            item {
                                Spacer(modifier = Modifier.padding(top = 100.dp))
                            }

                        }
                    }
                }
            })
    }
}

@Composable
fun GuarantorListingContainer(
    guarantorName: String,
    index: Int,
    onClick: (Int) -> Unit,
    expandContent: Boolean,
    committedAmount: String,
    eligibilityStatus: String,
    guarantorShipStatus: String,
    signatureStatus: String,
    acceptanceStatus: String,
    onClickReplaceGuarantor: () -> Unit,

    ) {
    var showExpanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .padding(top = 30.dp)
            .fillMaxWidth()
    ) {
        //Expand the content  of this container
        //Guarantor Listing
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                guarantorName,
                fontSize = 13.sp,
                fontFamily = fontFamilyResource(MR.fonts.Poppins.bold)
            )
            //progress indicator

            LinearProgressWithPercentage(progress = 0.0f,
                onClick = {
                    onClick.invoke(index)
                    showExpanded = !showExpanded
                })

            IconButton(
                modifier = Modifier
                    .clip(CircleShape)
                    .padding(start = 10.dp)
                    .background(color = MaterialTheme.colorScheme.background)
                    .size(20.dp),
                onClick = {
                    onClick.invoke(index)
                    showExpanded = !showExpanded
                },
                content = {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        modifier = if (expandContent) Modifier.size(
                            25.dp
                        )
                            .rotate(90F) else Modifier.size(25.dp),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            )
        }
        AnimatedVisibility(expandContent) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight(1f)
                    .fillMaxHeight()
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Eligibility Status",
                            fontSize = 12.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                        )
                        Text(
                            eligibilityStatus,
                            fontSize = 12.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                        )
                    }
                }
                item {
                    Row(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Committed Amount",
                            fontSize = 12.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                        )
                        Text(
                            text = committedAmount,
                            fontSize = 12.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                        )
                    }
                }
                item {
                    Row(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Guarantorship Status",
                            fontSize = 12.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                        )
                        Text(
                            text = guarantorShipStatus,
                            fontSize = 12.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                        )
                    }
                }
                item {
                    Row(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Signature Status",
                            fontSize = 12.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                        )
                        Text(
                            text = signatureStatus,
                            fontSize = 12.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                        )
                    }
                }
                item {
                    Row(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Approval Status",
                            fontSize = 12.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                        )
                        Text(
                            text = "",
                            fontSize = 12.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                        )
                    }
                }
                item {
                    Row(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Acceptance  Status",
                            fontSize = 12.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                        )
                        Text(
                            text = acceptanceStatus,
                            fontSize = 12.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                        )
                    }
                }
                item {
                    Row(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Date Accepted",
                            fontSize = 12.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                        )
                        Text(
                            text = "",
                            fontSize = 12.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                        )
                    }
                }
                //Replace guarantor
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(
                            onClick = onClickReplaceGuarantor,
                            modifier = Modifier.padding(top = 16.dp),
                            shape = CircleShape,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.onBackground)
                        ) {
                            Text(
                                modifier = Modifier,
                                text = "Replace  Guarantor",
                                color = MaterialTheme.colorScheme.background,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LongTermLoanRequestsListContainer(
    onClickContainer: () -> Unit,
    loanProductName: String,
    applicationComplete: Boolean,
    loanAmount: String,
    loanApplicationProgress: Float,
    applicantSigned: String,
    applicantHasSigned: Boolean
) {
    val progress by remember { mutableStateOf(loanApplicationProgress) }
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background),
        shape = RoundedCornerShape(size = 25.dp),
        onClick = onClickContainer
    ) {
        Row(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.outline.copy(0.3f),
                        shape = RoundedCornerShape(
                            topStart = 0.dp,
                            bottomStart = 0.dp,
                            bottomEnd = 20.dp
                        )
                    ),
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            start = 10.dp, end = 10.dp,
                            top = 25.dp
                        )
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    //show different icon based on the loan status
                    if (applicationComplete) {
                        Column() {
                            Icon(
                                Icons.Filled.DoneAll,
                                contentDescription = "completed",
                                modifier = Modifier.clip(shape = CircleShape)
                                    .background(color = MaterialTheme.colorScheme.secondary)
                                    .padding(5.dp),
                                tint = MaterialTheme.colorScheme.background
                            )
                            Text(
                                "Completed",
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                fontSize = 13.sp,
                                modifier = Modifier.padding(
                                    top = 10.dp,
                                    bottom = 25.dp
                                )
                            )
                        }
                    } else {
                        Column() {
                            LoanApplicationProgress(
                                progress = progress,
                                text = "${(progress * 100).toInt()}%",
                                modifier = Modifier.padding(start = 1.dp)
                            )
                            Text(
                                "In Progress",
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                fontSize = 13.sp,
                                modifier = Modifier.padding(
                                    top = 10.dp,
                                    bottom = 25.dp
                                )
                            )
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp)
                    .wrapContentHeight()
                    .background(color = MaterialTheme.colorScheme.inverseOnSurface),
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = applicantSigned,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                            fontSize = 13.sp,
                            modifier = Modifier.padding(
                                end = 15.dp,
                                top = 5.dp
                            ),
                            color = if (applicantHasSigned) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column() {
                            Text(
                                text = loanProductName,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                fontSize = 12.sp
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Column() {
                            Text(
                                text = loanAmount,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                fontSize = 12.sp,
                                modifier = Modifier.padding(end = 15.dp)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(top = 20.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            "27/04/2023 08:32",
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                            fontSize = 10.sp,
                            modifier = Modifier.padding(end = 15.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LinearProgressWithPercentage(
    progress: Float,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
            .clickable {
                onClick()
            }
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(16.dp))
                    .height(10.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
            )

        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Accepted",
                fontSize = 7.sp,
                fontFamily = fontFamilyResource(MR.fonts.Poppins.light)

            )
            Text(
                "Signed",
                fontSize = 7.sp,
                fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
            )
            Text(
                "Approved",
                fontSize = 7.sp,
                fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
            )
        }
    }
}

@Composable
fun LoanApplicationProgress(
    progress: Float,
    text: String,
    strokeWidth: Dp = 2.dp,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Box(modifier = modifier.size(50.dp)) {
        CircularProgressIndicator(
            progress = 1f,
            strokeWidth = strokeWidth,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
            modifier = Modifier.align(Alignment.Center)
        )
        CircularProgressIndicator(
            progress = progress,
            strokeWidth = strokeWidth,
            color = color,
            modifier = Modifier.align(Alignment.Center)
        )

        Text(
            text = text,
            modifier = Modifier.align(Alignment.Center),
            style = TextStyle(fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanApplicationProgressCard(
    loanProductName: String,
    loanAmount: String,
    loanApplicationDate: String,
    loanProgress: String,
    loanApplicationProgress: Float,
    onClickContainer: () -> Unit,

    ) {
    ElevatedCard(
        onClick = onClickContainer,
        modifier = Modifier.fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Box(modifier = Modifier.background(color = MaterialTheme.colorScheme.inverseOnSurface)) {
            Column(
                modifier = Modifier.padding(top = 15.dp, bottom = 17.dp, start = 17.dp, end = 17.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = loanProductName,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold),
                        fontSize = 14.sp
                    )
                    Text(
                        text = loanAmount,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold),
                        fontSize = 14.sp
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(top = 11.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = loanApplicationDate,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.8f)
                    )
                    Text(
                        text = loanProgress,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.regular),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    LinearProgressIndicator(
                        progress = loanApplicationProgress,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(shape = RoundedCornerShape(16.dp))
                            .height(10.dp),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                    )

                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuarantorDataCard(
    loanProductName: String,
    loanAmount: String,
    loanApplicationDate: String,
    loanProgress: String,
    loanApplicationProgress: Float,
    onClick: (Int) -> Unit,
    expandContent: Boolean,
    index: Int,
    dataListingColumn: @Composable () -> Unit,
) {
    var showExpanded by remember { mutableStateOf(false) }
    ElevatedCard(
        onClick = {
            onClick.invoke(index)
            showExpanded = !showExpanded

        },
        modifier = Modifier.fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Box(modifier = Modifier.background(color = MaterialTheme.colorScheme.inverseOnSurface)) {
            Column(
                modifier = Modifier.padding(top = 15.dp, bottom = 17.dp, start = 17.dp, end = 17.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = loanProductName,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold),
                        fontSize = 14.sp
                    )
                    Text(
                        text = loanAmount,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold),
                        fontSize = 14.sp
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 11.dp)
                ) {
                    LinearProgressWithPercentage(progress = 0.3f,
                        onClick = {
                            onClick.invoke(index)
                            showExpanded = !showExpanded

                        })
                }
                //Animated Visibility
                AnimatedVisibility(expandContent) {
                    dataListingColumn()
                }


            }
        }
    }
}



