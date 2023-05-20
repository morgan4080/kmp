package components.payLoan

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import composables.ActionButton
import composables.LoanStatusContainer
import composables.NavigateBackTopBar
import composables.TextInputContainer
import theme.labelTextColor

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PayLoanScreen() {

    val state = rememberLazyListState()

    Surface(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background),
        color = Color.White
    ) {
        Column(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) {

            Row(modifier = Modifier.fillMaxWidth()) {

                NavigateBackTopBar("Pay Loan")

            }

            Column(
                modifier = Modifier.fillMaxWidth().fillMaxHeight()
                    .padding(start = 16.dp, end = 16.dp)
                    .background(color = MaterialTheme.colorScheme.background)
            ) {

                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = "My loans",
                    color = labelTextColor
                )

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    state = state,
                    flingBehavior = rememberSnapFlingBehavior(lazyListState = state),
                ) {


                    items(3) {

                        Box(
                            modifier = Modifier
                                .padding(PaddingValues(start = 6.dp, end = 5.dp))
                                .fillParentMaxWidth(0.99f)
                        ) {

                            LoanStatusContainer()
                        }

                    }

                }

                //dotted indicator
                Row(modifier = Modifier.fillMaxWidth()) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp)
                    ) {
                        LazyRow(
                            modifier = Modifier
                                .align(Alignment.Center)
                        ) {
                            items(3) {
                                when (it) {
                                    0 -> {
                                        Box(
                                            modifier = Modifier
                                                .padding(5.dp)
                                                .width(20.dp)
                                                .height(10.dp)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(Color(0xFF489AAB.toInt()))
                                        )
                                    }

                                    1 -> {
                                        Box(
                                            modifier = Modifier
                                                .padding(5.dp)
                                                .size(10.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFFE5E5E5.toInt()))
                                        )
                                    }

                                    2 -> {
                                        Box(
                                            modifier = Modifier
                                                .padding(5.dp)
                                                .size(10.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFFE5E5E5.toInt()))
                                        )
                                    }
                                }
                            }
                        }
                    }

                }

                Row(modifier = Modifier.fillMaxWidth().padding(top = 38.dp)) {

                    TextInputContainer("Desired ammount", "")

                }

                Row(modifier = Modifier.fillMaxWidth().padding(top = 35.dp)) {
                    ActionButton("Pay Now", onClickContainer = {

                    })

                }

                Card(
                    onClick = {

                        //Navigate to view More Details

                    },

                    modifier = Modifier.fillMaxWidth().padding(top = 24.5.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.background)
                ) {

                    Text(
                        "View More Details",
                        fontSize = 12.sp,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                }

            }

        }

    }

}




























