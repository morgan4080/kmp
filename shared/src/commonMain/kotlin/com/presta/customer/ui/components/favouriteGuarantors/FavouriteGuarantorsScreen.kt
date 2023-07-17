package com.presta.customer.ui.components.favouriteGuarantors

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.presta.customer.MR
import com.presta.customer.ui.composables.NavigateBackTopBar
import com.presta.customer.ui.helpers.LocalSafeArea
import dev.icerock.moko.resources.compose.fontFamilyResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouriteGaurantorsScreen(component: FavouriteGuarantorsComponent) {
    Scaffold(modifier = Modifier.padding(LocalSafeArea.current), topBar = {
        NavigateBackTopBar("Favourite Guarantors", onClickContainer = {
            component.onBackNavClicked()

        })
    }, content = { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp)
        ) {
            item {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally){
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 70.dp),
                        horizontalArrangement = Arrangement.Center){
                        Icon(
                            imageVector = Icons.Outlined.Inventory2,
                            modifier = Modifier
                                .size(70.dp),
                            contentDescription = "No data",
                            tint = MaterialTheme.colorScheme.outline
                        )
                    }
                    Text("Whoops",
                        fontSize = 13.sp,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                    )
                    Text("No Data",
                        fontSize = 10.sp,
                        fontFamily = fontFamilyResource(MR.fonts.Poppins.light)
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.padding(bottom = 100.dp))
            }
        }
    })

}













