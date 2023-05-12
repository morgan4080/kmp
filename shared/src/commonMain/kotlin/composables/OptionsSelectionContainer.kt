package composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
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
import theme.bankContainerColor
import theme.labelTextColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionsSelectionContainer (label: String, description: String?=null, onClickContainer: () -> Unit){

    var checkedState by remember { mutableStateOf(false) }


    ElevatedCard(
        onClick = {
            checkedState = !checkedState
            onClickContainer()
        },

        modifier = Modifier.fillMaxWidth()
            .background(color = Color.White)

    ) {
        Box(modifier = Modifier.background(color = bankContainerColor)) {
            Row(
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically){

                Column(){

                    Text(
                        text = label,
                        modifier = Modifier.padding(start = 15.dp),
                        fontSize = 12.sp,
                        color = labelTextColor
                    )
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


                    Checkbox(
                        checked =checkedState,
                        onCheckedChange = { checkedState = it},
                        modifier = Modifier.clip(shape = CircleShape)
                            .background(color = MaterialTheme.colorScheme.secondaryContainer)
                            .height(20.dp)
                            .width(20.dp),
                        colors = CheckboxDefaults.colors(uncheckedColor =  MaterialTheme.colorScheme.secondaryContainer)
                    )

                    Spacer(modifier = Modifier.padding(end = 15.dp))


                }


            }


        }
    }

}
