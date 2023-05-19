package components.countries

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import composables.CountryRow
import composables.StickyHeaderContent
import dev.icerock.moko.resources.compose.readTextAsState
import helpers.LocalSafeArea
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import theme.backArrowColor

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CountriesScreen(component: CountriesComponent) {
    val model by component.model.subscribeAsState()
    val focusRequester = remember { FocusRequester() }
    var searching by remember { mutableStateOf(false) }
    var searchingInput by remember { mutableStateOf("") }
    val countriesJson: String? by model.countriesJSON.readTextAsState()
    var countriesObject: List<Country> = remember { mutableListOf(
        Country("Kenya", "254", "KE", "404"),
        Country("Tanzania", "255", "TZ", "834")
    )}

    if (countriesJson != null) {
        countriesObject = Json.decodeFromString("$countriesJson")
    }

    LaunchedEffect(searchingInput, countriesJson) {
        kotlin.run {
            countriesObject = Json.decodeFromString("$countriesJson")
            countriesObject = countriesObject.filter { country: Country -> country.name.lowercase().indexOf(searchingInput) > -1 }
        }
    }

    LaunchedEffect(searching) {
        if (searching) {
            try {
                focusRequester.requestFocus()
            } catch (e: IllegalStateException) {
                println(e)
            }
        } else {
            try {
                focusRequester.freeFocus()
            } catch (e: IllegalStateException) {
                println(e)
            }
        }
    }

    Scaffold(
        modifier = Modifier.padding(LocalSafeArea.current),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(top = LocalSafeArea.current.calculateTopPadding()),
                navigationIcon = {
                    IconButton(onClick = {
                        component.onBack()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBackIos,
                            contentDescription = "Navigates back",
                            tint = backArrowColor
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        searching = !searching
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Activates Search",
                            tint = backArrowColor
                        )
                    }
                },
                title = {
                    if (searching) {
                        BasicTextField(
                            modifier = Modifier
                                .focusRequester(focusRequester)
                                .fillMaxWidth(),
                            value = searchingInput,
                            onValueChange = {
                                searchingInput = it
                            },
                            singleLine = true,
                            decorationBox = { innerTextField ->
                                Box (
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    if (searchingInput.isEmpty()) {
                                        Text(
                                            modifier = Modifier.alpha(.3f),
                                            text = "Search",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                    innerTextField()
                                }
                            }
                        )
                    } else {
                        Text(
                            text = "Countries",
                            fontWeight = FontWeight.Normal,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(start = 1.dp),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = if (searching) MaterialTheme.colorScheme.inverseOnSurface else MaterialTheme.colorScheme.background)

            )
        }
    ) { innerPadding ->
        LazyColumn (
            modifier = Modifier
                .fillMaxHeight(1f)
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            countriesObject.groupBy { it.name[0] }.forEach { (initial, countriesForInitial) ->
                stickyHeader {
                    StickyHeaderContent(initial)
                }
                items(items = countriesForInitial) { country ->
                    CountryRow(country)
                }
            }
        }
    }
}
