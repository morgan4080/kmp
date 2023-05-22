package composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.presta.customer.MR
import components.onBoarding.store.Country
import components.onBoarding.store.OnBoardingStore
import dev.icerock.moko.resources.compose.fontFamilyResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun CountriesSearch(
    countryData: List<Country>,
    scope: CoroutineScope,
    scaffoldState: ModalBottomSheetState,
    onEvent: (OnBoardingStore.Intent) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
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
        Row (modifier = Modifier.fillMaxWidth()) {
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
                modifier = Modifier.focusRequester(focusRequester).fillMaxWidth()
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
    }
    LazyColumn (
        modifier = Modifier
            .fillMaxHeight()
            .padding(bottom = 10.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        items(countries) { country ->
            CountryRow(country, callback = {
                kc?.hide()
                onEvent(OnBoardingStore.Intent.SelectCountry(it))
                scope.launch {
                    if (scaffoldState.isVisible) {
                        scaffoldState.hide()
                    } else {
                        scaffoldState.show()
                    }
                }
            })
        }
    }
}