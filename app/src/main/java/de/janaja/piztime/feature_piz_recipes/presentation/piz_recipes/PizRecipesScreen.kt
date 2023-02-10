package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipes

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import de.janaja.piztime.feature_piz_recipes.presentation.piz_recipes.components.PizCard
import de.janaja.piztime.feature_piz_recipes.presentation.util.Screen

@Composable
fun PizRecipesScreen(
    navController: NavController,
    viewModel: PizRecipesViewModel = hiltViewModel()
){
    var state = viewModel.state.value

    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        val color = MaterialTheme.colorScheme.background
        Log.i("svg", "${color.red} ${color.green} ${color.blue}")
        state.pizRecipes.forEach { pizRecipe ->
            PizCard(
                pizRecipe = pizRecipe,
                onClick = {
                    navController.navigate(Screen.PizRecipeDetailScreen.route + "pizRecipeId=${pizRecipe.id}")
                })
        }
    }
}


