package composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import components.countries.Country

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CountriesSearch(countries: List<Country>) {
    LazyColumn (
        modifier = Modifier
            .fillMaxHeight(0.4f)
            .background(MaterialTheme.colorScheme.background)
    ) {
        countries.groupBy { it.name[0] }.forEach { (initial, countriesForInitial) ->
            stickyHeader {
                StickyHeaderContent(initial)
            }
            items(items = countriesForInitial) { country ->
                CountryRow(country)
            }
        }
    }
}