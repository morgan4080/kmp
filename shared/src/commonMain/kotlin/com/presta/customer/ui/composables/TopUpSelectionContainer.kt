<<<<<<< HEAD:shared/src/commonMain/kotlin/composables/TopUpSelectionContainer.kt
package composables
=======
package com.presta.customer.ui.composables
>>>>>>> 93b0a72d0fe80ddb36cd116f62ec95712118a423:shared/src/commonMain/kotlin/com/presta/customer/ui/composables/TopUpSelectionContainer.kt

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
<<<<<<< HEAD:shared/src/commonMain/kotlin/composables/TopUpSelectionContainer.kt
import dev.icerock.moko.resources.compose.fontFamilyResource
import theme.labelTextColor

@Composable
fun TopUpSelectionContainer() {
=======
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
>>>>>>> 93b0a72d0fe80ddb36cd116f62ec95712118a423:shared/src/commonMain/kotlin/com/presta/customer/ui/composables/TopUpSelectionContainer.kt

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
<<<<<<< HEAD:shared/src/commonMain/kotlin/composables/TopUpSelectionContainer.kt
                    .background(color = MaterialTheme.colorScheme.secondaryContainer)
                    .height(20.dp)
                    .width(20.dp),
                colors = CheckboxDefaults.colors(uncheckedColor = MaterialTheme.colorScheme.secondaryContainer)
=======
                    .background(color = MaterialTheme.colorScheme.inverseOnSurface)
                    .height(20.dp)
                    .width(20.dp),
                colors = CheckboxDefaults.colors(uncheckedColor = MaterialTheme.colorScheme.inverseOnSurface)
>>>>>>> 93b0a72d0fe80ddb36cd116f62ec95712118a423:shared/src/commonMain/kotlin/com/presta/customer/ui/composables/TopUpSelectionContainer.kt
            )

            Column(modifier = Modifier
                .padding(start = 10.dp)) {
<<<<<<< HEAD:shared/src/commonMain/kotlin/composables/TopUpSelectionContainer.kt
=======

>>>>>>> 93b0a72d0fe80ddb36cd116f62ec95712118a423:shared/src/commonMain/kotlin/com/presta/customer/ui/composables/TopUpSelectionContainer.kt
                Text(text = "Emergency Loan",
                fontSize = 12.sp,
                    fontFamily = fontFamilyResource(MR.fonts.Poppins.regular),
                    color = labelTextColor
                )
<<<<<<< HEAD:shared/src/commonMain/kotlin/composables/TopUpSelectionContainer.kt
                Text(
                    text = "min 10,000-max 50,000",
                    fontSize = 10.sp,
                    fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                )

=======
                    Text(
                        text = "min and max",
                        fontSize = 10.sp,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                    )
>>>>>>> 93b0a72d0fe80ddb36cd116f62ec95712118a423:shared/src/commonMain/kotlin/com/presta/customer/ui/composables/TopUpSelectionContainer.kt
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
                fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
            )

        }

    }

}