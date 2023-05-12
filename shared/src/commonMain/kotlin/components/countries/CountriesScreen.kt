package components.countries

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import composables.AsyncImage
import dev.icerock.moko.resources.compose.readTextAsState
import helpers.LocalSafeArea
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi
import theme.backArrowColor

@Serializable
data class Country(val name: String, val code: String, val alpha2Code: String, val numericCode: String)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun CountriesScreen(component: CountriesComponent) {
    val model by component.model.subscribeAsState()
    val countriesJson: String? by model.countriesJSON.readTextAsState()
    var countriesObject: List<Country> = mutableListOf()
    if (countriesJson != null) {
        countriesObject = Json.decodeFromString("$countriesJson")
    }

    Scaffold(
        modifier = Modifier.padding(LocalSafeArea.current),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier,
                navigationIcon = {
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBackIos,
                            contentDescription = "Navigates back",
                            tint = backArrowColor
                        )
                    }
                },
                title = {
                    Text(
                        text = "Countries",
                        fontWeight = FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(start = 1.dp),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background)

            )
        }
    ) {

        LazyColumn (
            modifier = Modifier
                .padding(vertical = 20.dp)
                .fillMaxHeight(1f)
                .padding(LocalSafeArea.current)
                .background(MaterialTheme.colorScheme.background)
        ) {
            items(items = countriesObject) { country ->
                val url = "https://flagcdn.com/28x21/${country.alpha2Code.lowercase()}.png"
                LazyRow (
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {

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
                            // width: 20, height: 15
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
                                fontSize = 16.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }
                    item {
                        Text(
                            text = "+${country.code}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Light,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    }
}
