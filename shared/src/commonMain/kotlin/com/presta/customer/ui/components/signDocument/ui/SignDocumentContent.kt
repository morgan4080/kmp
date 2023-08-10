package com.presta.customer.ui.components.signDocument.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.presta.customer.ui.components.signDocument.SignDocumentComponent
import com.presta.customer.ui.composables.ActionButton
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.helpers.LocalSafeArea
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignDocumentContent(component: SignDocumentComponent) {
    Scaffold(modifier = Modifier.padding(LocalSafeArea.current), topBar = {
        NavigateBackTopBar("Sign Document", onClickContainer = {
            component.onBackNavClicked()

        })
    }, content = { innerPadding ->
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)) {
                Row(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxWidth()
                ) {
                    Text("Your guarantorship of Kes has been confirmed")
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                ActionButton(label = "SIGN DOCUMENT",
                    onClickContainer = {

                    })
            }
        }
    })
}

//Show the pdf  Thumbnail
@OptIn(ExperimentalResourceApi::class)
@Composable
fun Base64Image(base64String: String) {
    val painter: Painter = painterResource(res = base64String)
    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier.size(200.dp)
    )
}