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
import de.janaja.piztime.feature_piz_recipes.domain.model.PizIngredient
import de.janaja.piztime.feature_piz_recipes.presentation.edit_recipe.EditRecipeEvent
import de.janaja.piztime.feature_piz_recipes.presentation.edit_recipe.EditRecipeViewModel
import de.janaja.piztime.feature_piz_recipes.presentation.util.DummyData

@Composable
fun EditStepsView(
    modifier: Modifier = Modifier,
    viewModel: EditRecipeViewModel = hiltViewModel(),
    new: MutableState<Boolean>,
    dismissDialog: () -> Unit // TODO ugly solution
) {

    // really ugly solution to reset state when this is opened in a dialog, but not reset it on recomposition
    if (new.value) {
        viewModel.reloadSteps()
    }
    EditStepsViewContent(
        modifier = modifier,
        steps = viewModel.pizStepsWithIngredientsState.value.stepDescriptions,
        ingredients = viewModel.pizStepsWithIngredientsState.value.ingredients,
        viewModel::onEvent,
        dismissDialog
    )


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditStepsViewContent(
    modifier: Modifier = Modifier,
    steps: List<String>,
    ingredients: List<List<PizIngredient>>,
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
                onClick = { onEvent(EditRecipeEvent.ClickSaveSteps); dismissDialog() },
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


            items(count = steps.size, key = { it }) { index ->

                Row(
                    Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { onEvent(EditRecipeEvent.ClickRemoveStep(index)) },
                        //modifier = Modifier.padding(end = 8.dp)
                    ) {

                        Icon(
                            Icons.Default.Delete,
                            "delete ingredient",
                            Modifier.fillMaxHeight()
                        )
                    }

                    TextField(
                        value = steps[index],
                        onValueChange = { onEvent(EditRecipeEvent.StepChanged(index, it)) },
                        modifier = Modifier
                            .padding(end = 8.dp)
                    )


                }
            }

            item() {
                IconButton(
                    onClick = { onEvent(EditRecipeEvent.ClickAddStep) },
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
fun EditStepsViewPreview() {
    EditStepsViewContent(
        modifier = Modifier,
        steps = DummyData.DummySteps.map { step -> step.description },
        ingredients = DummyData.DummySteps.map { step -> step.ingredients },
        onEvent = {},
        dismissDialog = {})
}