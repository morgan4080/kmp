package com.presta.customer.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import dev.icerock.moko.resources.compose.fontFamilyResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignProductSelection(
    label1: String,
    label2: String,
    icon:ImageVector,
    onClickContainer: () -> Unit,
    backgroundColor: Color,
    textColor:Color,
    iconTint:Color
) {
    ElevatedCard(
        onClick = onClickContainer,
        modifier = Modifier
            .clip(RoundedCornerShape(size = 12.dp))
    ) {
        Box(
            modifier = Modifier
                .background(backgroundColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        icon,
                        contentDescription = "Sign",
                        modifier = Modifier.size(40.dp),
                        tint = iconTint
                    )

                    Column(modifier = Modifier.padding(start = 20.dp)) {
                        Text(
                                label1,
                                fontSize = 14.sp,
                                color = textColor,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold)
                            )
                        Text(
                                label2,
                                fontSize = 11.sp,
                                color =textColor,
                                fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
                            )
                    }
                }
            }
        }
    }
}