package com.presta.customer.ui.components.longTermLoanConfirmation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.ui.components.applyLongTermLoan.store.ApplyLongTermLoansStore
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.longTermLoanConfirmation.LongTermLoanConfirmationComponent
import com.presta.customer.ui.components.signAppHome.store.SignHomeStore
import com.presta.customer.ui.composables.LoanInformation
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.composables.UserInformation
import com.presta.customer.ui.helpers.LocalSafeArea
import dev.icerock.moko.resources.compose.fontFamilyResource


@Composable
fun LongTermLoanConfirmationContent(
    component: LongTermLoanConfirmationComponent,
    signProfileState: SignHomeStore.State,
    authState: AuthStore.State,
    onLongTermLoanEvent: (ApplyLongTermLoansStore.Intent) -> Unit,
    state: ApplyLongTermLoansStore.State,
    onSignProfileEvent: (SignHomeStore.Intent) -> Unit,
) {
    val tabs = listOf("User Information", "Loan Information")
    var tabIndex by remember { mutableStateOf(0) }
    Scaffold(topBar = {
        NavigateBackTopBar("Confirmation", onClickContainer = {
            component.onBackNavClicked()
        })
    }, content = { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                TabRow(selectedTabIndex = tabIndex,
                    containerColor = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.5f),//Color.White.copy(alpha = 0.5f),
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(29.dp))
                        .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                    indicator = {},
                    divider = {}
                ) {
                    tabs.forEachIndexed { index, title ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(shape = RoundedCornerShape(70.dp))
                        ) {
                            Card(
                                shape = RoundedCornerShape(70.dp),
                                colors = CardDefaults.cardColors(containerColor = if (tabIndex == index) MaterialTheme.colorScheme.inverseOnSurface else Color.Transparent),
                                modifier = Modifier
                                    .padding(1.5.dp)
                            ) {

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
                                    modifier = Modifier
                                        .clip(
                                            shape = RoundedCornerShape(
                                                topStart = 70.dp,
                                                topEnd = 70.dp,
                                                bottomStart = 70.dp,
                                                bottomEnd = 70.dp
                                            )
                                        )
                                        .background(
                                            color = if (tabIndex == index) MaterialTheme.colorScheme.inverseOnSurface.copy(
                                                0.3f
                                            ) else MaterialTheme.colorScheme.inverseOnSurface.copy(
                                                0.1f
                                            )
                                        ),
                                    selectedContentColor = MaterialTheme.colorScheme.surfaceTint,
                                    unselectedContentColor = MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                    }
                }
                when (tabIndex) {
                    0 -> UserInformation(
                        component,
                        authState,
                        signProfileState,
                        onLongTermLoanEvent,
                        state,
                        onSignProfileEvent
                    )

                    1 -> LoanInformation(component)
                }
            }
        }
    })
}