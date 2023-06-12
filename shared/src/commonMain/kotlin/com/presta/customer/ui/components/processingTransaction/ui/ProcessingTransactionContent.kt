import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.network.payments.model.PaymentStatuses
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.components.processingTransaction.store.ProcessingTransactionStore
import com.presta.customer.ui.helpers.formatMoney
import dev.icerock.moko.resources.compose.fontFamilyResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProcessingTransactionContent(
    authState: AuthStore.State,
    state: ProcessingTransactionStore.State,
    amount: Double,
    retryTransaction: () -> Unit,
    navigateBack: () -> Unit
) {
    Scaffold (
        modifier = Modifier.fillMaxHeight().fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
            Row(modifier = Modifier.fillMaxWidth()) {

            }

            Column(modifier = Modifier.fillMaxWidth().padding(top = 50.dp)) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Phone Number ${authState.cachedMemberData?.phoneNumber}",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 14.sp,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.medium)
                    )

                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "KSH ${formatMoney(amount)}")
                }

            }

            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(vertical = 60.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {

                        AnimatedVisibility(!(state.paymentStatus !== null
                                &&
                                (state.paymentStatus.status == PaymentStatuses.CANCELLED
                                        ||
                                        state.paymentStatus.status == PaymentStatuses.FAILURE))) {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(70.dp)
                                    .background(MaterialTheme.colorScheme.background)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.background),
                                contentAlignment = Alignment.Center
                            ) {
                                if (state.isLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.then(Modifier.size(60.dp).alpha(if (state.isLoading) 1f else 0.0f)),
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Filled.CheckCircle,
                                        contentDescription = null,
                                        modifier = Modifier.size(150.dp),
                                        tint = Color.Green
                                    )
                                }
                            }
                        }

                        AnimatedVisibility((state.paymentStatus !== null
                                &&
                                (state.paymentStatus.status == PaymentStatuses.CANCELLED
                                        ||
                                        state.paymentStatus.status == PaymentStatuses.FAILURE))) {
                            Icon(
                                imageVector = Icons.Filled.Cancel,
                                contentDescription = null,
                                modifier = Modifier.size(150.dp),
                                tint = Color.Red
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.padding(start = 80.dp, end = 80.dp, top = 50.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Your transaction is ${if (state.paymentStatus !== null) state.paymentStatus.status else ""}",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 14.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                AnimatedVisibility((state.paymentStatus !== null
                        &&
                        (state.paymentStatus.status == PaymentStatuses.CANCELLED
                                ||
                                state.paymentStatus.status == PaymentStatuses.FAILURE))) {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(
                                modifier = Modifier.width(150.dp)
                                    .border(
                                        border = BorderStroke(
                                            1.dp,
                                            MaterialTheme.colorScheme.outline
                                        ),
                                        shape = RoundedCornerShape(size = 12.dp)
                                    ),
                                shape = RoundedCornerShape(size = 12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent,
                                    contentColor = MaterialTheme.colorScheme.primary
                                ),
                                onClick = {
                                    navigateBack()
                                }
                            ) {
                                Text(
                                    text = "Close",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.bold)
                                )
                            }
                            Button(
                                modifier = Modifier.width(150.dp),
                                shape = RoundedCornerShape(size = 12.dp),
                                onClick = {
                                    retryTransaction()
                                }
                            ) {
                                Text(
                                    text = "Retry",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.bold)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}