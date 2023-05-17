package components.productScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import composables.ProductSelectionCard


@Composable
fun ProductScreen(){

    Column(modifier = Modifier.fillMaxWidth()){


        LazyColumn(modifier = Modifier
            .fillMaxWidth()){

            item {

               ProductSelectionCard("Emergency Loan ","Interest 12%", onClickContainer = {


                })


            }

            item {

               ProductSelectionCard("Mkopo Halisi Loan ","Interest 12%", onClickContainer = {

                })


            }

            item {

               ProductSelectionCard("Shule Loan ","Interest 12%", onClickContainer = {

                })


            }

            item {

                ProductSelectionCard("Mfanisi Loan ","Interest 12%", onClickContainer = {

                })


            }

            item {


                ProductSelectionCard("Normal Loan ","Interest 12%", onClickContainer = {

                })


            }

        }

    }
}