package com.presta.customer.ui.components.sign

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.ui.helpers.LocalSafeArea
import dev.icerock.moko.resources.compose.fontFamilyResource
import dev.icerock.moko.resources.compose.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignScreen(component: SignComponent) {
    Scaffold(
        modifier = Modifier.padding(LocalSafeArea.current)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {

            Column(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ){
                    val imageModifier = Modifier
                        .size(200.dp)
                    Image(
                        painter = painterResource(MR.images.coming_soon),
                        contentDescription = "coming soon",
                        contentScale = ContentScale.Fit,
                        modifier = imageModifier,
                        alignment = Alignment.Center
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(modifier = Modifier
                        .padding(start = 40.dp, end = 40.dp),
                        text = "Coming Soon",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 20.sp,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.bold),
                        textAlign = TextAlign.Center,
                    )
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 40.dp, end = 40.dp),
                            text = "This new feature will enable you to",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 13.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                            textAlign = TextAlign.Center,
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 60.dp, end = 60.dp),
                            text = "Guarantee & sign loan forms ",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 13.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                            textAlign = TextAlign.Center,
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 75.dp, end = 75.dp),
                            text = "from anywhere, anytime",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 13.sp,
                            fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
    }
}