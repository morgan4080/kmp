package composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import theme.backArrowColor
import theme.labelTextColor

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun  ProductSelectionCard(label: String, description: String?=null, onClickContainer: () -> Unit){
    //Product selection card  with bottom text
    ElevatedCard(
        onClick = onClickContainer
        ,
        modifier = Modifier.fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Box(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) {
            Row(
                modifier = Modifier.padding(top = 9.dp, bottom = 9.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically){
                Column(){
                    Text(
                        text = label,
                        modifier = Modifier.padding(start = 15.dp),
                        fontSize = 15.sp,
                        color = labelTextColor)
                    //Spacer(modifier = Modifier.weight(1f))
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