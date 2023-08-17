package com.presta.customer.ui.components.favouriteGuarantors.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Contacts
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.presta.customer.ui.components.applyLongTermLoan.store.ApplyLongTermLoansStore
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.favouriteGuarantors.FavouriteGuarantorsComponent
import com.presta.customer.ui.components.signAppHome.store.SignHomeStore
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.helpers.LocalSafeArea
import com.presta.customer.ui.theme.actionButtonColor
import com.presta.customer.ui.theme.primaryColor
import dev.icerock.moko.resources.compose.fontFamilyResource
import androidx.compose.ui.window.Popup
import com.presta.customer.ui.composables.ActionButton
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun FavouriteGuarantorContent(
    component: FavouriteGuarantorsComponent,
    state: ApplyLongTermLoansStore.State,
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
    val skipHalfExpanded by remember { mutableStateOf(true) }
    val modalBottomState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = skipHalfExpanded
    )
    val scope = rememberCoroutineScope()
    ModalBottomSheetLayout(
        sheetState = modalBottomState,
        sheetContent = {
            Scaffold(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(color = MaterialTheme.colorScheme.background),
                topBar = {
                    NavigateBackTopBar("Add Favourite Guarantor ", onClickContainer = {
                        scope.launch { modalBottomState.hide() }
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
                        //list the guarantor
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.8f)
                        ) {


                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 30.dp)
                        ) {
                            ActionButton(
                                label = "Search",
                                onClickContainer = {

                                },
                                enabled = true
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
            )
        }
    ) {
        //list Data
        Scaffold(modifier = Modifier.padding(LocalSafeArea.current),
            floatingActionButton = {
                FloatingActionButton(
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.background,
                    onClick = {
                        //launch sheet Content
                        scope.launch { modalBottomState.show() }

                    },
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        modifier = Modifier,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.background
                    )
                }
            },
            topBar = {
                NavigateBackTopBar("Favourite Guarantors ", onClickContainer = {
                    component.onBackNavClicked()
                })
            }, content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp)
                        .fillMaxHeight()
                ) {
                    //list Guarantors from Live Data
                    LazyColumn(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxHeight(0.85f)
                    ) {

                        item {
                            FavouriteGuarantorView(
                                Index = 1,
                                selected = false,
                                onClick = {

                                },
                                deleteAccount = {

                                },
                                label = "Dennis",
                                description = null
                            )
                        }
                    }
                }
            })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouriteGuarantorView(
    Index: Int,
    selected: Boolean,
    onClick: (Int) -> Unit,
    deleteAccount: () -> Unit,
    label: String,
    description: String? = null,
) {
    ElevatedCard(
        onClick = {
            onClick.invoke(Index)
        }
    ) {
        Box(modifier = Modifier.background(color = MaterialTheme.colorScheme.inverseOnSurface)) {
            Row(
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    Spacer(modifier = Modifier.padding(end = 15.dp))
                    Card(
                        modifier = Modifier
                            .clip(shape = CircleShape)
                            .border(
                                border = BorderStroke(0.2.dp, MaterialTheme.colorScheme.outline),
                                shape = CircleShape
                            ),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(19.dp)
                                .background(if (selected) actionButtonColor else MaterialTheme.colorScheme.background)
                                .clickable {
                                    onClick.invoke(Index)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (selected) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = "Check Box",
                                    tint = Color.White,
                                    modifier = Modifier.padding(1.dp)
                                )
                            }
                        }
                    }
                }

                Column {

                    Text(
                        text = label,
                        modifier = Modifier
                            .padding(start = 15.dp),
                        fontSize = 12.sp,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold)
                    )

                    if (description != null) {
                        Text(
                            text = description,
                            modifier = Modifier.padding(start = 15.dp),
                            fontSize = 10.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                        )
                    }
                }

                Row {
                    //Created a Custom checkBox
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .size(19.dp)
                            .clickable {
                                deleteAccount()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier,
                            imageVector = Icons.Filled.Cancel,
                            contentDescription = null,
                            tint = actionButtonColor.copy(
                                alpha = 0.4f
                            )
                        )
                    }
                    Spacer(modifier = Modifier.padding(end = 15.dp))
                }
            }
        }
    }
}