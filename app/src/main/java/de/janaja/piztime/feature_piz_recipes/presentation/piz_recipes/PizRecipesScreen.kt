package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
    val state = viewModel.state.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {

        state.pizRecipes.forEachIndexed() { index, pizRecipe ->
                PizCard(
                    pizRecipe = pizRecipe,
                    onClick = {
                        navController.navigate(Screen.PizRecipeDetailScreen.route + "pizRecipeId=${pizRecipe.id}")
                    },
                    index = index)
        }
    }
}


