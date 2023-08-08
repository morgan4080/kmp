package com.presta.customer.ui.components.guarantorshipRequests

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.helpers.LocalSafeArea
import com.presta.customer.ui.theme.actionButtonColor
import dev.icerock.moko.resources.compose.fontFamilyResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuarantorshipRequestScreen(component: GuarantorshipRequestComponent) {
    Scaffold(modifier = Modifier.padding(LocalSafeArea.current), topBar = {
        NavigateBackTopBar("Guarantorship Requests", onClickContainer = {
            component.onBackNavClicked()
        })
    }, content = { innerPadding ->
        Column(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
            ) {
                item {
                    Spacer(modifier = Modifier.padding(top = 10.dp))
                }
                item {
                    GuarantorsRequestsView(
                        selected = false,
                        onClick = {

                        },
                        label = "Guarantor",
                        phoneNumber = "O777",
                        amount = 20.0,
                        memberNumber = "11"
                    )
                }

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
            }

            Spacer(modifier = Modifier.padding(bottom = 100.dp))

        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuarantorsRequestsView(
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
            .background(color = MaterialTheme.colorScheme.background)
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
                Icon(
                    imageVector = Icons.Filled.Contacts,
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                        .clip(shape = CircleShape)
                        .padding(start = 5.dp),
                    tint = actionButtonColor
                )
                Column(modifier = Modifier.padding(start = 30.dp)) {
                    Row() {
                        Text(
                            "Dennis Wanja ",
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.regular),
                            color = actionButtonColor,
                            fontSize = 12.sp
                        )
                        Text(
                            "requested you  to",
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.regular),
                            fontSize = 12.sp
                        )
                    }

                    Text(
                        "guarantee their loan LFRTFY of",
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.regular),
                        fontSize = 12.sp
                    )
                    Text(
                        "Ksh 334444",
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.regular),
                        color = actionButtonColor,
                        fontSize = 12.sp
                    )
                    Text(
                        "02/06/20222",
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.regular),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}













