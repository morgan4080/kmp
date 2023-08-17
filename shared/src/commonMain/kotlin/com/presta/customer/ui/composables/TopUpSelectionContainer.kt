package com.presta.customer.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.ui.theme.labelTextColor
import dev.icerock.moko.resources.compose.fontFamilyResource

@Composable
fun TopUpSelectionContainer(
//    checked:Boolean,
//    label:String,
    min:Double,
    max:Double,
//    Balance:Double,
//    Date:String
) {

    var checkedState by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = checkedState,
                onCheckedChange = { checkedState = it },
                modifier = Modifier.clip(shape = CircleShape)
                    .background(color = MaterialTheme.colorScheme.inverseOnSurface)
                    .height(20.dp)
                    .width(20.dp),
                colors = CheckboxDefaults.colors(uncheckedColor = MaterialTheme.colorScheme.inverseOnSurface)
            )

            Column(modifier = Modifier
                .padding(start = 10.dp)) {

                Text(text = "Emergency Loan",
                fontSize = 12.sp,
                    fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                    color = labelTextColor
                )
                    Text(
                        text = "min and max",
                        fontSize = 10.sp,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                    )
            }
        }

        Column() {

            Text(text = " Bal.Kes 5000.00",
                fontSize = 12.sp,
                fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold),
                color = labelTextColor
            )
            Text(
                text = "Due- 12 May 2020",
                fontSize = 10.sp,
                fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
            )

        }

    }

}