package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import de.janaja.piztime.feature_piz_recipes.domain.model.PizIngredient
import de.janaja.piztime.feature_piz_recipes.domain.model.PizRecipe
import de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.components.AmountSelector
import de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.components.DescriptionView
import de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.components.IngredientsView
import de.janaja.piztime.feature_piz_recipes.presentation.util.PreviewDummies

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PizRecipeDetailScreen(
    viewModel: PizRecipeDetailViewModel = hiltViewModel()
) {
    val recipeState = viewModel.pizRecipeState.value
    val amountState = viewModel.pizAmountState.value

    val scope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }
            ) {

            }
        }//,
        //scaffoldState = scaffoldState
    ) {
        PizRecipeDetailView(
            Modifier.padding(it),
            recipeState.pizRecipe,
            recipeState.pizIngredients,
            amountState.amount,
            { viewModel.increaseAmount() },
            { viewModel.decreaseAmount() }
        )
    }

}

@Composable
fun PizRecipeDetailView(
    modifier: Modifier,
    pizRecipe: PizRecipe,
    pizIngredients: List<PizIngredient>,
    amount: Int,
    increaseAmount: () -> Unit,
    decreaseAmount: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = pizRecipe.title,
            style = MaterialTheme.typography.headlineMedium
        )

        AmountSelector(
            amount,
            { increaseAmount() },
            { decreaseAmount() }
        )

        IngredientsView(pizIngredients, amount)

        DescriptionView(pizRecipe.description)
    }
}

@Preview
@Composable
fun PizRecipeDetailViewPreview() {
    PizRecipeDetailView(
        Modifier,
        PreviewDummies.DummyPizRecipe,
        PreviewDummies.DummyIngredients,
        4,
        {  },
        {  })
}


