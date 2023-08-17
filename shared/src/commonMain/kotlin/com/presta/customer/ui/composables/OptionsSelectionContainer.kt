package com.presta.customer.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.network.payments.data.PaymentTypes
import com.presta.customer.ui.theme.actionButtonColor
import com.presta.customer.ui.theme.activeCard
import dev.icerock.moko.resources.compose.fontFamilyResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionsSelectionContainer(
    mode: PaymentTypes? = null,
    label: String,
    description: String? = null,
    onClickContainer: (mode: PaymentTypes?) -> Unit
) {
    var checkedState by remember { mutableStateOf(false) }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val color = if (isPressed) activeCard else MaterialTheme.colorScheme.background

    ElevatedCard(
        onClick = {
            onClickContainer(mode)
            checkedState = !checkedState
        },

        modifier = Modifier.fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.inverseOnSurface)

    ) {
        Box(modifier = Modifier
            .background(color = color)) {
            Row(
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {

                    Text(
                        text = label,
                        modifier = Modifier.padding(start = 15.dp),
                        fontSize = 12.sp,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                    )

                    if (description != null) {
                        Text(

                            text = description,
                            modifier = Modifier.padding(start = 15.dp),
                            fontSize = 12.sp,
                        )
                    }
                }


                Row {
                    //Created a Custom checkBox
                    Spacer(modifier = Modifier.weight(1f))

                    Card(
                        modifier = Modifier
                            .background(color = MaterialTheme.colorScheme.background)
                            .clip(shape = CircleShape),
                        elevation = CardDefaults.cardElevation(0.dp),
                        border = BorderStroke(1.dp, color = if (checkedState) actionButtonColor else Color.Gray)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(19.dp)
                                .background(if (checkedState) actionButtonColor else MaterialTheme.colorScheme.background)
                                .clickable {
                                    onClickContainer(mode)
                                    checkedState = !checkedState
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (checkedState)
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = "Check Box",
                                    tint = Color.White,
                                    modifier = Modifier.padding(1.dp)
                                )
                        }
                    }

                    Spacer(modifier = Modifier.padding(end = 15.dp))

                }
            }
        }
    }
}
