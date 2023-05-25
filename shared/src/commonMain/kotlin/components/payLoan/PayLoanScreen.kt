package components.payLoan

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.presta.customer.MR
import composables.ActionButton
import composables.LoanStatusContainer
import composables.NavigateBackTopBar
import composables.Paginator
import composables.TextInputContainer
import composables.disbursementDetailsRow
import dev.icerock.moko.resources.compose.fontFamilyResource
import theme.backArrowColor
import theme.labelTextColor

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PayLoanScreen(component: PayLoanComponent){

   // val state = rememberLazyListState()
    val stateLazyRow0 = rememberLazyListState()
   // val stateLazyRow = rememberLazyListState()
    var launchPopUp by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background),
        color = Color.White
    ) {
        Column(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) {

            Row(modifier = Modifier
                .fillMaxWidth()) {

                NavigateBackTopBar("Pay Loan", onClickContainer = {

                })

            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(start = 16.dp, end = 16.dp)
                    .background(color = MaterialTheme.colorScheme.background)
            ) {

                Text(
                    text = "My loans",
                    color = labelTextColor
                )

                LazyRow(modifier = Modifier
                    .padding(vertical = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    state = stateLazyRow0,
                    flingBehavior = rememberSnapFlingBehavior(lazyListState = stateLazyRow0),
                    content = {
                        items(3) {
                            Box(
                                modifier = Modifier
                                    .fillParentMaxWidth()

                            ) {
                                LoanStatusContainer()
                            }
                        }
                    }
                )

                //dotted indicator
                Row(modifier = Modifier
                    .fillMaxWidth()) {
                    Paginator(3, stateLazyRow0.firstVisibleItemIndex)

                }

                Row(modifier = Modifier.fillMaxWidth()
                    .padding(top = 30.dp)) {

                    TextInputContainer("Desired ammount", "")

                }

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 35.dp)) {
                    ActionButton("Pay Now", onClickContainer = {

                    })

                }

                Card(
                    onClick = {

                        //Navigate to view More Details
                              //launch view More details PoP up
                              launchPopUp=true

                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.5.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.background)
                ) {

                    Text(
                        "View More Details",
                        fontSize = 12.sp,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                }

                //View More details popup
                //Added overlay  to the po up screen
                if (launchPopUp){

                    Popup(){

                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .background(color = Color.Black.copy(alpha = 0.7f)),
                            verticalArrangement = Arrangement.Top) {

                            ElevatedCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.8f)
                                    .padding(
                                        start = 25.dp,
                                        end = 25.dp,
                                        top = 26.dp),
                                colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
                            ) {

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            start = 16.dp,
                                            end = 16.dp)
                                ) {

                                    Row(modifier = Modifier
                                        .padding(top = 14.dp)
                                        .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween){
                                        Text(text = "Loan Details",
                                            fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                                            fontSize = 14.sp
                                        )
                                        Icon(
                                            Icons.Filled.Cancel,
                                            contentDescription = "Cancel  Arrow",
                                            tint = backArrowColor,
                                            modifier = Modifier.clickable {
                                                launchPopUp=false

                                            }
                                        )
                                    }

                                    Text(
                                        text = "Loan Balance",
                                        modifier = Modifier
                                            .padding(top = 7.dp),
                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                                    )

                                    Row(modifier = Modifier
                                        .fillMaxWidth(),){

                                        Row(verticalAlignment = Alignment.CenterVertically){
                                            Text(
                                                text = "Kes 30,000",
                                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                                                fontFamily = fontFamilyResource(MR.fonts.Poppins.bold),
                                            )
                                            Text(text = "(PerForming)",
                                            fontSize = 10.sp,
                                            fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold)
                                            )

                                        }

                                    }

                                    //data Rows

                                    LazyColumn(modifier =Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(0.4f)){
                                        items(8){
                                            disbursementDetailsRow("Requested Amount", "Kes 30,000")

                                        }

                                    }

                                    //Loan schedule

                                    Row(modifier = Modifier
                                        .fillMaxWidth()){
                                        Text(text = "Loan schedule",
                                        fontSize = 12.sp,
                                        fontFamily = fontFamilyResource(MR.fonts.Poppins.regular),
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )

                                    }

                                    LazyColumn(modifier =Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(0.7f)){
                                        items(5){
                                            Row(modifier = Modifier
                                                .padding(top = 7.dp)
                                                .fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically){
                                                LoanScheduleContainer()
                                            }

                                        }

                                    }

                                }
                                Row(
                                    modifier = Modifier
                                        .padding(top = 21.dp)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {

                                    OutlinedButton(modifier = Modifier.height(30.dp),
                                        onClick = {
                                            launchPopUp=false

                                    }){
                                        Text(text = "Dismiss",
                                        fontSize = 9.sp,
                                        color = labelTextColor)

                                    }

                                }

                            }

                        }

                    }

                }


            }

        }

    }

}


@Composable
fun LoanScheduleContainer(){
    Row(modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically){
        Column(){
            Text(text = "12 May 2023",
            fontSize = MaterialTheme.typography.labelSmall.fontSize,
            fontFamily = fontFamilyResource(MR.fonts.Poppins.regular),
                color = labelTextColor
            )
            Text(text = "12:20pm",
             fontSize = MaterialTheme.typography.labelSmall.fontSize,
                fontFamily = fontFamilyResource(MR.fonts.Poppins.regular))

        }

        Column(){
            Text(text = "kes 8000.00",
                fontSize = MaterialTheme.typography.labelMedium.fontSize,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold))

            Text(text = "Paid",
                fontSize = MaterialTheme.typography.labelSmall.fontSize,
                fontFamily = fontFamilyResource(MR.fonts.Poppins.regular)
            )

        }

    }

}



























