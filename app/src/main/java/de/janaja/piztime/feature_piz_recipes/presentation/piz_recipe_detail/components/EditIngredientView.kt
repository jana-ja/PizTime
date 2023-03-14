package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.PizRecipeDetailEvent
import de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.PizRecipeDetailViewModel
import de.janaja.piztime.feature_piz_recipes.presentation.util.DummyData

@Composable
fun EditIngredientView(
    modifier: Modifier = Modifier,
    viewModel: PizRecipeDetailViewModel = hiltViewModel()
) {

    val state = viewModel.editIngredientState.value
    EditIngredientViewContent(
        modifier = modifier,
        ingredientName = state.ingredientName,
        ingredientAmount = state.ingredientAmount,
        viewModel::onEvent
    )


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditIngredientViewContent(
    modifier: Modifier = Modifier,
    ingredientName: String,
    ingredientAmount: String,
    onEvent: (PizRecipeDetailEvent) -> Unit
) {

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(
                        "Zutaten",
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                actions = {
                    IconButton(
                        onClick = { onEvent(PizRecipeDetailEvent.ClickDeleteIngredient) },
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            "delete ingredient",
                            Modifier.fillMaxHeight()
                        )
                    }
                }
            )

        },
        bottomBar = {
            Button(
                onClick = { onEvent(PizRecipeDetailEvent.ClickSaveIngredient) },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .wrapContentWidth(align = Alignment.End),
            ) {
                Icon(
                    Icons.Rounded.Check,
                    "save ingredient",
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(end = 24.dp, start = 16.dp)
                .padding(vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)

        ) {

            TextField(
                value = ingredientAmount,
                onValueChange = { onEvent(PizRecipeDetailEvent.IngredientAmountChanged(it)) },
                maxLines = 1,
                modifier = Modifier
                    .weight(.3f)
                    .padding(end = 8.dp)
            )

            TextField(
                value = (ingredientName),
                onValueChange = { onEvent(PizRecipeDetailEvent.IngredientNameChanged(it)) },
                maxLines = 1,
                modifier = Modifier
                    .weight(.7f)
            )

        }
    }
}


@Preview
@Composable
fun EditIngredientViewPreview() {
    EditIngredientViewContent(
        modifier = Modifier,
        ingredientName = DummyData.DummyIngredients.first().ingredient,
        ingredientAmount = DummyData.DummyIngredients.first().baseAmount.toString(),
        onEvent = {}
    )
}