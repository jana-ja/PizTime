package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import de.janaja.piztime.feature_piz_recipes.presentation.edit_recipe.EditRecipeEvent
import de.janaja.piztime.feature_piz_recipes.presentation.edit_recipe.EditRecipeViewModel
import de.janaja.piztime.feature_piz_recipes.presentation.util.DummyData

@Composable
fun EditIngredientsView(
    modifier: Modifier = Modifier,
    viewModel: EditRecipeViewModel = hiltViewModel(),
    new: MutableState<Boolean>,
    dismissDialog: () -> Unit // TODO ugly solution
) {

    // really ugly solution to reset state when this is opened in a dialog, but not reset it on recomposition
    if (new.value) {
        viewModel.reloadIngredients()
    }
    EditIngredientsViewContent(
        modifier = modifier,
        ingredientNames = viewModel.pizIngredientsState.value.ingredientNames,
        ingredientAmounts = viewModel.pizIngredientsState.value.ingredientAmounts,
        viewModel::onEvent,
        dismissDialog
    )


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditIngredientsViewContent(
    modifier: Modifier = Modifier,
    ingredientNames: List<String>,
    ingredientAmounts: List<Double>,
    onEvent: (EditRecipeEvent) -> Unit,
    dismissDialog: () -> Unit // TODO ugly solution
) {

    // TODO states of textfields cant be inside of items because data will get lost on scrolling
    //  could have a statelist on top but state should be handled from viewmodel
    //  how to avoid recomposing every textfield when one state inside the list changes
    Scaffold(
        topBar = {
            Text(
                "Zutaten",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )
        },
        bottomBar = {
            Button(
                onClick = { onEvent(EditRecipeEvent.ClickSaveIngredients); dismissDialog() },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .wrapContentWidth(align = Alignment.End),
            ) {
                Icon(
                    Icons.Rounded.Check,
                    "add ingredient",
                )
            }
        }
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .padding(it)
                .padding(end = 24.dp, start = 16.dp)
                .padding(vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)

        ) {


            items(count = ingredientNames.size, key = { it }) { index ->

                Row(
                    Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { onEvent(EditRecipeEvent.ClickRemove(index)) },
                        //modifier = Modifier.padding(end = 8.dp)
                    ) {

                        Icon(
                            Icons.Default.Delete,
                            "delete ingredient",
                            Modifier.fillMaxHeight()
                        )
                    }

                    TextField(
                        value = "${ingredientAmounts[index]}",
                        onValueChange = { onEvent(EditRecipeEvent.AmountChanged(index, it)) },
                        maxLines = 1,
                        modifier = Modifier
                            .weight(.3f)
                            .padding(end = 8.dp)
                    )

                    TextField(
                        value = (ingredientNames[index]),
                        onValueChange = { onEvent(EditRecipeEvent.NameChanged(index, it)) },
                        maxLines = 1,
                        modifier = Modifier
                            .weight(.7f)
                    )

                }
            }

            item() {
                IconButton(
                    onClick = { onEvent(EditRecipeEvent.ClickAdd) },
                    modifier = Modifier.padding(start = 8.dp, top = 16.dp)
                ) {
                    Icon(
                        Icons.Default.Add,
                        "add ingredient",
                        Modifier.fillMaxHeight()
                    )
                }
            }

        }
    }
}


@Preview
@Composable
fun EditIngredientsViewPreview() {
    EditIngredientsViewContent(
        modifier = Modifier,
        ingredientNames = DummyData.DummyIngredients.map { ingredient -> ingredient.ingredient }.toMutableStateList(),
        ingredientAmounts = DummyData.DummyIngredients.map { ingredient -> ingredient.baseAmount },
        onEvent = {},
        dismissDialog = {})
}