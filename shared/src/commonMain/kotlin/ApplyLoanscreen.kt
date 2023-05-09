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


@Composable
fun ShortTermLoans(){
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


                navigateBackTopBar("Short Term Loan")

            }

            Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                .background(color = containerColor)
                .fillMaxHeight()){

                Text(modifier = Modifier.padding(start = 16.dp),
                    text = "Select Your Loan Product",
                color = labelTextColor)
                Column(modifier = Modifier.verticalScroll(rememberScrollState())
                    .fillMaxWidth()
                    .fillMaxHeight()){


                    productSelectionCard("Emergency Loan ","Interest 12%", onClickContainer = {




                    })
                    productSelectionCard("Mkopo Halisi Loan ","Interest 12%", onClickContainer = {



                    })
                    productSelectionCard("Shule Loan ","Interest 12%", onClickContainer = {

                    })
                    productSelectionCard("Mfanisi Loan ","Interest 12%", onClickContainer = {

                    })
                    productSelectionCard("Normal Loan ","Interest 12%", onClickContainer = {

                    })
                    productSelectionCard("Anzia Loan ","Interest 12%", onClickContainer = {

                    })
                    productSelectionCard("Soko Loan ","Interest 12%", onClickContainer = {

                    })
                    productSelectionCard("Tipper Loan ","Interest 12%", onClickContainer = {

                    })

                    Spacer(modifier = Modifier.padding(50.dp))

                }


            }



        }



    }



}


@Composable
fun emergencyLoans(){
    //emergency  loans screen
    //Gets The input value  as the user input
    val inputValue=""

    Surface(
        modifier = Modifier
            .background(color = containerColor),
        color = Color.White
    ) {


        Column(modifier = Modifier.background(color = containerColor)){

            Row(modifier = Modifier.fillMaxWidth()){


                navigateBackTopBar("Emergency Loan")

            }

            Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                .background(color = containerColor)
                .fillMaxHeight()){


                Text(modifier = Modifier.padding(start = 16.dp),
                    text = "Enter Loan  Amount")

                //container Card

               loanLimitContainer()

                //Enter the desired loan amount

                Row(modifier = Modifier.padding(top = 16.dp)){


                    textInputContainer("Enter the desired amount","")

                }


                Row(modifier = Modifier.padding(top = 16.dp)){

                    textInputContainer("Desired Period(Months)",inputValue)

                }

                //action Button
                Row(modifier = Modifier.padding(top = 30.dp)){
                    ActionButton("Confirm", onClickContainer = {


                    })

                }


            }



        }



    }

}


//Emergency Loan Confirmation


@Composable
fun emergencyLoansConfirmation(){
    //emergency  loans screen
    //Gets The input value  as the user input
    val inputValue=""

    Surface(
        modifier = Modifier
            .background(color = containerColor),
        color = Color.White
    ) {


        Column(modifier = Modifier.background(color = containerColor)){

            Row(modifier = Modifier.fillMaxWidth()){


                navigateBackTopBar("Emergency Loan Confirm")

            }

            Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                .background(color = containerColor)
                .fillMaxHeight()){


                Text(modifier = Modifier.padding(start = 16.dp),
                    text = "Confirm Loan  Details")



                //Disbursement Details

                disbursementDetailsContainer()

                //action Button
                Row(modifier = Modifier.padding(top = 30.dp)){
                    ActionButton("Confirm", onClickContainer = {



                    })

                }

            }



        }



    }

}


//Select The mode of  disbursement //Mpesa or Bank

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


//Bank Disbursement

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class)
@Composable
fun bankDisbursement(){

    var popupControl by remember { mutableStateOf(false) }


    Surface(
        modifier = Modifier
            .background(color = containerColor),
        color = containerColor
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


                if (popupControl) {


                        Popup() {
                            // Composable to select The bank


                            ElevatedCard(modifier = Modifier.fillMaxWidth().padding(start = 26.dp, end = 26.dp, top = 26.dp, bottom = 70.dp),
                                colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
                            ){

                                Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)){

                                    Text("Select Bank",
                                        modifier = Modifier.padding(start = 16.dp))
                                    Text("Select Options Below",
                                        modifier = Modifier.padding(start = 16.dp),
                                        fontSize = 10.sp)

                                    Column(modifier = Modifier.height(300.dp)){

                                        Column(modifier = Modifier.verticalScroll(rememberScrollState())
                                            .wrapContentHeight()) {


                                            Row(
                                                modifier = Modifier.fillMaxWidth()
                                                    .background(color = Color.White)
                                                    .padding(top = 10.dp, start = 16.dp, end = 16.dp)
                                            ) {
                                                bankDetailsContainer("KCB", onClickContainer = {


                                                })

                                            }

                                            Row(
                                                modifier = Modifier.fillMaxWidth()
                                                    .background(color = Color.White)
                                                    .padding(top = 7.dp, start = 16.dp, end = 16.dp)
                                            ) {
                                                bankDetailsContainer("Cooperative Bank", onClickContainer = {



                                                }
                                                )

                                            }

                                            Row(
                                                modifier = Modifier.fillMaxWidth()
                                                    .background(color = Color.White)
                                                    .padding(top = 7.dp, start = 16.dp, end = 16.dp)
                                            ) {
                                                bankDetailsContainer("KCB", onClickContainer = {

                                                })

                                            }

                                            Row(
                                                modifier = Modifier.fillMaxWidth()
                                                    .background(color = Color.White)
                                                    .padding(top = 7.dp, start = 16.dp, end = 16.dp)
                                            ) {
                                                bankDetailsContainer("KCB", onClickContainer = {



                                                })

                                            }

                                            Row(
                                                modifier = Modifier.fillMaxWidth()
                                                    .background(color = Color.White)
                                                    .padding(top = 7.dp, start = 16.dp, end = 16.dp)
                                            ) {
                                                bankDetailsContainer("KCB", onClickContainer = {

                                                })

                                            }
                                            Row(
                                                modifier = Modifier.fillMaxWidth()
                                                    .background(color = Color.White)
                                                    .padding(top = 7.dp, start = 16.dp, end = 16.dp)
                                            ) {
                                                bankDetailsContainer("KCB", onClickContainer = {

                                                })

                                            }
                                            Row(
                                                modifier = Modifier.fillMaxWidth()
                                                    .background(color = Color.White)
                                                    .padding(top = 7.dp, start = 16.dp, end = 16.dp)
                                            ) {
                                                bankDetailsContainer("KCB", onClickContainer = {

                                                })

                                            }
                                            Row(
                                                modifier = Modifier.fillMaxWidth()
                                                    .background(color = Color.White)
                                                    .padding(top = 7.dp, start = 16.dp, end = 16.dp)
                                            ) {
                                                bankDetailsContainer("KCB", onClickContainer = {

                                                })

                                            }

                                        }

                                    }


                                }

                                //Row

                                Row(modifier = Modifier.fillMaxWidth().padding(top = 10.dp, bottom = 10.dp, start = 16.dp, end = 16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween){

                                    ElevatedCard(onClick = {
                                        popupControl=false
                                    }, modifier = Modifier.padding(start = 16.dp)){

                                        Text(
                                            text = "Dismiss",
                                            fontSize = 11.sp,
                                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp, start = 20.dp, end = 20.dp))

                                    }


                                    ElevatedCard(onClick = {
                                        popupControl=false
                                    }, modifier = Modifier.padding(end = 16.dp),
                                    colors = CardDefaults.elevatedCardColors(containerColor = actionButtonColor)){

                                        Text(text = "Proceed",
                                            color = Color.White,
                                            fontSize = 11.sp,
                                            modifier = Modifier.padding(top = 5.dp, bottom = 5.dp, start = 20.dp, end = 20.dp)
                                        )

                                    }


                                }


                            }



                        }



                }



                productSelectionCard2("Select Bank", onClickContainer = {

                    //Business  Logic
                    //banK details pop up card
                    popupControl=true


                })


                productSelectionCard2("Account Number", onClickContainer = {
                    //Business  Logic


                })



                Spacer(modifier = Modifier.padding(top = 60.dp))


                ActionButton("Proceed", onClickContainer = {


                })



            }



        }



    }


}

//bank pop up
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun bankDetailsContainer (label: String,description: String?=null,onClickContainer: () -> Unit){

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


//disbursement Details card

@Composable
fun disbursementDetailsContainer(){

    ElevatedCard(modifier = Modifier.padding(top = 10.dp)) {
        Box (modifier = Modifier.background(color = Color.White)
        ) {
            Column (
                modifier = Modifier.padding(
                    top = 23.dp,
                    start = 24.dp,
                    end = 24.dp,
                    bottom = 24.dp,
                )
            ) {
                Row (modifier = Modifier
                    .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "Disbursement Amount ",
                        color= Color(0xFF8F8F8F.toInt()), // #002C56
                        fontSize = 16.sp
                    )


                }
                Row (modifier = Modifier
                    .padding(top = 0.dp)
                    .fillMaxWidth()
                ) {
                    Text(
                        text = "Kes 30,000",
                        color= MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                //data Rows
                Spacer(modifier = Modifier.height(19.dp))
                disbursementDetailsRow("Requested Amount","Kes 30,000")
                disbursementDetailsRow("Requested Amount","Kes 30,000")
                disbursementDetailsRow("Requested Amount","Kes 30,000")
                disbursementDetailsRow("Requested Amount","Kes 30,000")
                disbursementDetailsRow("Requested Amount","Kes 30,000")
                disbursementDetailsRow("Requested Amount","Kes 30,000")
                disbursementDetailsRow("Requested Amount","Kes 30,000")


            }
        }

    }

}

@Composable
fun disbursementDetailsRow(label: String,data: String){

    Row(modifier = Modifier.fillMaxWidth()
        .padding(top = 10.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween){

        Text(
            text = label,
            fontSize = 10.sp)

        Text(
            text = data,
            fontSize = 12.sp,
            color= MaterialTheme.colorScheme.onPrimaryContainer,
            fontWeight = FontWeight.Black)

    }


}


//emergency loan  Description Card--shows The loan limit
@Composable
fun loanLimitContainer(){

    ElevatedCard(modifier = Modifier.padding(top = 10.dp)) {
        Box (modifier = Modifier.background(color = Color.White)
        ) {
            Column (
                modifier = Modifier.padding(
                    top = 23.dp,
                    start = 24.dp,
                    end = 19.5.dp,
                    bottom = 33.dp,
                )
            ) {
                Row (modifier = Modifier
                    .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "Loan  Limit ",
                        color= Color(0xFF8F8F8F.toInt()), // #002C56
                        fontSize = 16.sp
                    )

                }
                Row (modifier = Modifier
                    .padding(top = 0.dp)
                    .fillMaxWidth()
                ) {
                    Text(
                        text = "Min.Kes 10,000-Max 50,000",
                        color= MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black
                    )
                }



                Row (modifier = Modifier
                    .padding(top = 11.dp)
                    .fillMaxWidth()
                ) {



                    Column(){

                        Text(
                            text = "Interest",
                            color= Color(0xFF8F8F8F.toInt()), // #002C56


                        )

                        Text(
                            text = "12%",
                            color= MaterialTheme.colorScheme.onPrimaryContainer, // #002C56
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                        )



                    }

                    Spacer(modifier = Modifier.padding(start = 42.dp))



                    Column(){

                        Text(
                            text = "Max loan  Period",
                            color= Color(0xFF8F8F8F.toInt()), // #002C56


                        )

                        Text(
                            text = "12 Months",
                            color= MaterialTheme.colorScheme.onPrimaryContainer, // #002C56
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                        )


                    }


                }


            }
        }

    }

}




















