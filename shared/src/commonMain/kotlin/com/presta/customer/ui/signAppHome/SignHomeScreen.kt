package com.presta.customer.ui.signAppHome

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.Grade
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.presta.customer.ui.composables.SignProductSelection
import com.presta.customer.ui.helpers.LocalSafeArea

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignHomeScreen(component: SignHomeComponent) {
    val progressPercentage = 0.75f // Example progress percentage
    var progress by remember { mutableStateOf(0.5f) }
    val progress2 = remember { mutableStateOf(0.75f) }
    Scaffold(
        modifier = Modifier.padding(LocalSafeArea.current),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(start = 9.dp),
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                title = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    modifier = Modifier,
                                    text = "Presta capital",
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 18.sp
                                )
                            }
                            IconButton(
                                modifier = Modifier.absoluteOffset(x = 6.dp).zIndex(1f),
                                onClick = {
                                    //scopeDrawer.launch { drawerState.open() }
                                },
                                content = {
                                    Icon(
                                        imageVector = Icons.Filled.Apps,
                                        modifier = Modifier.size(25.dp),
                                        contentDescription = "Menu pending",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            )
                        }
                    }
                }
            )
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
                            .clip(RoundedCornerShape(size = 12.dp))
                            .padding(innerPadding)

                    ) {
                        Box(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.inverseOnSurface)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                            ) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Text(text = "GOOD AFTERNOON")
                                    Text(text = "MURUNGI  MUTUGI")
                                    Text(text = "55432")
                                    Row(modifier = Modifier.fillMaxWidth()) {
                                        Spacer(modifier = Modifier.weight(1f))
                                        Text(text = "Share Amount ")
                                    }
                                    Row(modifier = Modifier.fillMaxWidth()) {
                                        Spacer(modifier = Modifier.weight(1f))
                                        Text(text = "20,000.00 ")
                                    }
                                    Divider(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 20.dp),
                                        thickness = 2.dp,
                                        color = Color.Gray
                                    )
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Loan Requests")
                                        Text("See All")

                                    }
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        //CircularProgressBar(percentage = progressPercentage)
                                        CircularProgressBarWithText(
                                            progress = progress,
                                            text = "${(progress * 100).toInt()}%",
                                            modifier = Modifier.padding(start = 1.dp)
                                        )
                                        // Text(text = "${(progressPercentage * 100).toInt()}%", fontSize = 24.sp)
                                        Column() {
                                            Text(
                                                "Normal loan",
                                                fontSize = 12.sp
                                            )
                                            Text(
                                                "27/04/2023 08:32",
                                                fontSize = 12.sp
                                            )
                                        }

                                        Spacer(modifier = Modifier.weight(1f))

                                        Text(
                                            text = "12,000.00  KES",
                                            fontSize = 12.sp
                                        )

                                    }
                                }
                            }
                        }
                    }
                }
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    ) {
                        SignProductSelection(
                            icon = Icons.Outlined.Assignment,
                            label1 = "Apply for a loan",
                            label2 = "Create a loan Request",
                            onClickContainer = {

                            },
                        backgroundColor = MaterialTheme.colorScheme.primary,
                        textColor =  MaterialTheme.colorScheme.background,
                            iconTint = MaterialTheme.colorScheme.background
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    ) {
                        SignProductSelection(
                            icon = Icons.Outlined.Assignment,
                            label1 = "Guarantorship requests ",
                            label2 = "View guarantorship requets",
                            onClickContainer = {

                            },
                        backgroundColor =MaterialTheme.colorScheme.inverseOnSurface ,
                        textColor = MaterialTheme.colorScheme.onBackground,
                        iconTint = MaterialTheme.colorScheme.primary)
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    ) {
                        SignProductSelection(
                            icon = Icons.Outlined.Grade,
                            label1 = "Favorite guarantors",
                            label2 = "View  and edit your favourite guarantors",
                            onClickContainer = {

                            },
                        backgroundColor = MaterialTheme.colorScheme.inverseOnSurface,
                            textColor = MaterialTheme.colorScheme.onBackground,
                        iconTint = MaterialTheme.colorScheme.primary)
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    ) {
                        SignProductSelection(
                            icon = Icons.Outlined.Shield,
                            label1 = "Witness request",
                            label2 = "View witness requests",
                            onClickContainer = {

                            },
                        backgroundColor = MaterialTheme.colorScheme.inverseOnSurface,
                            textColor = MaterialTheme.colorScheme.onBackground,
                        iconTint = MaterialTheme.colorScheme.primary)
                    }
                }
                item {
                    Spacer(modifier = Modifier.padding(bottom = 100.dp))
                }
            }
        })

}

@Composable
fun CircularProgressBarWithText(
    progress: Float,
    text: String,
    strokeWidth: Dp = 2.dp,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Box(modifier = modifier.size(80.dp)) {

        CircularProgressIndicator(
            progress = 1f,
            strokeWidth = strokeWidth,
            color = Color.Gray,
            modifier = Modifier.align(Alignment.Center)
        )
        CircularProgressIndicator(
            progress = progress,
            strokeWidth = strokeWidth,
            color = color,
            modifier = Modifier.align(Alignment.Center)
        )

        Text(
            text = text,
            modifier = Modifier.align(Alignment.Center),
            style = TextStyle(fontSize = 14.sp, color = Color.Black)
        )
    }
}

//Button(onClick = { progress += 0.1f }) {
//    Text("Increase Progress")
//}












