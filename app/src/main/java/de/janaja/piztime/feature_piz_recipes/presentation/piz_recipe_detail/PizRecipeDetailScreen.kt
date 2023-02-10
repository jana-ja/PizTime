package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.components.AmountSelector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PizRecipeDetailScreen(
    pizRecipeId: Long,
    navController: NavController,
    viewModel: PizRecipeDetailViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    val scope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }
            ) {

            }
        }//,
        //scaffoldState = scaffoldState
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            AmountSelector()
        }
    }

}