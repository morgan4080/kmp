<<<<<<< HEAD:shared/src/commonMain/kotlin/composables/ProductSelectionCard.kt
package composables

import androidx.compose.foundation.BorderStroke
=======
>>>>>>> 93b0a72d0fe80ddb36cd116f62ec95712118a423:shared/src/commonMain/kotlin/com/presta/customer/ui/composables/ProductSelectionCard.kt
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
<<<<<<< HEAD:shared/src/commonMain/kotlin/composables/ProductSelectionCard.kt
import dev.icerock.moko.resources.compose.fontFamilyResource
import theme.backArrowColor
import theme.labelTextColor
=======
import com.presta.customer.ui.theme.backArrowColor
import dev.icerock.moko.resources.compose.fontFamilyResource
>>>>>>> 93b0a72d0fe80ddb36cd116f62ec95712118a423:shared/src/commonMain/kotlin/com/presta/customer/ui/composables/ProductSelectionCard.kt

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun  ProductSelectionCard(label: String, description: String?=null, onClickContainer: () -> Unit){
    //Product selection card  with bottom text
    ElevatedCard(
        onClick = onClickContainer
        ,
        modifier = Modifier.fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
<<<<<<< HEAD:shared/src/commonMain/kotlin/composables/ProductSelectionCard.kt
            .border(BorderStroke(1.dp, Color.White), shape = RoundedCornerShape(size = 12.dp))
    ) {
        Box(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) {
=======
    ) {
        Box(modifier = Modifier.background(color = MaterialTheme.colorScheme.inverseOnSurface)) {
>>>>>>> 93b0a72d0fe80ddb36cd116f62ec95712118a423:shared/src/commonMain/kotlin/com/presta/customer/ui/composables/ProductSelectionCard.kt
            Row(
                modifier = Modifier.padding(top = 9.dp, bottom = 9.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically){
                Column(){
                    Text(
                        text = label,
                        modifier = Modifier.padding(start = 15.dp),
<<<<<<< HEAD:shared/src/commonMain/kotlin/composables/ProductSelectionCard.kt
                        fontSize = 12.sp,
                        color = labelTextColor,
=======
                        fontSize = 14.sp,
                        color= MaterialTheme.colorScheme.onBackground,
>>>>>>> 93b0a72d0fe80ddb36cd116f62ec95712118a423:shared/src/commonMain/kotlin/com/presta/customer/ui/composables/ProductSelectionCard.kt
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                    )
                    //Spacer(modifier = Modifier.weight(1f))
                    if (description != null) {
                        Text(
                            text = description,
                            modifier = Modifier.padding(start = 15.dp),
                            fontSize = 10.sp,
<<<<<<< HEAD:shared/src/commonMain/kotlin/composables/ProductSelectionCard.kt
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
=======
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
>>>>>>> 93b0a72d0fe80ddb36cd116f62ec95712118a423:shared/src/commonMain/kotlin/com/presta/customer/ui/composables/ProductSelectionCard.kt
                        )
                    }

                }
                Row(){
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        Icons.Filled.KeyboardArrowRight,
                        contentDescription = "Forward Arrow",
                        modifier = Modifier.clip(shape = CircleShape)
<<<<<<< HEAD:shared/src/commonMain/kotlin/composables/ProductSelectionCard.kt
                            .background(color = MaterialTheme.colorScheme.inverseOnSurface),
=======
                            .background(color = Color(0xFFE5F1F5)),
>>>>>>> 93b0a72d0fe80ddb36cd116f62ec95712118a423:shared/src/commonMain/kotlin/com/presta/customer/ui/composables/ProductSelectionCard.kt
                        tint = backArrowColor
                    )
                    Spacer(modifier = Modifier.padding(end = 15.dp))
                }
<<<<<<< HEAD:shared/src/commonMain/kotlin/composables/ProductSelectionCard.kt

=======
>>>>>>> 93b0a72d0fe80ddb36cd116f62ec95712118a423:shared/src/commonMain/kotlin/com/presta/customer/ui/composables/ProductSelectionCard.kt
            }
        }
    }
}