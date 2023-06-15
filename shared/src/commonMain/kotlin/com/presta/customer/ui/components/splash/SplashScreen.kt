package com.presta.customer.ui.components.splash

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.presta.customer.MR
import com.presta.customer.SharedStatus
import com.presta.customer.network.onBoarding.model.PinStatus
import com.presta.customer.ui.components.root.DefaultRootComponent
import dev.icerock.moko.resources.compose.fontFamilyResource
import dev.icerock.moko.resources.compose.painterResource
import com.presta.customer.ui.helpers.LocalSafeArea
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SplashScreen(component: SplashComponent, connectivityStatus: SharedStatus?) {
    val model by component.model.subscribeAsState()
    val authState by component.authState.collectAsState()

    LaunchedEffect(connectivityStatus) {
        if (connectivityStatus !== null) {
            connectivityStatus.current.collect{
                println(":::::::connectivityStatus.current")
                println(it)
                println(":::::::connectivityStatus.collect")
            }
        }
    }

    Scaffold (
        modifier = Modifier.
        fillMaxHeight(1f)
            .padding(LocalSafeArea.current)
    ) {
        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxHeight()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column (modifier = Modifier
                .fillMaxHeight(0.95f),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxHeight(0.55f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row (
                        modifier = Modifier.padding(bottom = 33.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        val painter: Painter = painterResource(model.organisation.logo)

                        Image(
                            painter = painter,
                            contentDescription = "Logo",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Row (
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Text(
                            modifier = Modifier.alpha(0.8f),
                            text = model.organisation.tenant_name.uppercase(),
                            style = MaterialTheme.typography.headlineMedium,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.semiBold)
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    println("::::::::::::::authState.cachedMemberData")
                    println(authState.cachedMemberData)
                    if (authState.isLoading || authState.cachedMemberData?.session_id !== "") {
                        CircularProgressIndicator(
                            modifier = Modifier.size(25.dp).padding(end = 2.dp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    } else {
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Button(
                                modifier = Modifier.width(150.dp)
                                    .border(
                                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                                        shape = RoundedCornerShape(size = 12.dp)
                                    ),
                                shape = RoundedCornerShape(size = 12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent,
                                    contentColor = MaterialTheme.colorScheme.primary
                                ),
                                onClick = {
                                    component.onSignInClicked()
                                }
                            ) {
                                Text(
                                    text = "Login",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontFamily = fontFamilyResource(MR.fonts.Poppins.bold)
                                )
                            }
                            Button(
                                modifier = Modifier.width(150.dp),
                                shape = RoundedCornerShape(size = 12.dp),
                                onClick = {
                                    component.onSignUpClicked()
                                }
                            ) {
                                Text(
                                    text = "Sign Up",
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