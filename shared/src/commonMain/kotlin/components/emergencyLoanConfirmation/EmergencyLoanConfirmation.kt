package components.emergencyLoanConfirmation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import composables.ActionButton
import composables.DisbursementDetailsContainer
import composables.NavigateBackTopBar
import theme.containerColor

@Composable
fun EmergencyLoansConfirmation(){
    //emergency  loans screen
    //Gets The input value  as the user input


    Surface(
        modifier = Modifier
            .background(color = containerColor),
        color = Color.White
    ) {


        Column(modifier = Modifier.background(color = containerColor)){

            Row(modifier = Modifier.fillMaxWidth()){


                NavigateBackTopBar("Emergency Loan Confirm")

            }

            Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                .background(color = containerColor)
                .fillMaxHeight()){


                Text(modifier = Modifier.padding(start = 16.dp),
                    text = "Confirm Loan  Details")



                //Disbursement Details

                DisbursementDetailsContainer()

                //action Button
                Row(modifier = Modifier.padding(top = 30.dp)){
                    ActionButton("Confirm", onClickContainer = {



                    })

                }
            }

        }

    }

}
