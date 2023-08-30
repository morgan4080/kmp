package com.presta.customer.ui.components.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.presta.customer.SharedStatus
import com.presta.customer.ui.components.auth.store.AuthStore
import com.presta.customer.ui.helpers.LocalSafeArea
import dev.icerock.moko.resources.compose.painterResource
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SplashScreen(component: SplashComponent, connectivityStatus: SharedStatus?) {
    val model by component.model.subscribeAsState()
    val authState by component.authState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(connectivityStatus) {
        if (connectivityStatus !== null) {
            connectivityStatus.current.collect{
                component.onEvent(AuthStore.Intent.UpdateOnlineState(it))
                if (!it) {
                    snackBarHostState.showSnackbar(
                        message = "Checking network connection",
                        duration = SnackbarDuration.Short
                    )
                } else {
                    component.onEvent(AuthStore.Intent.UpdateLoading)
                    delay(3000L)
                    component.onSignInClicked()
                }
            }
        }
    }

    Scaffold (
        modifier = Modifier
            .fillMaxHeight(1f)
            .padding(LocalSafeArea.current),
        snackbarHost = { SnackbarHost(modifier = Modifier.padding(bottom = 10.dp), hostState = snackBarHostState) },
        floatingActionButton = {
            Row (
                modifier = Modifier.fillMaxWidth(0.9f),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    "Powered By Presta",
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    style = TextStyle(fontSize = 10.sp),
                    fontWeight = FontWeight.Light,
                    fontStyle = FontStyle.Italic
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row (
                modifier = Modifier.padding(bottom = 33.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                when(isSystemInDarkTheme()) {
                    false -> Image(
                        painter = painterResource(model.organisation.logo),
                        contentDescription = "Logo"
                    )
                    true -> Image(
                        painter = painterResource(model.organisation.logodark),
                        contentDescription = "Logo"
                    )
                }
            }
            Row (
                modifier = Modifier.alpha(if (authState.isLoading || authState.cachedMemberData?.session_id !== "" || connectivityStatus == null || !authState.isOnline)  1f else 0f),
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(25.dp).padding(end = 2.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}