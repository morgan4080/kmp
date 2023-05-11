package components.countries

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import dev.icerock.moko.resources.compose.readTextAsState
import helpers.LocalSafeArea
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Serializable
data class Country(val name: String, val code: String, val alpha2Code: String, val numericCode: String)
@Composable
fun CountriesScreen(component: CountriesComponent) {
    val model by component.model.subscribeAsState()
    val countriesJson: String? by model.countriesJSON.readTextAsState()
    var countriesObject: List<Country> = mutableListOf()
    if (countriesJson != null) {
        countriesObject = Json.decodeFromString("$countriesJson")
    }

    LazyColumn (
        modifier = Modifier.fillMaxHeight(1f).padding(LocalSafeArea.current),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        items(items = countriesObject) { item ->
            Row {
                Text(
                    text = item.name,
                    fontSize = 4.em,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}