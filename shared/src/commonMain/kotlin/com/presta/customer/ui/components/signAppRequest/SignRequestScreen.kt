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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import dev.icerock.moko.resources.compose.fontFamilyResource

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
                            .background(color = MaterialTheme.colorScheme.background)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = MaterialTheme.colorScheme.inverseOnSurface),

                        ) {
                            Column(){
                                Row(modifier = Modifier.fillMaxWidth()){
                                    Box(modifier = Modifier.clip(shape = RoundedCornerShape(bottomEnd = 20.dp))) {
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier
                                                .background(color = MaterialTheme.colorScheme.outline)
                                        ) {
                                            Column(
                                                verticalArrangement = Arrangement.Center,
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                modifier = Modifier.padding(15.dp)
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
                                    }

                                    Column(modifier = Modifier.padding(start = 10.dp)) {

                                        Row(modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End){
                                            Text(
                                                "APPLICANT SIGNED",
                                                fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                                fontSize = 13.sp,
                                                modifier = Modifier.padding(end = 10.dp)

                                                )
                                        }
                                        Row(modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically){

                                            Column(){
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

                                            Text(
                                                text = "ksh. 12000.00",
                                                fontFamily = fontFamilyResource(MR.fonts.Poppins.light),
                                                fontSize = 12.sp,
                                                modifier = Modifier.padding(end = 10.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.padding(bottom = 100.dp))
                }
            }
        })

}














