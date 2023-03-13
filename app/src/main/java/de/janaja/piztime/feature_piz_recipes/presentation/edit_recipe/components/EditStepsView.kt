package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import de.janaja.piztime.feature_piz_recipes.presentation.util.SimpleFlowRow
import kotlinx.coroutines.launch

@Composable
fun EditStepsView(
    modifier: Modifier = Modifier,
    viewModel: EditRecipeViewModel = hiltViewModel(),
    dismissDialog: () -> Unit // TODO ugly solution
) {

    val coroutineScope = rememberCoroutineScope()
    // reset state when this is opened in a dialog, but not reset it on recomposition
    // launched effect is not triggered on recomposition
    LaunchedEffect(coroutineScope) {
        viewModel.reloadSteps()
    }
    EditStepsViewContent(
        modifier = modifier,
        steps = viewModel.pizStepsWithIngredientsState.value.stepDescriptions,
        ingredientNamesLists = viewModel.pizStepsWithIngredientsState.value.stepIngredientsNames,
        ingredientAmountsLists = viewModel.pizStepsWithIngredientsState.value.stepIngredientsAmounts,
        viewModel::onEvent,
        dismissDialog
    )


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditStepsViewContent(
    modifier: Modifier = Modifier,
    steps: List<String>,
    ingredientNamesLists: List<List<String>>,
    ingredientAmountsLists: List<List<Double>>,
    onEvent: (EditRecipeEvent) -> Unit,
    dismissDialog: () -> Unit // TODO ugly solution
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
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
                .padding(vertical = 24.dp)
            //.background(Color.LightGray)
            ,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = listState

        ) {


            items(count = steps.size, key = { it }) { stepIndex ->
                Column() {
                    Row(
                        Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { onEvent(EditRecipeEvent.ClickRemoveStep(stepIndex)) },
                            //modifier = Modifier.padding(end = 8.dp)
                        ) {

                            Icon(
                                Icons.Default.Delete,
                                "delete ingredient",
                                Modifier.fillMaxHeight()
                            )
                        }



                        TextField(
                            value = steps[stepIndex],
                            onValueChange = { onEvent(EditRecipeEvent.StepChanged(stepIndex, it)) },
                            modifier = Modifier
                                .padding(end = 8.dp)
                        )

                    }
                    // step ingredients
                    if (ingredientNamesLists[stepIndex].isNotEmpty()) {
                        SimpleFlowRow(
                            verticalGap = 8.dp,
                            horizontalGap = 8.dp,
                            //modifier = Modifier.padding(start = 16.dp, top = 8.dp)
                        ) {
                            ingredientNamesLists[stepIndex].indices.forEach { ingredientIndex ->
                                val name = ingredientNamesLists[stepIndex][ingredientIndex]
                                val amount = ingredientAmountsLists[stepIndex][ingredientIndex].toString()
                                Row() {
                                    TextField(
                                        value = name,
                                        onValueChange = {
                                            onEvent(
                                                EditRecipeEvent.StepIngredientNameChanged(
                                                    stepIndex,
                                                    ingredientIndex,
                                                    it
                                                )
                                            )
                                        },
                                        modifier = Modifier.width(100.dp),
                                        textStyle = MaterialTheme.typography.bodyMedium
                                    )
                                    TextField(
                                        value = amount,
                                        onValueChange = {
                                            onEvent(
                                                EditRecipeEvent.StepIngredientAmountChanged(
                                                    stepIndex,
                                                    ingredientIndex,
                                                    it
                                                )
                                            )
                                        },
                                        modifier = Modifier.width(100.dp),
                                        textStyle = MaterialTheme.typography.bodyMedium
                                    )
                                }

//                                    Text(
//                                        text = "${ingredient.ingredient}: ${(ingredient.baseAmount)}g",
//                                        maxLines = 1,
//                                        overflow = TextOverflow.Ellipsis,
//                                        modifier = Modifier
//                                            //.background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp))
//                                            .padding(4.dp),
//                                        style = MaterialTheme.typography.bodyMedium
//                                    )
                            }
                        }
                    }
                }
            }

            item() {
                IconButton(
                    onClick = {
                        onEvent(EditRecipeEvent.ClickAddStep)
                        coroutineScope.launch {
                            listState.animateScrollToItem(steps.size)
                        }
                    },
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
        ingredientNamesLists = DummyData.DummySteps.map { step -> step.ingredients.map { ingredient -> ingredient.ingredient } },
        ingredientAmountsLists = DummyData.DummySteps.map { step -> step.ingredients.map { ingredient -> ingredient.baseAmount } },
        onEvent = {},
        dismissDialog = {})
}