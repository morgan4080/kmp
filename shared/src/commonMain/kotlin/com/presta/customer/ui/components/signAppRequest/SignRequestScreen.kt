package com.presta.customer.ui.components.signAppRequest

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.helpers.LocalSafeArea
import com.presta.customer.ui.theme.backArrowColor
import dev.icerock.moko.resources.compose.fontFamilyResource
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun SignRequestScreen(component: SignRequestComponent) {
    var skipHalfExpanded by remember { mutableStateOf(true) }
    var showExpanded by remember { mutableStateOf(false) }//for the Animated Visibility
    val state = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = skipHalfExpanded
    )
    val scope = rememberCoroutineScope()
    ModalBottomSheetLayout(
        sheetState = state,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight(0.9f)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .padding(bottom = 100.dp)
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
                                        scope.launch { state.hide() }
                                    }
                                )
                            }
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    "Normal Loan : 12000",
                                    fontSize = 14.sp,
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                                )

                            }
                            Row(
                                modifier = Modifier
                                    .padding(top = 10.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    "88.89% Done",
                                    fontSize = 12.sp,
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                                )

                            }
                            Row(
                                modifier = Modifier
                                    .padding(top = 20.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    "GUARANTORS  STATUS",
                                    fontSize = 13.sp,
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .padding(top = 30.dp)
                                    .fillMaxWidth()
                            ) {
                                //Expand the content  of this container
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "MORGAN MUTUGI",
                                        fontSize = 13.sp,
                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                                    )
                                    //progress indicator
                                    LinearProgressWithPercentage(progress = 0.5f)
                                    IconButton(
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .padding(start = 10.dp)
                                            .background(color = MaterialTheme.colorScheme.background)
                                            .size(20.dp),
                                        onClick = {
                                            showExpanded = !showExpanded
                                        },
                                        content = {
                                            Icon(
                                                imageVector = Icons.Filled.PlayArrow,
                                                modifier = if (showExpanded) Modifier.size(
                                                    25.dp
                                                )
                                                    .rotate(90F) else Modifier.size(25.dp),
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    )
                                }
                                AnimatedVisibility(showExpanded) {
                                    //:
                                    LazyColumn(
                                        modifier = Modifier
                                            .fillParentMaxHeight(0.7f)
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
                                                    "Member can guarantee",
                                                    fontSize = 12.sp,
                                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.padding(LocalSafeArea.current),
            topBar = {
                NavigateBackTopBar(label = "Loan Requests", onClickContainer = {

                })
            },
            content = { innerPadding ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp)
                ) {
                    item {
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(innerPadding)
                                .background(color = MaterialTheme.colorScheme.background),
                            onClick = {
                                scope.launch { state.show() }
                            }
                        ) {
                            Row(
                                modifier = Modifier
                                    .wrapContentHeight()
                                    .background(color = MaterialTheme.colorScheme.inverseOnSurface)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .clip(
                                            shape = RoundedCornerShape(
                                                bottomEnd = 20.dp
                                            )
                                        )
                                        .background(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                                        .fillMaxHeight()
                                ) {
                                    Column(
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier
                                            .padding(15.dp)
                                            .fillParentMaxHeight(0.13f)

                                    ) {
                                        Icon(
                                            Icons.Filled.Done,
                                            contentDescription = "completed",
                                            modifier = Modifier.clip(shape = CircleShape)
                                                .background(color = MaterialTheme.colorScheme.primary),
                                            tint = MaterialTheme.colorScheme.background
                                        )
                                        Text(
                                            "Completed",
                                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight()
                                        .background(color = MaterialTheme.colorScheme.inverseOnSurface),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(start = 10.dp)
                                            .fillMaxHeight()
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            Text(
                                                "APPLICANT SIGNED",
                                                fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                                fontSize = 13.sp,
                                                modifier = Modifier.padding(end = 10.dp)

                                            )
                                        }
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {

                                            Column() {
                                                Text(
                                                    "Normal",
                                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                                    fontSize = 12.sp
                                                )
                                                Text(
                                                    "Loan",
                                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                                    fontSize = 12.sp
                                                )
                                            }
                                            Spacer(modifier = Modifier.weight(1f))
                                            Column() {
                                                Text(
                                                    text = "ksh. 12000000.00",
                                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                                    fontSize = 12.sp,
                                                    modifier = Modifier.padding(end = 10.dp)
                                                )
                                            }
                                        }

                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 16.dp),
                                            horizontalArrangement = Arrangement.End,
                                            verticalAlignment = Alignment.Bottom
                                        ) {
                                            Text(
                                                "27/04/2023 08:32",
                                                fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                                fontSize = 10.sp,
                                                modifier = Modifier.padding(end = 10.dp)
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

@Composable
fun LinearProgressWithPercentage(progress: Float) {
    Column(
        modifier = Modifier
            .padding(start = 20.dp)
            .fillMaxWidth(0.8f)
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(16.dp))
                    .height(10.dp)
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













