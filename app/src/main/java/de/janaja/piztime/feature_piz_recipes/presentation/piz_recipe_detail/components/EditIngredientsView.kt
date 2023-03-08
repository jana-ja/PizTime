package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.PizRecipeDetailViewModel
import de.janaja.piztime.feature_piz_recipes.presentation.util.DummyData

@Composable
fun EditIngredientsView(
    modifier: Modifier = Modifier,
    viewModel: PizRecipeDetailViewModel = hiltViewModel()
) {

    // TODO implement UIEvent instead of direct callbacks
    EditIngredientsViewContent(
        modifier = modifier,
        ingredientNames = viewModel.pizIngredientsState.value.ingredientNames,
        ingredientAmounts = viewModel.pizIngredientsState.value.ingredientAmounts,
        viewModel::updateIngredients,
        viewModel::removeIngredientFromState,
        viewModel::addIngredientToState,
        viewModel::changeIngredientNameInState,
        viewModel::changeIngredientAmountInState

    )


}

@Composable
fun EditIngredientsViewContent(
    modifier: Modifier = Modifier,
    ingredientNames: SnapshotStateList<String>,
    ingredientAmounts: List<Double>,
    onClickSave: () -> Unit,
    onClickRemove: (index: Int) -> Unit, // TODO really remove by index?
    onClickAdd: () -> Unit,
    onIngredientNameChanged: (index: Int, value: String) -> Unit,
    onIngredientAmountChanged: (index: Int, value: String) -> Unit
) {
//    val ingredientNames: SnapshotStateList<String>
//    val ingredientAmounts: SnapshotStateList<Double>
//
//    // TODO use viewmodel state
//    ingredients.also {
//        ingredientNames = remember{ ingredients.map { ingredient -> ingredient.ingredient }.toMutableStateList() }
//        ingredientAmounts = remember { ingredients.map { ingredient -> ingredient.baseAmount }.toMutableStateList() }
//
//    }

    // TODO states of textfields cant be inside of items because data will get lost on scrolling
    //  could have a statelist on top but state should be handled from viewmodel
    //  how to avoid recomposing every textfield when one state inside the list changes

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

        items(count = ingredientNames.size, key = { it }) { index ->

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(PaddingValues(start = 8.dp, top = 16.dp, end = 8.dp)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onClickRemove(index) },
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
                    onValueChange = { onIngredientAmountChanged(index, it) }, // TODO error handling
                    maxLines = 1,
                    modifier = Modifier
                        .weight(.3f)
                        .padding(end = 8.dp)
                )

                TextField(
                    value = (ingredientNames[index]),
                    onValueChange = { onIngredientNameChanged(index, it) },
                    maxLines = 1,
                    modifier = Modifier
                        .weight(.7f)
                )

            }
        }

        item() {
            IconButton(
                onClick = { onClickAdd() },
                modifier = Modifier.padding(start = 8.dp, top = 16.dp)
            ) {
                Icon(
                    Icons.Default.Add,
                    "add ingredient",
                    Modifier.fillMaxHeight()
                )
            }
        }

        item() {
            Button(
                onClick = onClickSave,
                modifier = Modifier
                    .padding(start = 8.dp, top = 16.dp)
                    .fillMaxWidth()
                    .wrapContentWidth(align = Alignment.End),
            ) {
                Icon(
                    Icons.Rounded.Check,
                    "add ingredient",
                    Modifier.fillMaxHeight()
                )
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
        onClickSave = {},
        onClickRemove = {},
        onClickAdd = {},
        onIngredientNameChanged = { _, _ -> },
        onIngredientAmountChanged = { _, _ -> })
}