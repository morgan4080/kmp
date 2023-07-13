package com.presta.customer.ui.components.addGuarantors

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.presta.customer.ui.composables.InputTypes
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.composables.SearchFromPhoneBook
import com.presta.customer.ui.helpers.LocalSafeArea
import com.presta.customer.ui.theme.backArrowColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGuarantorsScreen(component: AddGuarantorsComponent) {
    var launchPopUp by remember { mutableStateOf(false) }
    Scaffold(modifier = Modifier.padding(LocalSafeArea.current), topBar = {
        NavigateBackTopBar("Add Guarantors", onClickContainer = {
            component.onBackNavClicked()

        })
    }, content = { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(innerPadding)){
                ElevatedCard(
                    onClick = {
                    },
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.background)
                ) {
                    Box(modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.background)) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically){
                                Icon(
                                    Icons.Filled.KeyboardArrowRight,
                                    contentDescription = "Forward Arrow",
                                    modifier = Modifier.clip(shape = CircleShape)
                                        .background(color = Color(0xFFE5F1F5)),
                                    tint = backArrowColor
                                )
                        }
                    }
                }
                Row(modifier = Modifier
                    .padding(start = 10.dp)){
                    SearchFromPhoneBook(
                        label = "Search  From  Phonebook",
                        inputValue = "",
                        icon = Icons.Outlined.Search,
                        inputType = InputTypes.STRING,
                        callback = {

                        }
                    )
                }

                ElevatedCard(
                    onClick = {
                    },
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.background)
                        .padding(start = 10.dp)
                ) {
                    Box(modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.background)) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically){
                            Icon(
                                Icons.Filled.KeyboardArrowRight,
                                contentDescription = "Forward Arrow",
                                modifier = Modifier.clip(shape = CircleShape)
                                    .background(color = Color(0xFFE5F1F5)),
                                tint = backArrowColor
                            )
                        }
                    }
                }
            }
        }
    })
}













