package composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.unit.dp
import components.countries.CountriesComponent
import components.countries.Country
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun CountryRow(country: Country, component: CountriesComponent) {
    val url = "https://flagcdn.com/28x21/${country.alpha2Code.lowercase()}.png"
    LazyRow (
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                component.onSelected(Json.encodeToString(country))
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        contentPadding = PaddingValues(vertical = 10.dp, horizontal = 16.dp)
    ) {
        item {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Box(modifier= Modifier.padding(end = 10.dp), contentAlignment = Alignment.TopCenter) {
                    AsyncImage(
                        url,
                        null,
                        colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(2f) }),
                        modifier = Modifier
                            .width(35.dp)
                            .height(30.dp)
                            .alpha(.9f)
                    )
                }

                Text(
                    text = country.name,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        item {
            Text(
                text = "+${country.code}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}