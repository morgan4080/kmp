package com.presta.customer.ui.components.signLoanForm.ui

import ShimmerBrush
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.ImageConverter
import com.presta.customer.MR
import com.presta.customer.network.longTermLoans.model.ActorType
import com.presta.customer.ui.components.applyLongTermLoan.store.ApplyLongTermLoansStore
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.signLoanForm.SignLoanFormComponent
import com.presta.customer.ui.composables.ActionButton
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.helpers.LocalSafeArea
import dev.icerock.moko.resources.compose.fontFamilyResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignLoanFormContent(
    component: SignLoanFormComponent,
    state: ApplyLongTermLoansStore.State,
    authState: AuthStore.State,
    onEvent: (ApplyLongTermLoansStore.Intent) -> Unit,
) {
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

    if (component.loanRequestRefId != "") {
        LaunchedEffect(
            component.loanRequestRefId,
            state.prestaLoanByLoanRequestRefId
        ) {
            authState.cachedMemberData?.let {
                ApplyLongTermLoansStore.Intent.GetZohoSignUrl(
                    token = it.accessToken,
                    loanRequestRefId = component.loanRequestRefId,
                    actorRefId = component.memberRefId,
                    actorType = ActorType.APPLICANT
                )
            }?.let {
                onEvent(
                    it
                )
            }
        }
    }

    Scaffold(modifier = Modifier.padding(LocalSafeArea.current), topBar = {
        NavigateBackTopBar("Sign Loan Form", onClickContainer = {
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
                            "Proceed to Sign your Loan " + component.loanNumber + " of Kes " + component.amount,
                            fontSize = 16.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.regular),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Text(
                        "Proceed Below to sign your Loan form",
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
                            //Todo=--already fixed this implementation
                            if (state.prestaZohoSignUrl?.signURL != null) {
                                component.platform.openUrl(state.prestaZohoSignUrl.signURL)
                            }

                        },
                        loading = state.prestaZohoSignUrl == null
                    )
                }
            }
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



