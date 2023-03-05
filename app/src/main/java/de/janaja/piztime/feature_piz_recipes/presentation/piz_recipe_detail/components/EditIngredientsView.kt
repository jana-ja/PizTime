package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import de.janaja.piztime.feature_piz_recipes.domain.model.PizIngredient
import de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.PizRecipeDetailViewModel
import de.janaja.piztime.feature_piz_recipes.presentation.util.DummyData

@Composable
fun EditIngredientsView(
    modifier: Modifier = Modifier,
    viewModel: PizRecipeDetailViewModel = hiltViewModel()
) {

    // retrieve ingredients from state once. then have a list of stateful ingredients here to edit. finally save at the end.
    val ingredients = viewModel.pizIngredientsState.value.pizIngredients

    EditIngredientsViewContent(modifier = modifier, ingredients = ingredients)



}

@Composable
private fun EditIngredientsViewContent(
    modifier: Modifier,
    ingredients: List<PizIngredient>
){
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)

    ) {
        item() {
            Text(
                "Zutaten",
                style = MaterialTheme.typography.titleLarge
            )
        }

        items(count = ingredients.size, key = { it }) { index ->

            var ingredient by remember { mutableStateOf(ingredients[index].ingredient) }
            var amount by remember { mutableStateOf(ingredients[index].baseAmount) }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(PaddingValues(start = 16.dp, top = 16.dp, end = 8.dp)),
            ) {
                TextField(
                    value = "$amount",
                    onValueChange = { amount = it.toDouble() }, // TODO error handling
                    maxLines = 1,
                    modifier = Modifier
                        .weight(.3f)
                        .padding(end = 8.dp)
                )
                TextField(
                    value = (ingredient),
                    onValueChange = { ingredient = it },
                    maxLines = 1,
                    modifier = Modifier
                        .weight(.7f)
                )

            }
        }
    }
}


@Preview
@Composable
fun EditIngredientsViewPreview() {
    EditIngredientsViewContent(modifier = Modifier, DummyData.DummyIngredients)
}