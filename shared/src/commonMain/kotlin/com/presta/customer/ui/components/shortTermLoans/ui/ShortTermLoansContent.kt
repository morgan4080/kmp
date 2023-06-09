package com.presta.customer.ui.components.shortTermLoans.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.shortTermLoans.ShortTermLoansComponent
import com.presta.customer.ui.components.shortTermLoans.store.ShortTermLoansStore
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.composables.ShortTermProductList
import com.presta.customer.ui.composables.ShortTermTopUpList
import dev.icerock.moko.resources.compose.fontFamilyResource

@Composable
fun ShortTermLoansContent(
    component: ShortTermLoansComponent,
    authState: AuthStore.State,
    state: ShortTermLoansStore.State,
    onEvent: (ShortTermLoansStore.Intent) -> Unit,
    onAuthEvent: (AuthStore.Intent) -> Unit,
    innerPadding: PaddingValues,
   ){
    Surface(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background),
        color = Color.White
    ) {
        Column(modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .fillMaxHeight()){
            Row(modifier = Modifier
                .fillMaxWidth()){
                NavigateBackTopBar("Short Term Loan", onClickContainer = {
                    component.onBackNav()

                })
            }
            Column(modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .background(color = MaterialTheme.colorScheme.background)){
                //Switching views container
                var tabIndex by remember { mutableStateOf(0) }
                val tabs = listOf("Product", "Top Up")
                Column(modifier = Modifier
                    .fillMaxWidth()) {
                    TabRow(selectedTabIndex = tabIndex,
                        containerColor = Color.White.copy(alpha = 0.5f),
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(29.dp))
                            .background(Color.Gray.copy(alpha = 0.5f)),
                        indicator = {},
                        divider ={

                        }
                    ) {
                        tabs.forEachIndexed { index, title ->

                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .clip(shape = RoundedCornerShape(70.dp))){

                                Card(
                                    shape = RoundedCornerShape(70.dp),
                                    colors = CardDefaults.cardColors(containerColor = if (tabIndex == index) Color.White else Color.Transparent),
                                    modifier = Modifier
                                        .padding(1.5.dp)){

                                    Tab(text = {
                                        Text(text = title,
                                            fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                                            fontSize = 12.sp
                                        )
                                    },
                                        selected = tabIndex == index,
                                        onClick = {
                                            tabIndex = index
                                        },
                                        modifier = Modifier
                                            .clip(shape= RoundedCornerShape(topStart = 70.dp, topEnd = 70.dp, bottomStart = 70.dp, bottomEnd = 70.dp))
                                            .background(color = if (tabIndex == index) Color.White.copy(alpha = 0.3f) else Color.White.copy(alpha = 0.1f)),
                                        selectedContentColor = Color.Black,
                                        unselectedContentColor = Color.DarkGray
                                    )
                                }
                            }
                        }
                    }
                    when (tabIndex) {
                        0 -> ShortTermProductList(component,state)
                        1 -> ShortTermTopUpList(component,state)
                    }
                }
            }
        }
    }
}