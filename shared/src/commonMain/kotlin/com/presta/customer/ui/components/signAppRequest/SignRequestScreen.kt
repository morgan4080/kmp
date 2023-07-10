package com.presta.customer.ui.components.signAppRequest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.helpers.LocalSafeArea

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignRequestScreen(component: SignRequestComponent) {
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
                            .background(color = androidx.compose.material3.MaterialTheme.colorScheme.inverseOnSurface)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = androidx.compose.material3.MaterialTheme.colorScheme.inverseOnSurface),
                            verticalAlignment = Alignment.CenterVertically

                        ) {
//                            androidx.compose.material3.MaterialTheme.colorScheme.inverseOnSurface
                            Card(modifier = Modifier){
                                Box(contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .background(color = Color.Gray)) {
                                    Column( verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier =Modifier.padding(15.dp)) {
                                        Icon(
                                            Icons.Filled.Done,
                                            contentDescription = "completed",
                                            modifier = Modifier.clip(shape = CircleShape)
                                                .background(color = MaterialTheme.colors.secondaryVariant),
                                            tint = MaterialTheme.colors.background
                                        )
                                        Text("Completed")
                                    }
                                }
                            }

                            Column(modifier = Modifier.padding(start = 5.dp)) {
                                Text("Normal")
                                Text("Loan")

                            }
                            Text(text = "ksh. 12000.00")
                        }
                    }

                    Spacer(modifier = Modifier.padding(bottom = 100.dp))
                }
            }
        })

}














