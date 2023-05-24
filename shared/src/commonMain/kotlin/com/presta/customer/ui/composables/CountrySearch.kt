package com.presta.customer.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.presta.customer.MR
import com.presta.customer.ui.components.onBoarding.store.Country
import com.presta.customer.ui.components.onBoarding.store.OnBoardingStore
import dev.icerock.moko.resources.compose.fontFamilyResource
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun CountriesSearch(
    countryData: List<Country>,
    scaffoldState: ModalBottomSheetState,
    onEvent: (OnBoardingStore.Intent) -> Unit
) {
    val scope = rememberCoroutineScope()
    var searchingInput by remember { mutableStateOf("") }
    var countries = countryData
    countries = countries.filter { country: Country -> country.name.lowercase().indexOf(searchingInput.lowercase()) > -1 }
    val kc = LocalSoftwareKeyboardController.current
    Column (
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = {
                    kc?.hide()
                    scope.launch {
                        if (scaffoldState.isVisible) {
                            scaffoldState.hide()
                        } else {
                            scaffoldState.show()
                        }
                    }
                },
                content = {
                    Icon(
                        modifier = Modifier.size(25.dp).alpha(0.7f),
                        imageVector = Icons.Filled.Cancel,
                        contentDescription = null,
                        tint = Color.Red
                    )
                }
            )

        }
        Row (
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.fillMaxWidth().absoluteOffset(y = (-20).dp),
                text = "Select a country",
                style = MaterialTheme.typography.titleMedium,
                fontFamily = fontFamilyResource(MR.fonts.Poppins.medium),
                textAlign = TextAlign.Center
            )
        }
        Row (
            modifier = Modifier
                .fillMaxWidth().padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            BasicTextField(
                modifier = Modifier.fillMaxWidth()
                    .padding(PaddingValues(horizontal = 16.dp))
                    .background(color = MaterialTheme.colorScheme.inverseOnSurface, shape = RoundedCornerShape(10.dp)),
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    fontStyle = MaterialTheme.typography.bodyLarge.fontStyle,
                    letterSpacing = MaterialTheme.typography.bodyLarge.letterSpacing,
                    lineHeight = MaterialTheme.typography.bodyLarge.lineHeight,
                    fontFamily = MaterialTheme.typography.bodyLarge.fontFamily
                ),
                value = searchingInput,
                onValueChange = {
                    searchingInput = it
                },
                singleLine = true,
                decorationBox = { innerTextField ->
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                modifier = Modifier.padding(8.dp),
                                imageVector = Icons.Filled.Search,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.outline
                            )
                            innerTextField()
                        }
                        IconButton(onClick = {
                            searchingInput = ""
                        }, content = {
                            Icon(
                                modifier = Modifier.padding(8.dp),
                                imageVector = Icons.Filled.Cancel,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.outline
                            )
                        })
                    }
                }
            )
        }
        LazyColumn (
            modifier = Modifier
                .padding(bottom = 10.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            countries.forEach { country ->
                item {
                    val url = "https://flagcdn.com/h40/${country.alpha2Code.lowercase()}.png"

                    ListItem(
                        modifier = Modifier.clickable {
                            kc?.hide()
                            onEvent(OnBoardingStore.Intent.SelectCountry(country))
                            scope.launch {
                                if (scaffoldState.isVisible) {
                                    scaffoldState.hide()
                                } else {
                                    scaffoldState.show()
                                }
                            }
                        },
                        leadingContent = {
                            AsyncImage(
                                url,
                                null,
                                colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(2f) }),
                                modifier = Modifier
                                    .width(35.dp)
                                    .height(30.dp)
                                    .alpha(.9f)
                            )
                        },
                        headlineText = {
                            Text(
                                text = country.name,
                                style = MaterialTheme.typography.bodyMedium,
                                overflow = TextOverflow.Ellipsis,
                            )
                        },
                        trailingContent = {
                            Text(
                                text = "+${country.code}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    )
                }
            }
        }
    }
}