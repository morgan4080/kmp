package com.presta.customer.ui.components.signWitnessForm.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohamedrejeb.calf.ui.dialog.AdaptiveAlertDialog
import com.presta.customer.ImageConverter
import com.presta.customer.MR
import com.presta.customer.network.longTermLoans.model.ActorType
import com.presta.customer.ui.components.applyLongTermLoan.store.ApplyLongTermLoansStore
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.signWitnessForm.SignWitnessFormComponent
import com.presta.customer.ui.composables.ActionButton
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.composables.ShimmerBrush
import com.presta.customer.ui.helpers.LocalSafeArea
import dev.icerock.moko.resources.compose.fontFamilyResource

@Composable
fun SignWitnessFormContent(
    component: SignWitnessFormComponent,
    state: ApplyLongTermLoansStore.State,
    authState: AuthStore.State,
    onEvent: (ApplyLongTermLoansStore.Intent) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }
    if (component.loanRequestRefId != "") {
        LaunchedEffect(
            authState.cachedMemberData,
            component.loanRequestRefId,
            state.prestaGuarontorshipRequests
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
    LaunchedEffect(
        authState.cachedMemberData,
        state.isLoading
    ) {
        if (state.prestaZohoSignUrl?.signURL != null) {
            component.platform.openUrl(state.prestaZohoSignUrl.signURL)
        }

    }

    LaunchedEffect(state.error) {
        showDialog = state.error !== null
    }

    Scaffold(modifier = Modifier.padding(LocalSafeArea.current), topBar = {
        NavigateBackTopBar("Sign Document", onClickContainer = {
            component.onBackNavClicked()
        })
    }, content = { innerPadding ->
        if (state.prestaLoanByLoanRequestRefId?.pdfThumbNail == null) {
            LazyColumn(modifier = Modifier.padding(innerPadding)) {
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
            }

        } else {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.85f)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxWidth()
                    ) {
                        Text(
                            "Being a Witness for  " + component.loanNumber + " of KES " + component.amount + " has been confirmed",
                            fontSize = 16.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Text(
                        "Proceed Below to sign your form",
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                    //Form Image
                    if (state.prestaLoanByLoanRequestRefId.pdfThumbNail != "") {
                        Base64ToImage(state.prestaLoanByLoanRequestRefId.pdfThumbNail)
                    }
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    ActionButton(
                        label = "SIGN DOCUMENT",
                        onClickContainer = {
                            authState.cachedMemberData?.let {
                                ApplyLongTermLoansStore.Intent.GetZohoSignUrl(
                                    token = it.accessToken,
                                    loanRequestRefId = component.loanRequestRefId,
                                    actorRefId = component.memberRefId,
                                    actorType = ActorType.WITNESS
                                )
                            }?.let {
                                onEvent(ApplyLongTermLoansStore.Intent.ClearExistingError)
                                onEvent(
                                    it
                                )
                            }
                        },
                        loading = state.isLoading
                    )
                }
            }
        }

        if (showDialog && state.error !== null) {
            AdaptiveAlertDialog(
                onConfirm = {
                    showDialog = false
                    authState.cachedMemberData?.let {
                        ApplyLongTermLoansStore.Intent.GetZohoSignUrl(
                            token = it.accessToken,
                            loanRequestRefId = component.loanRequestRefId,
                            actorRefId = component.memberRefId,
                            actorType = ActorType.WITNESS
                        )
                    }?.let {
                        onEvent(ApplyLongTermLoansStore.Intent.ClearExistingError)
                        onEvent(
                            it
                        )
                    }
                },
                onDismiss = {
                    showDialog = false
                    onEvent(ApplyLongTermLoansStore.Intent.ClearExistingError)
                    component.onBackNavClicked()
                },
                confirmText = "Retry",
                dismissText = "Dismiss",
                title = "Witness Signing Error",
                text = state.error,
            )
            component.platform.logErrorsToFirebase(Exception(state.error))
        }
    })
}

@Composable
fun Base64ToImage(base64String: String) {
    val imageConverter = ImageConverter()
    val bitmap = imageConverter.decodeBase64ToBitmap(base64String)
    Image(
        bitmap = bitmap as ImageBitmap,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .padding(top = 20.dp)
            .padding(top = 20.dp)
            .fillMaxHeight(0.9f)
            .fillMaxWidth()

    )
}




