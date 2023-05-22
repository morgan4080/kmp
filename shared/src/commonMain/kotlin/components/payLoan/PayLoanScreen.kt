package components.payLoan

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import composables.ActionButton
import composables.LoanStatusContainer
import composables.NavigateBackTopBar
import composables.Paginator
import composables.TextInputContainer
import composables.disbursementDetailsRow
import theme.actionButtonColor
import theme.labelTextColor

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PayLoanScreen(component: PayLoanComponent) {

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
                    modifier = Modifier.padding(start = 16.dp),
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
                                    Text(
                                        text = "Loan Details",
                                        modifier = Modifier
                                            .padding(
                                                top = 14.dp)
                                    )

                                    Text(
                                        text = "Loan Balance",
                                        fontSize = 10.sp,
                                        modifier = Modifier
                                            .padding(top = 3.dp)
                                    )
                                    Row(modifier = Modifier
                                        .fillMaxWidth(),){

                                        Row(verticalAlignment = Alignment.CenterVertically){
                                            Text(
                                                text = "Kes 30,000",
                                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                                                fontWeight = FontWeight.Black,
                                            )
                                            Text(text = "(PerForming)")

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
                                        Text(text = "Loan schedule")

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
                                        .fillMaxWidth()
                                        .padding(
                                            top = 10.dp,
                                            bottom = 10.dp,
                                            start = 16.dp,
                                            end = 16.dp
                                        ),
                                    horizontalArrangement = Arrangement.Center
                                ) {

                                    ElevatedCard(onClick = {
                                        launchPopUp = false
                                    }, modifier = Modifier) {

                                        Text(
                                            text = "Dismiss",
                                            fontSize = 11.sp,
                                            modifier = Modifier
                                                .padding(
                                                top = 5.dp,
                                                bottom = 5.dp,
                                                start = 20.dp,
                                                end = 20.dp
                                            )
                                        )

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
            Text(text = "Schedule date",
            fontSize = MaterialTheme.typography.labelSmall.fontSize)
            Text(text = "12 may 2020 12:20pm",
             fontSize = MaterialTheme.typography.labelSmall.fontSize)

        }

        Column(){
            Text(text = "kes 8000.00",
                fontSize = MaterialTheme.typography.labelSmall.fontSize)
            Text(text = "Paid",
                fontSize = MaterialTheme.typography.labelSmall.fontSize)

        }

    }

}



























