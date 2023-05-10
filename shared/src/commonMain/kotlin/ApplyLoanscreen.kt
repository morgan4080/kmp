package pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import composables.*
import org.jetbrains.compose.resources.ExperimentalResourceApi
import theme.*


@Composable
fun  applyLoanScreen(){

    //apply loans Screen

    Surface(
        modifier = Modifier
            .background(color = containerColor),
        color = Color.White
    ) {


        Column(modifier = Modifier.background(color = containerColor)){

            Row(modifier = Modifier.fillMaxWidth()){


                navigateBackTopBar("Apply Loan")

            }

            Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                .background(color = containerColor)
                .fillMaxHeight()){


                Text(modifier = Modifier.padding(start = 16.dp),
                    text = "Select Loan Type",
                color = labelTextColor)

                productSelectionCard2("Short Term Loan", onClickContainer = {



                })
                productSelectionCard2("Long Term Loan", onClickContainer = {


                })


            }



        }



    }
}







//Emergency Loan Confirmation




//Select The mode of  disbursement //Mpesa or Bank



//Bank Disbursement



//bank pop up


//disbursement Details card

//emergency loan  Description Card--shows The loan limit




















