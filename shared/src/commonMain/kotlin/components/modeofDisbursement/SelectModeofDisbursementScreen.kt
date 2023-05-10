package components.modeofDisbursement

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import composables.navigateBackTopBar
import composables.productSelectionCard2
import theme.containerColor

@Composable
fun selectModeOfDisbursement(){

    Surface(
        modifier = Modifier
            .background(color = containerColor),
        color = Color.White
    ) {


        Column(modifier = Modifier.background(color = containerColor)){

            Row(modifier = Modifier.fillMaxWidth()){


                navigateBackTopBar("Disbursement Method")

            }

            Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                .background(color = containerColor)
                .fillMaxHeight()){


                Text(modifier = Modifier.padding(start = 16.dp),
                    text = "Select Disbursement Method")

                Spacer(modifier = Modifier.padding(top = 25.dp))

                productSelectionCard2("Mpesa", onClickContainer = {
                    //Business  Logic

                })

                productSelectionCard2("Bank", onClickContainer = {
                    //Business  Logic

                })

            }

        }


    }

}
