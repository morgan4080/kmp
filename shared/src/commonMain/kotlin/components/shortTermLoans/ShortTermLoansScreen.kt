package components.shortTermLoans

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import composables.NavigateBackTopBar
import composables.ProductSelectionCard
import theme.containerColor
import theme.labelTextColor

@Composable
fun ShortTermLoansScreen(){
    //short term loans screen


    Surface(
        modifier = Modifier
            .background(color = containerColor),
        color = Color.White
    ) {


        Column(modifier = Modifier.background(color = containerColor)
            .fillMaxWidth()
            .fillMaxHeight()){

            Row(modifier = Modifier.fillMaxWidth()){


                NavigateBackTopBar("Short Term Loan")

            }

            Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                .background(color = containerColor)
                .fillMaxHeight()){

                Text(modifier = Modifier.padding(start = 16.dp),
                    text = "Select Your Loan Product",
                    color = labelTextColor
                )
                Column(modifier = Modifier.verticalScroll(rememberScrollState())
                    .fillMaxWidth()
                    .fillMaxHeight()){


                    ProductSelectionCard("Emergency Loan ","Interest 12%", onClickContainer = {




                    })
                    ProductSelectionCard("Mkopo Halisi Loan ","Interest 12%", onClickContainer = {



                    })
                    ProductSelectionCard("Shule Loan ","Interest 12%", onClickContainer = {

                    })
                    ProductSelectionCard("Mfanisi Loan ","Interest 12%", onClickContainer = {

                    })
                    ProductSelectionCard("Normal Loan ","Interest 12%", onClickContainer = {

                    })
                    ProductSelectionCard("Anzia Loan ","Interest 12%", onClickContainer = {

                    })
                    ProductSelectionCard("Soko Loan ","Interest 12%", onClickContainer = {

                    })
                    ProductSelectionCard("Tipper Loan ","Interest 12%", onClickContainer = {

                    })

                    Spacer(modifier = Modifier.padding(50.dp))

                }

            }

        }

    }

}