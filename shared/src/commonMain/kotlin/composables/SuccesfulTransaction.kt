package composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import theme.containerColor
@Composable
fun  successfulTransaction(){


    Surface(
        modifier = Modifier
            .background(color = containerColor),
        color = Color.White
    ) {

        Column(modifier = Modifier.fillMaxWidth().fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){

            Column(
                verticalArrangement = Arrangement.Center,

                modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally){



                Text("Transaction Successful!",
                fontSize = 28.sp)

                Row(modifier = Modifier.padding(start = 50.dp, end = 50.dp, top = 5.dp)){

                    Text(text = "Loan topup of Kes 30,000 transferred to Mpesa No. 0724123456",
                    fontSize = 14.sp)

                }

            }


           Row(modifier = Modifier.padding(start = 25.dp, end = 25.dp)){

               ActionButton("Done", onClickContainer = {

               })
           }


        }

    }


}