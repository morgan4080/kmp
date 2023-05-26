package com.presta.customer.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.ui.theme.backArrowColor
import com.presta.customer.ui.theme.labelTextColor
import dev.icerock.moko.resources.compose.fontFamilyResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductSelectionCard2(label: String, description: String?=null, onClickContainer: () -> Unit){
    //Has  single central text-use cases- Select Bank.etc -has  more  padding than  The product Selection card
    ElevatedCard(
        onClick = onClickContainer,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) {
            Row(
                modifier = Modifier.padding(top = 19.dp, bottom = 19.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically){

                Column(){

                    Text(
                        text = label,
                        modifier = Modifier.padding(start = 15.dp),
                        fontSize = 12.sp,
                        color = labelTextColor,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                    )
                    if (description != null) {
                        Text(

                            text = description,
                            modifier = Modifier.padding(start = 15.dp),
                            fontSize = 12.sp,)
                    }
                }
                Row(){

                    Spacer(modifier = Modifier.weight(1f))
                    Icon(

                        Icons.Filled.KeyboardArrowRight,
                        contentDescription = "Forward Arrow",
                        modifier = Modifier.clip(shape = CircleShape)
                            .background(color = MaterialTheme.colorScheme.secondaryContainer),
                        tint = backArrowColor

                    )
                    Spacer(modifier = Modifier.padding(end = 15.dp))

                }
            }
        }
    }
}

