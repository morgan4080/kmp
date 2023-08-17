package com.presta.customer.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.ui.theme.backArrowColor
import dev.icerock.moko.resources.compose.fontFamilyResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigateBackTopBar(label: String,onClickContainer: () -> Unit) {
    CenterAlignedTopAppBar(
        modifier =Modifier
            .background(color =  MaterialTheme.colorScheme.background)
            .padding(start = 9.dp),
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background),
        title = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically){
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Forward Arrow",
                            tint = backArrowColor,
                            modifier = Modifier.clickable {
                                onClickContainer()
                            }
                        )

                        Text(
                            text = label,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp),
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 18.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                        )

                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        },
    )

}


