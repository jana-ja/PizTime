package de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.janaja.piztime.feature_piz_recipes.domain.model.PizStepWithIngredients
import de.janaja.piztime.feature_piz_recipes.presentation.piz_recipe_detail.PizRecipeDetailEvent
import de.janaja.piztime.feature_piz_recipes.presentation.util.*

@Composable
fun StepsView(
    modifier: Modifier = Modifier,
    stepsWithIngredients: List<PizStepWithIngredients>,
    amount: Int,
    contentModifier: Modifier = Modifier,
    onEvent: (PizRecipeDetailEvent) -> Unit,
    editMode: Boolean
) {
//    // TODO this is only here to fix current preview issue with resource dimen values
//    val borderHeight: Dp =
//        100.dp //dimensionResource(id = R.dimen.topSheetBorderHeight).value
//
//    Surface(
//        modifier = modifier.bottomElevation(),
//        shape = TopSheetShape(borderHeight.value),
//        color = Color.White,
//        shadowElevation = 8.dp
//    ) {
    Box(
        modifier = contentModifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Column(
            modifier = Modifier.padding(bottom = 16.dp, end = 16.dp)
        ) {
            Log.i("StepsView", "I got recomposed!")

            Text(
                "Rezept:",
                style = MaterialTheme.typography.titleLarge
            )


            stepsWithIngredients.indices.forEach { stepIndex ->
                Column {
                    // step description
                    var rowModifier = Modifier.padding(0.dp)
                    if (editMode) rowModifier =
                        rowModifier.clickable { onEvent(PizRecipeDetailEvent.ClickEditStep(stepsWithIngredients[stepIndex].id)) }

                    Row(rowModifier) {
                        Text(
                            "${stepIndex + 1}: ",
                            Modifier.padding(PaddingValues(start = 16.dp, top = 16.dp)),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            stepsWithIngredients[stepIndex].description,
                            Modifier.padding(PaddingValues(top = 16.dp)),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    // step ingredients
                    if (stepsWithIngredients[stepIndex].ingredients.isNotEmpty()) {
                        SimpleFlowRow(
                            verticalGap = 8.dp,
                            horizontalGap = 8.dp,
                            modifier = Modifier.padding(start = 16.dp, top = 8.dp)
                        ) {
                            stepsWithIngredients[stepIndex].ingredients.forEach { ingredient ->
                                var ingredientModifier = Modifier.padding(4.dp)
                                if (editMode) ingredientModifier =
                                    ingredientModifier.clickable {
                                        onEvent(
                                            PizRecipeDetailEvent.ClickEditIngredient(
                                                ingredient.id,
                                                true,
                                                stepsWithIngredients[stepIndex].id
                                            )
                                        )
                                    }

                                Text(
                                    text = "${ingredient.ingredient}: ${(ingredient.baseAmount * amount).cut()}g",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = ingredientModifier
                                    //.background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp))
                                    ,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            if (editMode) {
                                IconButton(
                                    onClick = {
                                        onEvent(PizRecipeDetailEvent.ClickAddIngredient(false, stepsWithIngredients[stepIndex].id))
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Add,
                                        "add step ingredient"
                                    )
                                }
                            }
                        }
                    }
                }
            }
            if (editMode) {
                IconButton(
                    onClick = {
                        onEvent(PizRecipeDetailEvent.ClickAddStep)
                    }
                ) {
                    Icon(
                        Icons.Default.Add,
                        "add step"
                    )
                }
            }
        }
    }
//    }
}


@Preview
@Composable
fun DescriptionViewPreview() {
    StepsView(stepsWithIngredients = DummyData.DummySteps, amount = 4, onEvent = {}, editMode = false)
}

